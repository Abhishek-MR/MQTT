package com.example.abhi.mqtt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;


import helpers.MqttHelper;
import helpers.MqttHelper1;


public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    MqttHelper1 mqttHelper1;

    // values to publish

    static String HOSTNAME = "tcp://m11.cloudmqtt.com:16201";
    static String USERNAME = "rcduaeoh";
    static String PASSWORD = "hm3O7P_0KiXi";
    String TOPIC1 = "sensor/snd";
    String TOPIC2 = "sensor/rec";
    String MSG = "NULL";

    Button pubbut1 ;
    Button pubbut2 ;

    TextView dataReceived1;
    TextView dataReceived2;

    int i=0;


    MqttAndroidClient client;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived1 = (TextView) findViewById(R.id.dataReceived1);
        dataReceived2 = (TextView) findViewById(R.id.dataReceived2);

        pubbut1 = (Button) findViewById(R.id.pubBut1);
        pubbut2 = (Button) findViewById(R.id.pubBut2);



        pubbut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                pub(TOPIC1,MSG+i);
            }
        });

        pubbut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pub(TOPIC2,MSG+1);
            }
        });

        startMqtt();
        startMqtt2();

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), HOSTNAME, clientId);

        MqttConnectOptions options = new MqttConnectOptions();


        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());



        try {
            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Toast.makeText(MainActivity.this," Not Connected",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // function to publish
    public void pub(String topic, String message){

        try {

            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    // function to subscribe
    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived1.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void startMqtt2() {
        mqttHelper1 = new MqttHelper1(getApplicationContext());
        mqttHelper1.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived2.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}