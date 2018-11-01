package com.bugbinc.afterburnerstats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bugbinc.afterburnerstats.models.Stat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final String SERVER_URI = "tcp://192.168.0.143:1883";

    private static final String TOPIC_STATS = "STATS";

    private MqttClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Go fullscreen
        int uiFlags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToServer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToServer() {
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(SERVER_URI, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            client.connect(connOpts);

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    try {
                        client.subscribe(TOPIC_STATS);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Type statsListType = new TypeToken<ArrayList<Stat>>(){}.getType();
                    List<Stat> statList = new Gson().fromJson(new String(message.getPayload()), statsListType);
                    listStats(statList);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            client.subscribe(TOPIC_STATS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void listStats(List<Stat> statList) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Stat stat : statList) {
            stringBuilder.append(stat.getName());
            stringBuilder.append(": ");
            stringBuilder.append(String.format("%.1f", stat.getValue()));
            stringBuilder.append(" ");
            stringBuilder.append(stat.getUnits());
            stringBuilder.append("\n");
        }
        ((TextView)findViewById(R.id.tv_Message)).setText(stringBuilder.toString());
    }
}
