package demo.service;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Created by n-240 on 2016/3/11.
 *
 * @author cwf
 */
public class MClient {
    private MqttClient client;
    private String deviceid;

    public MqttClient getClient() {
        return client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
