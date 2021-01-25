package com.example.fb_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TemperatureActivity extends AppCompatActivity {

    private static final String TAG = "TemperatureActivity" ;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    MqttClient client;
    String topic = "topic/FB_Application";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MySingletonClass.getInstance().isDarkMode()){
            getTheme().applyStyle(R.style.AppThemeDark, true);
        }
        else{
            getTheme().applyStyle(R.style.AppThemeLight, true);
        }
        setContentView(R.layout.activity_temperature);

        listView = (ListView) findViewById(R.id.ListViewTemperature);
        listItems = new ArrayList<String>();

        try {
            client = new MqttClient("tcp://"+MySingletonClass.getInstance().getIpAddress()+":1883", "AndroidSub", new MemoryPersistence());
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "connectionLost");
                    cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    JSONObject obj = new JSONObject(payload);
                    if (obj.getString("order").equals("return")){
                        if (obj.getString("value").equals("init")){
                            int len = obj.getInt("len");
                            for (int i=0 ; i<len ; i++ ){
                                listItems.add(obj.getString("sensor"+i) + " : " + obj.getString("sensor"+i+"val"));
                            }
                            final ArrayAdapter<String> adp = new ArrayAdapter<String>(TemperatureActivity.this, android.R.layout.simple_list_item_1, listItems);
                            runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      listView.setAdapter(adp);
                                  }
                            });

                        }
                        else if (obj.getString("value").equals("update")){
                            int len = obj.getInt("len");
                            for (int i=0 ; i<len ; i++ ){
                                listItems.set(i,obj.getString("sensor"+i) + " : " + obj.getString("sensor"+i+"val"));
                            }
                            final ArrayAdapter<String> adp = new ArrayAdapter<String>(TemperatureActivity.this, android.R.layout.simple_list_item_1, listItems);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(adp);
                                }
                            });
                        }

                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.e(TAG, "deliveryComplete");
                }
            });

            client.connect();
            client.subscribe(topic);

            MqttMessage mess = new MqttMessage();
            String str = "{\"order\" : \"sensor\", \"value\" : \"init\"}";
            mess.setPayload(str.getBytes());
            client.publish(topic, mess);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void update(View view) throws MqttException {
        MqttMessage mess = new MqttMessage();
        String str = "{\"order\" : \"sensor\", \"value\" : \"update\"}";
        mess.setPayload(str.getBytes());
        client.publish(topic, mess);
    }
}