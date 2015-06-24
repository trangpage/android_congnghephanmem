package com.trangpig.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
import com.trangpig.until.MyConstant;
import com.trangpig.until.MyUri;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by user on 4/24/2015.
 */
public class MyService extends Service {
    //action for intent to send broadcastreceiver
    public final static String ACTION_ADD_FRIEND_NOTIFY = "com.trangpig.notify";
    public final static String ACTION_CONVERSATION_CHAT = "conversation-chat";
    public final static String ACTION_MESSAGE_CHAT = "com.trangpig.message.notify";
    public final static String ACTION_OPEN_ROOM_CHAT = "open-room-chat";
    public final static String ACTION_ROOM_CHAT = "room-chat";
    Intent intentBroadcast = new Intent();
    WebSocketClient webSocketClient;
    // key string in intent
    public static final String MES = "messsage";
    public static final String WEB = "web";
    public static final String REGISTRY_ROOM = "regstryroom";
    Handler handler;
    Message message;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_SHORT).show();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String mes = (String) msg.obj;
                        if (mes.contains(MyConstant.MESSAGE_ADD_FRIEND)) {
                            intentBroadcast.setAction(ACTION_ADD_FRIEND_NOTIFY);
                            intentBroadcast.putExtra(MES, mes.replace(MyConstant.MESSAGE_ADD_FRIEND, ""));
                        }
                        if (mes.contains(MyConstant.MESSAGE_CHAT_CONVERSATION)) {
                            intentBroadcast.setAction(ACTION_MESSAGE_CHAT);
                            intentBroadcast.putExtra(MES, mes.replace(MyConstant.MESSAGE_CHAT_CONVERSATION, ""));
                        }
                        if (mes.contains(MyConstant.MESSAGE_REGISTRY_CHAT_GROUP)) {
                            intentBroadcast.setAction(ACTION_OPEN_ROOM_CHAT);
                            intentBroadcast.putExtra(REGISTRY_ROOM, mes);
                        }
                        if (mes.contains(MyConstant.MESSAGE_CHAT_ROOM)) {
                            showTost(mes);
                            intentBroadcast.setAction(ACTION_ROOM_CHAT);
                            intentBroadcast.putExtra(MES, mes.substring(mes.indexOf(':') + 1));
                        }
                        sendBroadcast(intentBroadcast);
                        break;
                    case 0:
                        showTost(msg.obj.toString());
                        break;
                    default:
                }

            }
        };
        try {
            webSocketClient = new WebSocketClient(URI.create(String.format(MyUri.URL_WEBSOCKET, MyUri.IP, ((Account) Data.getInstance().getAttribute(Data.ACOUNT)).getIdAcc() + ""))) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    message = handler.obtainMessage();
                    message.obj = "Open";
                    message.what = 0;
                    handler.sendMessage(message);
                }

                @Override
                public void onMessage(String s) {
                    message = handler.obtainMessage();
                    message.obj = s;
                    message.what = 1;
                    handler.sendMessage(message);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    message = handler.obtainMessage();
                    message.obj = "Close";
                    message.what = 0;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    message = handler.obtainMessage();
                    message.obj = e.getMessage();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            };
            webSocketClient.connect();
            Data.getInstance().setAttribute(WEB, webSocketClient);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
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

    private void showTost(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
