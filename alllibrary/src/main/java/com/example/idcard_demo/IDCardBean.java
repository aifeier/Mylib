package com.example.idcard_demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import wintone.idcard.android.AuthParameterMessage;
import wintone.idcard.android.AuthService;
import wintone.idcard.android.RecogParameterMessage;
import wintone.idcard.android.ResultMessage;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wintone.lisence.Common;
import com.wintone.lisence.MachineCode;

public class IDCardBean extends Activity {
	public static final String TAG = "IDCardBean";
	private String cls;
	private int nTypeInitIDCard;
	private int nTypeLoadImageToMemory;
	private int nMainID;
	private int[] nSubID;
	private boolean GetSubID;
	private String lpHeadFileName;
	private String lpFileName;
	private boolean GetVersionInfo;
	private String sn;
	private String logo;
	private boolean isCut;
	private String authfile;
	private String userdata;
	private String server;
	private String strtimelog = "";
	private String versionfile = "";
	private boolean isSaveCut = false;
	private String[] fieldname = new String[20];
	private String[] fieldvalue = new String[20];
	private String ReturnGetVersionInfo;
	private int ReturnGetSubID = -1;
	private int ReturnSaveHeadImage = -1;
	private int ReturnInitIDCard = -1;
	private int ReturnLoadImageToMemory = -1;
	private int ReturnRecogIDCard = -1;
	private int ReturnAuthority = -1;
	private String mcode;
	private String deviceId;
	private String androId;
	private String simId;
	private String returntype = "";
	private ImageView jpgView;
	private ImageView shapView;
	private ImageView stripView;
	private ImageView logoView;
	private Bitmap bitmLp;
	private Bitmap bitmNew;
	private TextView textView;
	public Animation scale;
	public Animation translate;
	public Animation scaleReduce;
	private ProgressBar progressBar;
	private int screenWidth;
	private int screenHeight;
	private String devcode;
	private boolean isCheckDevType;
	private String datefile;
	private int triggertype;
	private boolean isAutoClassify = false;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private int[] array;
	private int multiRows;
	private String lpFileOut;
	private String lpFileIn;
	private boolean isAutoRecog = false;
	private AuthService.authBinder authBinder;
	private RecogService.recogBinder recogBinder;
	private String lock = "lock";
	private boolean bool = true;
	private Date date1;
	private Date date2;
	private boolean isGetRecogFieldPos;
	public ServiceConnection authConn = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			IDCardBean.this.authBinder = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			IDCardBean.this.authBinder = ((AuthService.authBinder) service);
			try {
				AuthParameterMessage apm = new AuthParameterMessage();
				apm.authfile = IDCardBean.this.authfile;
				apm.sn = IDCardBean.this.sn;
				apm.devcode = IDCardBean.this.devcode;
				apm.datefile = IDCardBean.this.datefile;
				apm.idtype = IDCardBean.this.nMainID + "";
				apm.versionfile = IDCardBean.this.versionfile;
				apm.server = IDCardBean.this.server;
				IDCardBean.this.ReturnAuthority = IDCardBean.this.authBinder.getIDCardAuth(apm);

				if (IDCardBean.this.ReturnAuthority == 0) {
					Intent recogIntent = new Intent(IDCardBean.this.getApplicationContext(),
							RecogService.class);
					IDCardBean.this.bindService(recogIntent, IDCardBean.this.recogConn, Context.BIND_AUTO_CREATE);

					ResultMessage resultMessage = new ResultMessage();
					resultMessage.ReturnAuthority = IDCardBean.this.ReturnAuthority;
					IDCardBean.this.ReturnResultToActivity(resultMessage);
				}
			} catch (Exception e) {
				IDCardBean.this.ReturnAuthority = -1;
			} finally {
				if (IDCardBean.this.authBinder != null)
					IDCardBean.this.unbindService(IDCardBean.this.authConn);
			}
		}
	};

	public ServiceConnection recogConn = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			IDCardBean.this.recogConn = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				synchronized (IDCardBean.this.lock) {
					IDCardBean.this.recogBinder = ((RecogService.recogBinder) service);
					if (IDCardBean.this.recogBinder != null) {
						new Thread() {
							public void run() {
								IDCardBean.this.startRecog();
							}
						}.start();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 3) {
				IDCardBean.this.jpgView.startAnimation(IDCardBean.this.scale);
				IDCardBean.this.shapView.startAnimation(IDCardBean.this.scaleReduce);
				IDCardBean.this.textView.setVisibility(View.VISIBLE);
				int cut_image_id = IDCardBean.this.getResources().getIdentifier("cut_image",
						"string", IDCardBean.this.getPackageName());
				String str1 = IDCardBean.this.getResources().getString(cut_image_id);
				IDCardBean.this.textView.setText(str1);
			}

			if (msg.obj != null) {
				String newpath = (String) msg.obj;
				IDCardBean.this.bitmNew = IDCardBean.this.compressImageView(newpath);
				IDCardBean.this.jpgView.clearAnimation();
				IDCardBean.this.jpgView.setVisibility(View.INVISIBLE);
				IDCardBean.this.shapView.setImageBitmap(IDCardBean.this.bitmNew);
				IDCardBean.this.stripView.setVisibility(View.VISIBLE);
				IDCardBean.this.stripView.setAlpha(80);
				IDCardBean.this.stripView.startAnimation(IDCardBean.this.translate);
				int distinguish_id = IDCardBean.this.getResources().getIdentifier("distinguish",
						"string", IDCardBean.this.getPackageName());
				String str2 = IDCardBean.this.getResources().getString(distinguish_id);
				IDCardBean.this.textView.setText(str2);
			}

			if (msg.what == 10) {
				IDCardBean.this.progressBar.setProgress(10);
			}
			if (msg.what == 40) {
				IDCardBean.this.progressBar.setProgress(40);
			}
			if (msg.what == 80) {
				IDCardBean.this.progressBar.setProgress(80);
			}
			if (msg.what == 90) {
				IDCardBean.this.progressBar.setProgress(90);
			}
			if (msg.what == 100)
				IDCardBean.this.progressBar.setProgress(100);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.date1 = new Date();
		requestWindowFeature(1);
		getWindow().setFlags(1024, 1024);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int idcardbean_layout = getResources().getIdentifier("idcardbean", "layout",
				getPackageName());
		setContentView(idcardbean_layout);
		int translate_animation_anim = getResources().getIdentifier("translate_animation", "anim",
				getPackageName());
		this.translate = AnimationUtils.loadAnimation(this, translate_animation_anim);

		int scale_animation_anim = getResources().getIdentifier("scale_animation", "anim",
				getPackageName());
		this.scale = AnimationUtils.loadAnimation(this, scale_animation_anim);
		int scal_reduce_animation_anim = getResources().getIdentifier("scale_reduce_animation",
				"anim", getPackageName());
		this.scaleReduce = AnimationUtils.loadAnimation(this, scal_reduce_animation_anim);

		this.screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		this.screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		findView();
		this.stripView.setVisibility(View.INVISIBLE);
		this.textView.setVisibility(View.INVISIBLE);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		StringBuilder sb = new StringBuilder();
		sb.append(telephonyManager.getDeviceId());
		this.deviceId = sb.toString();

		StringBuilder sb1 = new StringBuilder();
		sb1.append(Settings.Secure.getString(getContentResolver(), "android_id"));
		this.androId = sb1.toString();

		StringBuilder sb2 = new StringBuilder();
		sb2.append(telephonyManager.getSimSerialNumber());
		this.simId = sb2.toString();

		MachineCode machineCode = new MachineCode();
		this.mcode = machineCode.MachineNO("1.0", this.deviceId, this.androId, this.simId);

		Intent mIntent = getIntent();
		this.cls = mIntent.getStringExtra("cls");
		this.nTypeInitIDCard = mIntent.getIntExtra("nTypeInitIDCard", 0);
		this.nTypeLoadImageToMemory = mIntent.getIntExtra("nTypeLoadImageToMemory", 0);
		this.nMainID = mIntent.getIntExtra("nMainID", 0);
		this.nSubID = mIntent.getIntArrayExtra("nSubID");
		this.GetSubID = mIntent.getBooleanExtra("GetSubID", true);
		this.lpHeadFileName = mIntent.getStringExtra("lpHeadFileName");
		this.lpFileName = mIntent.getStringExtra("lpFileName");
		this.GetVersionInfo = mIntent.getBooleanExtra("GetVersionInfo", true);
		this.sn = mIntent.getStringExtra("sn");
		this.server = mIntent.getStringExtra("server");
		this.logo = mIntent.getStringExtra("logo");
		this.isCut = mIntent.getBooleanExtra("isCut", true);
		this.authfile = mIntent.getStringExtra("authfile");
		this.userdata = mIntent.getStringExtra("userdata");
		this.returntype = mIntent.getStringExtra("returntype");
		this.datefile = mIntent.getStringExtra("datefile");
		this.devcode = mIntent.getStringExtra("devcode");
		this.isCheckDevType = mIntent.getBooleanExtra("isCheckDevType", false);
		this.versionfile = mIntent.getStringExtra("versionfile");
		this.triggertype = mIntent.getIntExtra("triggertype", 0);
		this.isSaveCut = mIntent.getBooleanExtra("isSaveCut", false);
		this.isAutoClassify = mIntent.getBooleanExtra("isAutoClassify", false);

		this.isGetRecogFieldPos = mIntent.getBooleanExtra("isGetRecogFieldPos", false);
		if (this.authfile != null) {
			this.x1 = mIntent.getIntExtra("x1", this.x1);
		}
		this.y1 = mIntent.getIntExtra("y1", this.y1);
		this.x2 = mIntent.getIntExtra("x2", this.x2);
		this.y2 = mIntent.getIntExtra("y2", this.y2);
		this.multiRows = mIntent.getIntExtra("multiRows", 1);

		this.array = new int[4];
		this.array[0] = this.x1;
		this.array[1] = this.y1;
		this.array[2] = this.x2;
		this.array[3] = this.y2;
		this.isAutoRecog = this.lpFileName.contains("_cut.jpg");
		this.bitmLp = compressImageView(this.lpFileName);
		this.jpgView.setImageBitmap(this.bitmLp);
		initAnimation();

		if ((this.logo != null) && (new File(this.logo).exists())) {
			String myJpgPath = this.logo;
			Uri logoUri = Uri.fromFile(new File(myJpgPath));
			this.logoView.setImageURI(logoUri);
		} else {
			this.logo = "";
		}

		if (this.nMainID == 0) {
			String cfg = "";
			try {
				cfg = readtxt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] cfgs = cfg.split("==##");
			if ((cfgs != null) && (cfgs.length >= 2)) {
				this.nMainID = String2Int(cfgs[0]);
			}
		}
		if (this.nMainID == 0) {
			this.nMainID = 2;
		}
		ViewGroup.LayoutParams para = this.shapView.getLayoutParams();
		if (this.nMainID == 1100) {
			para.width = 700;
			para.height = 150;
			this.shapView.setLayoutParams(para);
		} else {
			para.width = 600;
			para.height = 450;
			this.shapView.setLayoutParams(para);
		}

		this.progressBar.setIndeterminate(false);
		this.progressBar.setMax(100);
		this.progressBar.setProgress(0);
		Intent authIntent = new Intent(getApplicationContext(), AuthService.class);
		bindService(authIntent, this.authConn, Context.BIND_AUTO_CREATE);
	}

	public void startRecog() {
		RecogParameterMessage rpm = new RecogParameterMessage();
		rpm.nTypeInitIDCard = this.nTypeInitIDCard;
		rpm.nTypeLoadImageToMemory = this.nTypeLoadImageToMemory;
		rpm.nMainID = this.nMainID;
		rpm.nSubID = this.nSubID;
		rpm.GetSubID = this.GetSubID;
		rpm.lpHeadFileName = this.lpHeadFileName;
		rpm.lpFileName = this.lpFileName;
		rpm.GetVersionInfo = this.GetVersionInfo;
		rpm.sn = this.sn;
		rpm.logo = this.logo;
		rpm.isCut = this.isCut;
		rpm.authfile = this.authfile;
		rpm.array = this.array;
		rpm.multiRows = this.multiRows;
		rpm.isAutoRecog = this.isAutoRecog;
		rpm.userdata = this.userdata;
		rpm.devcode = this.devcode;
		rpm.dateFilePath = this.datefile;
		rpm.versionfile = this.versionfile;
		rpm.triggertype = this.triggertype;
		rpm.isSaveCut = this.isSaveCut;
		rpm.isCheckDevType = this.isCheckDevType;
		rpm.isAutoClassify = this.isAutoClassify;
		rpm.isGetRecogFieldPos = this.isGetRecogFieldPos;
		this.ReturnAuthority = this.recogBinder.IDCardAuthAndInit(rpm);
		if (this.ReturnAuthority == -10090) {
			this.ReturnAuthority = 0;
		}
		System.out.println("ReturnInitIDCard:" + this.recogBinder.IDCardGetInit());

		if ((this.ReturnAuthority == 0) && (this.recogBinder.IDCardGetInit() == 0)) {
			try {
				Message mesg = new Message();
				mesg.what = 10;
				this.handler.sendMessage(mesg);

				mesg = new Message();
				mesg.what = 3;
				this.handler.sendMessage(mesg);

				if ((((this.nMainID == 1100) || (this.nMainID == 1101))) && (!(this.isAutoRecog))) {
					String resultlpFileName = this.recogBinder.IDCardLoadAndCutLineationImage(rpm);
					Message msg = this.handler.obtainMessage();
					msg.obj = resultlpFileName;
					this.handler.sendMessage(msg);
					mesg = new Message();
					mesg.what = 80;
					this.handler.sendMessage(mesg);
				} else {
					this.recogBinder.IDCardLoadNoLineationImage(rpm);
					mesg = new Message();
					mesg.what = 40;
					this.handler.sendMessage(mesg);
					this.recogBinder.IDCardCutNoLineationImage(rpm);
					Message msg = this.handler.obtainMessage();
					msg.obj = this.lpFileName;
					this.handler.sendMessage(msg);
					mesg = new Message();
					mesg.what = 80;
					this.handler.sendMessage(mesg);
				}
				this.recogBinder.IDCardRecognitionImage(rpm);
				this.recogBinder.IDCardGetRecognitionResult(rpm);
				mesg = new Message();
				mesg.what = 90;
				this.handler.sendMessage(mesg);
				ResultMessage resultMessage = this.recogBinder.IDCardReturnRecognitionResult(rpm);
				mesg = new Message();
				mesg.what = 100;
				this.handler.sendMessage(mesg);
				ReturnResultToActivity(resultMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ResultMessage resultMessage = new ResultMessage();
			resultMessage.ReturnAuthority = this.ReturnAuthority;
			resultMessage.ReturnInitIDCard = this.recogBinder.IDCardGetInit();
			ReturnResultToActivity(resultMessage);
		}
	}

	public void findView() {
		int imgview_id = getResources().getIdentifier("imgview", "id", getPackageName());
		this.jpgView = ((ImageView) findViewById(imgview_id));
		int shape_id = getResources().getIdentifier("shape", "id", getPackageName());
		this.shapView = ((ImageView) findViewById(shape_id));
		int strip_id = getResources().getIdentifier("strip", "id", getPackageName());
		this.stripView = ((ImageView) findViewById(strip_id));
		int photo_logo_id = getResources().getIdentifier("photo_logo", "id", getPackageName());
		this.logoView = ((ImageView) findViewById(photo_logo_id));
		int progressBar_id = getResources().getIdentifier("progressBar", "id", getPackageName());
		this.progressBar = ((ProgressBar) findViewById(progressBar_id));
		int text_id = getResources().getIdentifier("text", "id", getPackageName());
		this.textView = ((TextView) findViewById(text_id));
	}

	public void initAnimation() {
		this.scale.setDuration(2000L);
//		this.scale.setInterpolator(this, 17432581);
		this.scale.setRepeatCount(1);
		this.scale.setFillAfter(true);
		this.scaleReduce.setDuration(3000L);
//		this.scaleReduce.setInterpolator(this, 17432587);
		this.scaleReduce.setRepeatCount(2);
		this.scaleReduce.setFillAfter(true);
		this.translate.setDuration(2000L);
//		this.translate.setInterpolator(this, 17432581);
		this.translate.setRepeatCount(4);
		this.translate.setFillAfter(true);
	}

	public Bitmap compressImageView(String path) {
		Bitmap bitmap = null;
		try {
			InputStream temp = new FileInputStream(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(temp, null, options);

			temp.close();

			int i = 0;
			bitmap = null;
			while (true) {
				if ((options.outWidth >> i <= 640) && (options.outHeight >> i <= 480)) {
					temp = new FileInputStream(path);
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(temp, null, options);
					return bitmap;
				}
				++i;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private int String2Int(String stri) {
		int nRet = 0;
		if ((stri != null) && (!(stri.equals("")))) {
			try {
				nRet = Integer.parseInt(stri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nRet;
	}

	public String readtxt() throws IOException {
		Common common = new Common();
		String paths = common.getSDPath();
		if ((paths == null) || (paths.equals(""))) {
			return "";
		}
		String fullpath = paths + "/AndroidWT/idcard.cfg";
		File file = new File(fullpath);
		if (!(file.exists())) {
			return "";
		}
		FileReader fileReader = new FileReader(fullpath);
		BufferedReader br = new BufferedReader(fileReader);
		String str = "";
		String r = br.readLine();
		while (r != null) {
			str = str + r;
			r = br.readLine();
		}
		br.close();
		fileReader.close();
		return str;
	}

	public void ReturnResultToActivity(ResultMessage resultMessage) {
		this.date2 = new Date();
		Log.i("TimeTAG", "Time=" + (this.date2.getTime() - this.date1.getTime()));
		resultMessage.time = String.valueOf(this.date2.getTime() - this.date1.getTime());
		try {
			Intent intent = new Intent("idcard.receiver");
			Bundle bundle = new Bundle();

			bundle.putSerializable("GetFieldName", resultMessage.GetFieldName);
			bundle.putSerializable("GetRecogResult", resultMessage.GetRecogResult);
			bundle.putSerializable("textNamePosition",
					(Serializable) resultMessage.textNamePosition);

			bundle.putString("ReturnTime", resultMessage.time);

			bundle.putInt("ReturnGetSubID", resultMessage.ReturnGetSubID);
			bundle.putInt("ReturnSaveHeadImage", resultMessage.ReturnSaveHeadImage);
			bundle.putInt("ReturnLoadImageToMemory", resultMessage.ReturnLoadImageToMemory);
			bundle.putInt("ReturnInitIDCard", resultMessage.ReturnInitIDCard);
			bundle.putInt("ReturnRecogIDCard", resultMessage.ReturnRecogIDCard);
			bundle.putInt("ReturnAuthority", resultMessage.ReturnAuthority);
			bundle.putString("ReturnGetVersionInfo", resultMessage.ReturnGetVersionInfo);
			bundle.putString("ReturnUserData", this.userdata);
			bundle.putSerializable("textNamePosition", resultMessage);
			if (!(resultMessage.lpFileName.equals("lpFileName")))
				bundle.putString("ReturnLPFileName", resultMessage.lpFileName);
			else {
				bundle.putString("ReturnLPFileName", this.lpFileName);
			}
			bundle.putString("lpFileOut", resultMessage.lpFileOut);
			bundle.putInt("x1", this.array[0]);
			bundle.putInt("y1", this.array[1]);
			bundle.putInt("x2", this.array[2]);
			bundle.putInt("y2", this.array[3]);
			intent.putExtras(bundle);
			if (this.returntype.equals("withvalue")) {
				setResult(-1, intent);
				finish();
				return;
			}
			startActivity(intent);
			finish();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	protected void onStop() {
		super.onStop();
		this.jpgView.destroyDrawingCache();
		this.shapView.destroyDrawingCache();
		this.stripView.destroyDrawingCache();
		if (this.bitmLp != null) {
			this.bitmLp.recycle();
			System.gc();
		}
		if (this.bitmNew != null) {
			this.bitmNew.recycle();
			System.gc();
		}
		if (this.recogBinder != null) {
			unbindService(this.recogConn);
		}
		finish();
	}

	protected void onDestroy() {
		super.onDestroy();
	}
}