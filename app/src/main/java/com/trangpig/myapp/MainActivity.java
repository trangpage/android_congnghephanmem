package com.trangpig.myapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.renderscript.ScriptIntrinsicHistogram;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuoc.quy.dto.SinhVien;
import com.trangpig.model.Conversation;
import com.trangpig.model.Friend;
import com.trangpig.model.MessageChat;
import com.trangpig.until.Utils;
import com.trangpig.until.WsConfig;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends FragmentActivity
//        implements ActionBar.TabListener
{
//Button bnt ;
//   EditText txtId, txtIp, masv, name, classs, sc1, sc2, sc3, ave, error;
//    ActionBar tabBar;

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;

    private WebSocketClient client;

    // Chat messages list adapter
    private ChatListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;
    private  ListView listViewChat;
    ArrayList<Conversation> arrCon;
    ArrayList<Friend> arrFrs;
    ArrayList<MessageChat> arrMes;
    EditText edit;

    private Utils utils;

    // Client name
    private String name = null;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        setContentView(R.layout.sinhvien);

//        txtId = (EditText) findViewById(R.id.editId);
//        txtIp = (EditText) findViewById(R.id.editIP);
//        masv = (EditText) findViewById(R.id.txtmasv);
//        name = (EditText) findViewById(R.id.txtname);
//        classs = (EditText) findViewById(R.id.etxtclass);
//        sc1 = (EditText) findViewById(R.id.txtsc1);
//        sc2 = (EditText) findViewById(R.id.txtsc2);
//        sc3 = (EditText) findViewById(R.id.txtsc3);
//        ave = (EditText) findViewById(R.id.txtaver);
//        error = (EditText) findViewById(R.id.error);
//
//        bnt = (Button) findViewById(R.id.button3);
//        bnt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    RestTemplate tmp = new RestTemplate();
//                    tmp.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                    SinhVien sinhVien = tmp.getForObject("http://" + txtIp.getText().toString() + ":8080/hello/sv/" + txtId.getText().toString(), SinhVien.class);
//                    masv.setText(Integer.toString(sinhVien.getId()));
//                    classs.setText(sinhVien.getClasss());
//                    sc1.setText(Integer.toString(sinhVien.getScore1()));
//                    sc2.setText(Integer.toString(sinhVien.getScore2()));
//                    sc3.setText(Integer.toString(sinhVien.getScore3()));
//                    name.setText(sinhVien.getName());
//                    ave.setText(Double.toString(sinhVien.getaverage()) + "");
//                }catch(Exception e){
//                    error.setText(e.getMessage());
//                }
//            }
//
//        });
//-------------------------
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_chat);
//
//        btnSend = (Button) findViewById(R.id.btnSend);
//        inputMsg = (EditText) findViewById(R.id.inputMsg);
//        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
//
//        utils = new Utils(getApplicationContext());
//
//        // Getting the person name from previous screen
//        Intent i = getIntent();
//        name = i.getStringExtra("name");
//
//        btnSend.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // Sending message to web socket server
//                sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText()
//                        .toString()));
//
//                // Clearing the input filed once message was sent
//                inputMsg.setText("");
//            }
//        });
//
//        listMessages = new ArrayList<Message>();
////        listMessages = new ArrayList<>();
//        adapter = new MessagesListAdapter(this, listMessages);
//        listViewMessages.setAdapter(adapter);
//
//        /**
//         * Creating web socket client. This will have callback methods
//         * */
//
//         client = new WebSocketClient(URI.create(String.format(WsConfig.URL_WEBSOCKET,i.getStringExtra("ip"))
//                + name)) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//            }
//
//            @Override
//            public void onMessage(String s) {
//                Log.d(TAG, String.format("Got string message! %s", s));
//                parseMessage(s);
//                showToast("mes");
//            }
//
////            @Override
////            public void onMessage(byte[] data) {
////                Log.d(TAG, String.format("Got binary message! %s",
////                        bytesToHex(data)));
////
////                // Message will be in JSON format
////                parseMessage(bytesToHex(data));
////            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                String message = String.format(Locale.US,
//                        "Disconnected! Code: %d Reason: %s", i, s);
//
//                showToast("close");
//
//                // clear the session id from shared preferences
//                utils.storeSessionId(null);
//            }
//
//            @Override
//            public void onError(Exception error) {
//                Log.e(TAG, "Error! : " + error);
//                showToast("Error! : " + error);
//            }
//        };
//        client.connect();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlist);
        edit = (EditText)findViewById(R.id.txtBanBe);
        listViewChat = (ListView)findViewById(R.id.lvChat);
        arrCon=new ArrayList<Conversation>();
        arrFrs = new ArrayList<Friend>();
        arrMes = new ArrayList<MessageChat>();
        arrFrs.add(new Friend(1, "Trang, Tuyet"));
        arrMes.add(new MessageChat(1,1,"Trang, Tuyet","hello",new Date()));
        arrCon.add(new Conversation(1,arrFrs,arrMes));
        arrCon.add(new Conversation(1,arrFrs,arrMes));
        arrCon.add(new Conversation(1,arrFrs,arrMes));

        adapter = new ChatListAdapter(this, arrCon, R.layout.myitemlayout);
        listViewChat.setAdapter(adapter);
    }

    private void sendMessageToServer(String message) {
        if (client != null) {
            client.send(message);
        }
    }

    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                Message m = new Message(fromName, message, isSelf);

                // Appending the message to chat list
                appendMessage(m);

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(client != null ){
            client.close();
        }
    }

    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SinhVien sv = new SinhVien();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
