package com.trangpig.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
import com.trangpig.until.MyUri;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by user on 4/24/2015.
 */
public class MyService extends Service {
    Intent intents = new Intent("my-event");
    WebSocketClient webSocketClient;
    public static final String MES = "messsage";
    public static final String WEB = "web";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_SHORT).show();
        try {
            webSocketClient = new WebSocketClient(URI.create(String.format(MyUri.URL_WEBSOCKET,MyUri.IP,((Account)Data.getInstance().getAttribute(Data.ACOUNT)).getIdAcc() + ""))){
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMessage(String s) {
                    intents.putExtra(MES, s);
                    getApplicationContext().sendBroadcast(intents);
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };
            webSocketClient.connect();
            Data.getInstance().setAttribute(WEB,webSocketClient);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketClient.close();
    }
}
