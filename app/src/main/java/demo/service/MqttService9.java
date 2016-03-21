package demo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MqttService9 extends Service implements MqttCallback
{

	Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), msg.getData().getString("data"), Toast.LENGTH_LONG).show();

			return false;
		}
	});

	private static final int id = 9;

	private static final int MAX = 240;

	public static final String 		DEBUG_TAG = "MqttService"; // Debug TAG
	
	private static final String		MQTT_THREAD_NAME = "MqttService[" + DEBUG_TAG + "]"; // Handler Thread ID
	
//	private static final String 	MQTT_BROKER = /*"m2m.eclipse.org"*/"192.168.100.195"; // Broker URL or IP Address
//	private static final int 		MQTT_PORT = 1883;				// Broker Port

	private static final String 	MQTT_BROKER = "192.168.100.195";/*"m2m.eclipse.org"*//*"192.168.100.195";*/ // Broker URL or IP Address
	private static final int 		MQTT_PORT = 1883;

	public static final int			MQTT_QOS_0 = 0; // QOS Level 0 ( Delivery Once no confirmation )
	public static final int 		MQTT_QOS_1 = 1; // QOS Level 1 ( Delevery at least Once with confirmation )
	public static final int			MQTT_QOS_2 = 2; // QOS Level 2 ( Delivery only once with confirmation with handshake )
	
	private static final int 		MQTT_KEEP_ALIVE = 240000; // KeepAlive Interval in MS
	private static final String		MQTT_KEEP_ALIVE_TOPIC_FORAMT = "/users/%s/keepalive"; // Topic format for KeepAlives
	private static final byte[] 	MQTT_KEEP_ALIVE_MESSAGE = { 0 }; // Keep Alive message to send
	private static final int		MQTT_KEEP_ALIVE_QOS = MQTT_QOS_0; // Default Keepalive QOS
	
	private static final boolean 	MQTT_CLEAN_SESSION = true; // Start a clean session?
	
	private static final String 	MQTT_URL_FORMAT = "tcp://%s:%d"; // URL Format normally don't change
	
	private static final String 	ACTION_START 	= DEBUG_TAG + ".START"; // Action to start
	private static final String 	ACTION_STOP		= DEBUG_TAG + ".STOP"; // Action to stop
	private static final String 	ACTION_KEEPALIVE= DEBUG_TAG + ".KEEPALIVE"; // Action to keep alive used by alarm manager
	private static final String 	ACTION_RECONNECT= DEBUG_TAG + ".RECONNECT"; // Action to reconnect
	
	
	private static final String 	DEVICE_ID_FORMAT = "andr_%s"; // Device ID Format, add any prefix you'd like
																  // Note: There is a 23 character limit you will get
																  // An NPE if you go over that limit
	private boolean mStarted = false; // Is the Client started?
	private String mDeviceId = "ttxiaomi";		  // Device ID, Secure.ANDROID_ID
	private String SubscribeId = "mobile.TQ";
	private Handler mConnHandler;	  // Seperate Handler thread for networking
	private List<Handler> handlerList;
	private List<MClient> clientList;
	
	private MqttDefaultFilePersistence mDataStore; // Defaults to FileStore
	private MemoryPersistence mMemStore; 		// On Fail reverts to MemoryStore
	private MqttConnectOptions mOpts;			// Connection Options
	
	private MqttTopic mKeepAliveTopic;			// Instance Variable for Keepalive topic
	
	private MqttClient mClient;					// Mqtt Client

	private AlarmManager mAlarmManager;			// Alarm manager to perform repeating tasks
	private ConnectivityManager mConnectivityManager; // To check for connectivity changes

	/**
	 * Start MQTT Client
	 * @param ctx context to start the service with
	 * @return void
	 */
	public static void actionStart(Context ctx) {
		Intent i = new Intent(ctx,MqttService9.class);
		i.setAction(ACTION_START);
		ctx.startService(i);
	}
	/**
	 * Stop MQTT Client
	 * @param ctx context to start the service with
	 * @return void
	 */
	public static void actionStop(Context ctx) {
		Intent i = new Intent(ctx,MqttService9.class);
		i.setAction(ACTION_STOP);
		ctx.startService(i);
	}
	/**
	 * Send a KeepAlive Message
	 * @param ctx context to start the service with
	 * @return void
	 */
	public static void actionKeepalive(Context ctx) {
		Intent i = new Intent(ctx,MqttService9.class);
		i.setAction(ACTION_KEEPALIVE);
		ctx.startService(i);
	}
	
	/**
	 * Initalizes the DeviceId and most instance variables
	 * Including the Connection Handler, Datastore, Alarm Manager
	 * and ConnectivityManager.
	 */
	@Override
	public void onCreate() {
		super.onCreate();


//		mDeviceId = String.format(DEVICE_ID_FORMAT,
//				Secure.getString(getContentResolver(), Secure.ANDROID_ID));
//
//		mDeviceId = "tt";
		
		HandlerThread thread = new HandlerThread(MQTT_THREAD_NAME);
		thread.start();
		
		mConnHandler = new Handler(thread.getLooper());
		handlerList = new ArrayList<>();
		for(int i = 0; i < MAX; i++){
			HandlerThread thread1 = new HandlerThread(mDeviceId + i);
			thread1.start();
			handlerList.add(new Handler(thread1.getLooper()));
		}
		clientList = new ArrayList<>();
		
		try {
			mDataStore = new MqttDefaultFilePersistence(getCacheDir().getAbsolutePath());
		} catch(MqttPersistenceException e) {
			e.printStackTrace();
			mDataStore = null;
			mMemStore = new MemoryPersistence();
		}
		
		mOpts = new MqttConnectOptions();
		mOpts.setCleanSession(MQTT_CLEAN_SESSION);
		// Do not set keep alive interval on mOpts we keep track of it with alarm's
		
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	}
	
	/**
	 * Service onStartCommand
	 * Handles the action passed via the Intent
	 * 
	 * @return START_REDELIVER_INTENT
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
//		mDeviceId = "tt";
		String action = intent.getAction();
		Log.i(DEBUG_TAG, "Received action of " + action);
		
		if(action == null) {
			Log.i(DEBUG_TAG,"Starting service with no action\n Probably from a crash");
		} else {
			if(action.equals(ACTION_START)) {
				Log.i(DEBUG_TAG,"Received ACTION_START");
				start();
			} else if(action.equals(ACTION_STOP)) {
				stop();
			} else if(action.equals(ACTION_KEEPALIVE)) {
					keepAlive();
			} else if(action.equals(ACTION_RECONNECT)) {
				if(isNetworkAvailable()) {
					reconnectIfNecessary();
				}
			}
		}
		
		return START_REDELIVER_INTENT;
	}
	
	/**
	 * Attempts connect to the Mqtt Broker
	 * and listen for Connectivity changes
	 * via ConnectivityManager.CONNECTVITIY_ACTION BroadcastReceiver
	 */
	private synchronized void start() {
		if(mStarted) {
			Log.i(DEBUG_TAG,"Attempt to start while already started");
			return;
		}
		
		if(hasScheduledKeepAlives()) {
			stopKeepAlives();
		}
		
		connect();
		
		registerReceiver(mConnectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	/**
	 * Attempts to stop the Mqtt client
	 * as well as halting all keep alive messages queued
	 * in the alarm manager
	 */
	private synchronized void stop() {
		if(!mStarted) {
			Log.i(DEBUG_TAG, "Attemtpign to stop connection that isn't running");
			return;
		}

		for(int i = 0 ; i < MAX; i ++) {
			final MqttClient client =  clientList.get(i).getClient();
			clientList.remove(client);
			if (client != null) {
				handlerList.get(i).post(new Runnable() {
					@Override
					public void run() {
						try {
							client.disconnect();
						} catch (MqttException ex) {
							ex.printStackTrace();
						}
						mClient = null;
						mStarted = false;
						Log.i(DEBUG_TAG, "stop keep alives " + client.getClientId());
//						stopKeepAlives();
					}
				});
			}
		}

		stopKeepAlives();
		unregisterReceiver(mConnectivityReceiver);
	}
	/**
	 * Connects to the broker with the appropriate datastore
	 */
	private synchronized void connect() {

			for(int i = 0; i < MAX; i++) {

				String deviceId = mDeviceId + i;

				try {
					mDataStore = new MqttDefaultFilePersistence(getCacheDir().getAbsolutePath());
				} catch(MqttPersistenceException e) {
					e.printStackTrace();
					mDataStore = null;
					mMemStore = new MemoryPersistence();
				}

				String url = String.format(Locale.US, MQTT_URL_FORMAT, MQTT_BROKER, MQTT_PORT);
				Log.i(DEBUG_TAG, "Connecting with URL: " + url);
				try {
					if (mDataStore != null) {
						Log.i(DEBUG_TAG, "Connecting with DataStore");
						mClient = new MqttClient(url, deviceId, mDataStore);
					} else {
						Log.i(DEBUG_TAG, "Connecting with MemStore");
						mClient = new MqttClient(url, deviceId, mMemStore);
					}
					Log.i(DEBUG_TAG, "clientId: " + mClient.getClientId());
				} catch (MqttException e) {
					e.printStackTrace();
				}
				MClient client = new MClient();
				client.setClient(mClient);
				client.setDeviceid(deviceId);
				clientList.add(client);
				final int a = i;
				mOpts = new MqttConnectOptions();
				mOpts.setCleanSession(MQTT_CLEAN_SESSION);
				handlerList.get(i).post(new Runnable() {
					@Override
					public void run() {
						try {
							clientList.get(a).getClient().connect(mOpts);

							clientList.get(a).getClient().subscribe(SubscribeId, 0);

							clientList.get(a).getClient().setCallback(MqttService9.this);

							mStarted = true; // Service is now connected

							Log.i(DEBUG_TAG, "Successfully connected and subscribed starting keep alives " + clientList.get(a).getDeviceid());

							startKeepAlives();
						} catch (MqttException e) {
							e.printStackTrace();
						}
					}
				});
			}
	}
	/**
	 * Schedules keep alives via a PendingIntent
	 * in the Alarm Manager
	 */
	private void startKeepAlives() {
		Intent i = new Intent();
		i.setClass(this, MqttService9.class);
		i.setAction(ACTION_KEEPALIVE);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + MQTT_KEEP_ALIVE,
				MQTT_KEEP_ALIVE, pi);
	}
	/**
	 * Cancels the Pending Intent
	 * in the alarm manager
	 */
	private void stopKeepAlives() {
		Intent i = new Intent();
		i.setClass(this, MqttService9.class);
		i.setAction(ACTION_KEEPALIVE);
		PendingIntent pi = PendingIntent.getService(this, 0, i , 0);
		mAlarmManager.cancel(pi);
	}
	/**
	 * Publishes a KeepALive to the topic
	 * in the broker
	 */
	private synchronized void keepAlive() {
		if(isConnected()) {
			try {
				sendKeepAlive();
				return;
			} catch(MqttConnectivityException ex) {
				ex.printStackTrace();
				reconnectIfNecessary();
			} catch(MqttPersistenceException ex) {
				ex.printStackTrace();
				stop();
			} catch(MqttException ex) {
				ex.printStackTrace();
				stop();
			}
		}
	}
	/**
	 * Checkes the current connectivity
	 * and reconnects if it is required.
	 */
	private synchronized void reconnectIfNecessary() {
		if(mStarted && mClient == null) {
			connect();
		}
	}
	/**
	 * Query's the NetworkInfo via ConnectivityManager
	 * to return the current connected state
	 * @return boolean true if we are connected false otherwise
	 */
	private boolean isNetworkAvailable() {
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		
		return (info == null) ? false : info.isConnected();
	}
	/**
	 * Verifies the client State with our local connected state
	 * @return true if its a match we are connected false if we aren't connected
	 */
	private boolean isConnected() {
		if(mStarted && mClient != null && !mClient.isConnected()) {
			Log.i(DEBUG_TAG,"Mismatch between what we think is connected and what is connected");
		}
		
		if(mClient != null) {
			return (mStarted && mClient.isConnected()) ? true : false;
		}
		
		return false;
	}
	/**
	 * Receiver that listens for connectivity chanes
	 * via ConnectivityManager
	 */
	private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(DEBUG_TAG,"Connectivity Changed...");
		}
	};
	/**
	 * Sends a Keep Alive message to the specified topic
//	 * @see MQTT_KEEP_ALIVE_MESSAGE
//	 * @see MQTT_KEEP_ALIVE_TOPIC_FORMAT
	 * @return MqttDeliveryToken specified token you can choose to wait for completion
	 */
	private synchronized void sendKeepAlive()
		throws MqttConnectivityException, MqttPersistenceException, MqttException {
//		if(!isConnected())
//			throw new MqttConnectivityException();
		for(MClient client: clientList) {
//			if (mKeepAliveTopic == null) {
				mKeepAliveTopic = client.getClient().getTopic(
						String.format(Locale.US, MQTT_KEEP_ALIVE_TOPIC_FORAMT, mDeviceId));
//			}

			Log.i(DEBUG_TAG, "Sending Keepalive to " + MQTT_BROKER);

			MqttMessage message = new MqttMessage(MQTT_KEEP_ALIVE_MESSAGE);
			message.setQos(MQTT_KEEP_ALIVE_QOS);
			mKeepAliveTopic.publish(client.getDeviceid().getBytes(), MQTT_KEEP_ALIVE_QOS, true);
//			return mKeepAliveTopic.publish(message);
			mKeepAliveTopic.publish(message);
		}
	}
	/**
	 * Query's the AlarmManager to check if there is
	 * a keep alive currently scheduled
	 * @return true if there is currently one scheduled false otherwise
	 */
	private synchronized boolean hasScheduledKeepAlives() {
		Intent i = new Intent();
		i.setClass(this, MqttService9.class);
		i.setAction(ACTION_KEEPALIVE);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_NO_CREATE);
		
		return (pi != null) ? true : false;
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	/**
	 * Connectivity Lost from broker
	 */
	@Override
	public void connectionLost(Throwable arg0) {
		stopKeepAlives();
		
		mClient = null;
		
		if(isNetworkAvailable()) {
			reconnectIfNecessary();
		}
	}
	/**
	 * Publish Message Completion
	 */
	@Override
	public void deliveryComplete(MqttDeliveryToken arg0) {
		try {
			Log.i(DEBUG_TAG, new String(arg0.getMessage().getPayload()));
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Received Message from broker
	 */
	@Override
	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {

		Log.i(DEBUG_TAG, "  Topic:\t" + topic.getName() +
				"  Message:\t" + new String(message.getPayload()) +
				"  QoS:\t" + message.getQos());
		Bundle bundle = new Bundle();
		bundle.putString("data", "  Topic:\t" + topic.getName() +
				"  Message:\t" + new String(message.getPayload()) +
				"  QoS:\t" + message.getQos());
		Message message1 = new Message();
		message1.setData(bundle);
		handler.sendMessage(message1);
//		Intent intent = new Intent(PushActivity.ACTION);
//		intent.putExtra(PushActivity.KEY_service, id);
//		sendBroadcast(intent);
	}


	/**
	 * MqttConnectivityException Exception class
	 */
	private class MqttConnectivityException extends Exception {
		private static final long serialVersionUID = -7385866796799469420L; 
	}
}
