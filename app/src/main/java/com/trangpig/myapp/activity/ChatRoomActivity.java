package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.MessageChat;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.MessagesListAdapter;
import com.trangpig.myapp.fragment.ListConversationFragment;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyConstant;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends Activity {


    public static final String ROOM_ID = "room_name";
    private Button btnSend;
    private ImageButton btnIcon;
    private EditText inputMsg;

    //
    String[] rooms;
    //    private ListView listViewMessages;
    private MessagesListAdapter adapter;
    private RecyclerView listViewMessages;

    private RestTemplate restTemplate;
    private Handler handlerSend, handlerRecive, handlerSentImg;
    List<MessageChat> listMessageChat;
    // chat new mes
    Account account;
    MessageChat newMes;
    MessageChat receiveMes;
    Message mesHandler;
    WebSocketClient webSocketClient;
    BroadcastReceiver broadcastReceiver;
    String json;
    ObjectMapper objectMapper;
    String mesBroadCast;
    ImageButton bntImg;
    int post;
    String headerMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_chat);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        rooms = getResources().getStringArray(R.array.string_array_room);
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        if (account == null) {
            Toast.makeText(ChatRoomActivity.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
            ChatRoomActivity.this.finish();
            return;
        }
        btnSend = (Button) findViewById(R.id.btnSend);
        btnIcon = (ImageButton) findViewById(R.id.btnIcon);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        bntImg = (ImageButton) findViewById(R.id.btnImg);
        listViewMessages = (RecyclerView) findViewById(R.id.list_view_messages);
        objectMapper = new ObjectMapper();

        listMessageChat = new ArrayList<>();
        adapter = new MessagesListAdapter(this, listMessageChat);
        listViewMessages.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listViewMessages.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(listMessageChat.size());
        listViewMessages.setItemAnimator(new DefaultItemAnimator());

        setTitle(rooms[getIntent().getIntExtra(ROOM_ID, 0)]);
        newMes = new MessageChat();
        newMes.setSender(account.retrieveAccountAsFriend());
//        newMes.setIdSender(account.getIdAcc());
//        newMes.setFromName(account.getName());
        newMes.setIdConversation(-1);

        //broadcastReceiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mesHandler = handlerRecive.obtainMessage();
                mesHandler.obj = intent.getStringExtra(MyService.MES);
                handlerRecive.sendMessage(mesHandler);

            }
        };

        headerMes = MyConstant.MESSAGE_CHAT_ROOM + "+" + getIntent().getIntExtra(ROOM_ID, 0) + ":";
        //handler
        handlerSend = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (webSocketClient == null || webSocketClient.isClosed() || !webSocketClient.isOpen()) {
                    webSocketClient = (WebSocketClient) Data.getInstance().getAttribute(MyService.WEB);
                }
                if(webSocketClient != null && (webSocketClient.isClosed() || !webSocketClient.isOpen())){
                    startService(new Intent(ChatRoomActivity.this, MyService.class));
                }
                newMes.setText(msg.obj.toString());
                try {
                    json = headerMes + objectMapper.writeValueAsString(newMes);
                    if(webSocketClient != null && webSocketClient.isOpen()) {
                        webSocketClient.send(json);
                        inputMsg.setText("");
                    }

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Clearing the input filed once message was sent
            }

        };
        handlerRecive = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mesBroadCast = msg.obj.toString();
                try {
                    receiveMes = objectMapper.readValue(mesBroadCast, MessageChat.class);
                    if (receiveMes.getIdConversation() == -1) {
                        listMessageChat.add(receiveMes);
                        post = listMessageChat.size() - 1;
                        adapter.notifyItemInserted(post);
                        linearLayoutManager.scrollToPosition(post);
                    } else if (!receiveMes.getText().contains(ConversationChat.MES_HINT_ON) && !receiveMes.getText().contains(ConversationChat.MES_HINT_OFF)) {
                        for (int i = 0; i < account.getConversations().size(); i++) {
                            if (receiveMes.getIdConversation() == account.getConversations().get(i).getIdCon()) {
                                account.getConversations().get(i).setReaded(false);
                                account.getConversations().get(i).addMessageChat(receiveMes);
                                notification(receiveMes);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        handlerSentImg = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //nhan Uri
                Uri uri = (Uri) msg.obj;
                MyFile myFile = Utils.getMyFileFromUri(uri, ChatRoomActivity.this);
                new AsyncTask<MyFile, Void, MyStatus>() {
                    @Override
                    protected MyStatus doInBackground(MyFile... myFiles) {
                        MyStatus status = null;
                        try {
                            status = restTemplate.postForObject(String.format(MyUri.URL_UP_IMAGE, MyUri.IP), myFiles[0], MyStatus.class);
                        } catch (RestClientException e) {
                            Log.e(ConversationChat.class.getName(), e.getMessage());
                            Toast.makeText(ChatRoomActivity.this, ChatRoomActivity.this.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus status) {
                        super.onPostExecute(status);
                        if (status == null) {
                            Toast.makeText(ChatRoomActivity.this, ChatRoomActivity.this.getResources().getString(R.string.send_files_failed), Toast.LENGTH_LONG).show();
                        } else {
                            mesHandler = handlerSend.obtainMessage();
                            mesHandler.obj = MessagesListAdapter.CHAR_ZERO + "image:" + status.getObj().toString();
                            handlerSend.sendMessage(mesHandler);
                        }
                    }
                }.execute(myFile);
            }
        };

        // bat su kien button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesHandler = handlerSend.obtainMessage();
                mesHandler.obj = inputMsg.getText().toString();
                handlerSend.sendMessage(mesHandler);
            }
        });
        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ChatRoomActivity.this, IconActivity.class), ConversationChat.ACTIVITY_ICON);
            }
        });
        bntImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), ConversationChat.PICK_PHOTO_FOR_AVATAR);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, ConversationChat.PICK_PHOTO_FOR_AVATAR);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.ACTION_ROOM_CHAT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConversationChat.ACTIVITY_ICON:
                switch (resultCode) {
                    case ConversationChat.ICON_SMALL:
                        if (data.getStringExtra(ConversationChat.KEY_ICON_STRING) != null)
                            inputMsg.append(data.getStringExtra(ConversationChat.KEY_ICON_STRING));
                        break;
                    case ConversationChat.ICON_BIG:
                        if (data.getStringExtra(ConversationChat.KEY_ICON_STRING) != null) {
                            mesHandler = handlerSend.obtainMessage();
                            mesHandler.obj = data.getStringExtra(ConversationChat.KEY_ICON_STRING);
                            mesHandler.what = ConversationChat.ICON_BIG;
                            handlerSend.sendMessage(mesHandler);
                        }
                        break;
                }
                break;
            case ConversationChat.PICK_PHOTO_FOR_AVATAR:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    Uri selectedImageUri = data.getData();
                    mesHandler = handlerSentImg.obtainMessage();
                    mesHandler.obj = selectedImageUri;
                    handlerSentImg.sendMessage(mesHandler);
                }
                break;
            default:
                break;
        }
    }

    public void notification(MessageChat mes) {
        Intent intent = new Intent(this, ConversationChat.class);
        intent.putExtra(ListConversationFragment.ID_CON, mes.getIdConversation());

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle(ChatRoomActivity.this.getResources().getString(R.string.new_message) + mes.getFromName())
                .setContentText(mes.getText())
                .setSmallIcon(R.drawable.message)
                .setContentIntent(pIntent).build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
        Utils.playBeep(this);
    }
}
