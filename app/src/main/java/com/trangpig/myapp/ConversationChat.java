package com.trangpig.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.model.MessageChat;
import com.trangpig.data.Data;
import com.trangpig.myapp.fragment.ListConversationFragment;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyUri;

import org.java_websocket.client.WebSocketClient;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.nhuocquy.model.Message;


public class ConversationChat extends ActionBarActivity {


    private Button btnSend;
    private EditText inputMsg;

    private WebSocketClient client;
    private ListView listViewMessages;
    private MessagesListAdapter adapter;

    private long idCon = -1;
    private RestTemplate restTemplate;
    private Conversation con;
    private Handler handler;
    List<MessageChat> listMessageChat;
    // chat new mes
    private Conversation contmp;
    List<MessageChat> listNewMes;
    MessageChat newMes;
    MessageChat receiveMes;
    Account acc;
    Message mesHandel;
    WebSocketClient webSocketClient;
    BroadcastReceiver broadcastReceiver;
    String json;
    ObjectMapper objectMapper;
    String mesBroadCast;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        objectMapper = new ObjectMapper();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(webSocketClient == null){
                    webSocketClient = (WebSocketClient) Data.getInstance().getAttribute(MyService.WEB);
                }
                newMes.setText(msg.obj.toString());
                try {
                    json = objectMapper.writeValueAsString(contmp);
                    webSocketClient.send(json);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }

        };

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mesBroadCast = intent.getStringExtra(MyService.MES);
                try {

                     receiveMes = objectMapper.readValue(mesBroadCast, MessageChat.class);
                    if(contmp.getIdCon()==newMes.getIdConversation()) {
                        listMessageChat.add(newMes);
                    }else{
                        for (int i= 0; i < acc.getConversations().size(); i++){
                            if(receiveMes.getIdConversation()== acc.getConversations().get(i).getIdCon()){
                                acc.getConversations().get(i).setReaded(false);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();


            }
        };
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        intent  = getIntent();
        idCon = intent.getLongExtra(ListConversationFragment.ID_CON, -1);
        listMessageChat = new ArrayList<>();
        adapter = new MessagesListAdapter(ConversationChat.this, listMessageChat);
        listViewMessages.setAdapter(adapter);

        // chat mes mới
        acc = (Account)Data.getInstance().getAttribute(Data.ACOUNT);
        listNewMes = new ArrayList<MessageChat>();

        newMes.setFromName(acc.getName());
        newMes.setIdSender(acc.getIdAcc());
        listNewMes.add(newMes);

        contmp.setIdCon(con.getIdCon());
        contmp.setFriends(con.getFriends());
        contmp.setListMes(listNewMes);

      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  con = restTemplate.postForObject(String.format(MyUri.CONVERSATION, MyUri.IP), new long[]{idCon}, Conversation.class);
                  if(con.getListMes() != null) {
                      listMessageChat = con.getListMes();
                      adapter.setListMes(listMessageChat);
                      handler.sendEmptyMessage(1);
                  }
              } catch (RestClientException e) {

                  Toast.makeText(ConversationChat.this,"Khong the ket noi Internet",Toast.LENGTH_SHORT).show();
                  e.printStackTrace();
              }finally {

              }
          }
      }).start();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesHandel = handler.obtainMessage();
                mesHandel.obj = inputMsg.getText().toString();
                handler.sendMessage(mesHandel);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_conversation, menu);
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
