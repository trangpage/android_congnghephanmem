package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.model.MessageChat;
import com.nhuocquy.myfile.MyFile;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.MessagesListAdapter;
import com.trangpig.myapp.fragment.ListConversationFragment;
import com.trangpig.myapp.fragment.ListFriendFragment;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyUri;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.nhuocquy.model.Message;


public class ConversationChat extends ActionBarActivity {
    public static final int ACTIVITY_ICON = 1;
    public static final int ICON_BIG = 2;
    public static final int ICON_SMALL = 3;
    static final int PICK_PHOTO_FOR_AVATAR = 4;

    public static final String KEY_ICON_STRING = "icon";

    private Button btnSend;
    private ImageButton btnIcon;
    private EditText inputMsg;

    private ListView listViewMessages;
    private MessagesListAdapter adapter;

    private long idCon = -1;
    private long[] idFriends;
    private RestTemplate restTemplate;
    private Conversation con;
    private Handler handlerSend, handlerRecive, handlerSentImg;
    List<MessageChat> listMessageChat;
    // chat new mes
    private Conversation contmp;
    List<MessageChat> listNewMes;
    MessageChat newMes;
    MessageChat receiveMes;
    Account acc;
    Message mesHandler;
    WebSocketClient webSocketClient;
    BroadcastReceiver broadcastReceiver;
    String json;
    ObjectMapper objectMapper;
    String mesBroadCast;
    Intent intent;
    private Account account;
    List<Conversation> arrCon;
    ImageButton bntImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_chat);
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        arrCon = account.getConversations();

        btnSend = (Button) findViewById(R.id.btnSend);
        btnIcon = (ImageButton) findViewById(R.id.btnIcon);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        bntImg = (ImageButton) findViewById(R.id.btnImg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        objectMapper = new ObjectMapper();

        handlerSend = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (webSocketClient == null) {
                    webSocketClient = (WebSocketClient) Data.getInstance().getAttribute(MyService.WEB);
                }
                newMes.setText(msg.obj.toString());
                try {
                    json = objectMapper.writeValueAsString(contmp);
                    webSocketClient.send(json);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (msg.what) {
                    case ICON_BIG:
                    case ICON_SMALL:
                        break;
                    default:
                        inputMsg.setText("");
                        break;
                }
                // Clearing the input filed once message was sent
            }

        };
        handlerRecive = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mesBroadCast = msg.obj.toString();
                showTost("Receive Message");
                try {
                    receiveMes = objectMapper.readValue(mesBroadCast, MessageChat.class);
                    if (con.getIdCon() == receiveMes.getIdConversation()) {
                        listMessageChat.add(receiveMes);
                        adapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < acc.getConversations().size(); i++) {
                            if (receiveMes.getIdConversation() == acc.getConversations().get(i).getIdCon()) {
                                acc.getConversations().get(i).setReaded(false);
                                acc.getConversations().get(i).addMessageChat(receiveMes);
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
                String fileName = msg.obj.toString();
                MyFile myFile = new MyFile(fileName);
                try {
                    String value = new ObjectMapper().writeValueAsString(myFile);
                    Log.e("tuyet......", myFile.getFileName());
                    Log.e("tuyet......", value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result = restTemplate.postForObject("http://192.168.137.1:8080/tuyensinh/image", myFile, String.class);
                if (result == null) {
                    Toast.makeText(ConversationChat.this, "Gửi file không thành công", Toast.LENGTH_LONG).show();
                } else {
                    if (webSocketClient == null) {
                        webSocketClient = (WebSocketClient) Data.getInstance().getAttribute(MyService.WEB);
                    }
                    webSocketClient.send(MessagesListAdapter.CHAR_ZERO + "image:" + result);
                }
            }
        };

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(ConversationChat.this, "Receive message from service", Toast.LENGTH_LONG).show();
                mesHandler = handlerRecive.obtainMessage();
                mesHandler.obj = intent.getStringExtra(MyService.MES);
                handlerRecive.sendMessage(mesHandler);

            }
        };

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        intent = getIntent();
        idCon = intent.getLongExtra(ListConversationFragment.ID_CON, -1);
        if (idCon == -1) {
            idFriends = intent.getLongArrayExtra(ListFriendFragment.ID_FRIENDS);
        }
        listMessageChat = new ArrayList<>();
        adapter = new MessagesListAdapter(ConversationChat.this, listMessageChat);
        listViewMessages.setAdapter(adapter);

        // chat mes mới
        acc = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        listNewMes = new ArrayList<MessageChat>();

        newMes = new MessageChat();
        newMes.setFromName(acc.getName());
        newMes.setIdSender(acc.getIdAcc());
        listNewMes.add(newMes);

        contmp = new Conversation();
        contmp.setListMes(listNewMes);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    con = restTemplate.postForObject(String.format(MyUri.CONVERSATION, MyUri.IP), (idCon != -1) ? new long[]{idCon} : idFriends, Conversation.class);
                    if (con != null && con.getListMes() != null) {
                        // tao conversation trong list conversation
//                        for (int i = 0; i < arrCon.size(); i++) {
//                            if (con.getIdCon() != arrCon.get(i).getIdCon()) {
//                                account.getConversations().add(0, con);
//                                Data.getInstance().setAttribute(Data.ACOUNT, account);
//                            }
//                        }
                        listMessageChat = con.getListMes();
                        adapter.setListMes(listMessageChat);
                        contmp.setIdCon(con.getIdCon());
                        contmp.setFriends(con.getFriends());
                    }
                } catch (RestClientException e) {

                    Toast.makeText(ConversationChat.this, "Khong the ket noi Internet", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {

                }
            }
        }).start();

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
                startActivityForResult(new Intent(ConversationChat.this, IconActivity.class), ACTIVITY_ICON);
            }
        });
        bntImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_ICON:
                switch (resultCode) {
                    case ICON_SMALL:
                        if (data.getStringExtra(KEY_ICON_STRING) != null)
                            inputMsg.append(data.getStringExtra(KEY_ICON_STRING));
                        break;
                    case ICON_BIG:
                        if (data.getStringExtra(KEY_ICON_STRING) != null) {
                            mesHandler = handlerSend.obtainMessage();
                            mesHandler.obj = data.getStringExtra(KEY_ICON_STRING);
                            mesHandler.what = ICON_BIG;
                            handlerSend.sendMessage(mesHandler);
                        }
                        break;
                }
                break;
            case PICK_PHOTO_FOR_AVATAR:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    if (picturePath == null)
                        Log.e("tuyet.................", "null");
                    cursor.close();
                    mesHandler = handlerSentImg.obtainMessage();
                    mesHandler.obj = picturePath;
                    handlerSentImg.sendMessage(mesHandler);
                }

                break;
            default:
                break;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("my-event"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void showTost(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
