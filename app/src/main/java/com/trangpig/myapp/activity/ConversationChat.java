package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.model.MessageChat;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyStatus;
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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//import com.nhuocquy.model.Message;


public class ConversationChat extends ActionBarActivity {
    public static final int ACTIVITY_ICON = 1;
    public static final int ICON_BIG = 2;
    public static final int ICON_SMALL = 3;
    static final int PICK_PHOTO_FOR_AVATAR = 4;

    public static final String KEY_ICON_STRING = "icon";

    public static final String MES_HINT_ON = (char) 0 + "hintOn";
    public static final String MES_HINT_OFF = (char) 0 + "hintOff";

    private Button btnSend;
    private ImageButton btnIcon;
    private EditText inputMsg;
    //
    private TextView txtHint;
    private TextWatcher t;

    //    private ListView listViewMessages;
    private MessagesListAdapter adapter;
    private RecyclerView listViewMessages;

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
    int post;


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
        listViewMessages = (RecyclerView) findViewById(R.id.list_view_messages);
        objectMapper = new ObjectMapper();

        //
        txtHint = (TextView) findViewById(R.id.txtHint);
//        txtHint.setVisibility(View.INVISIBLE);


        t = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("heo.....watcher", s.toString());
                if (s.length() == 1) {
                    mesHandler = handlerSend.obtainMessage();
                    mesHandler.obj = MES_HINT_ON;
                    handlerSend.sendMessage(mesHandler);
                }
                if (s.length() == 0) {
                    mesHandler = handlerSend.obtainMessage();
                    mesHandler.obj = MES_HINT_OFF;
                    handlerSend.sendMessage(mesHandler);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        inputMsg.addTextChangedListener(t);
//        inputMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                inputMsg.addTextChangedListener(t);
//                return false;
//    }
//});

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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listViewMessages.setLayoutManager(linearLayoutManager);
        linearLayoutManager.scrollToPosition(listMessageChat.size());
        listViewMessages.setItemAnimator(new DefaultItemAnimator());
        // chat mes mới
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        listNewMes = new ArrayList<MessageChat>();

        newMes = new MessageChat();
        newMes.setFromName(account.getName());
        newMes.setIdSender(account.getIdAcc());
        listNewMes.add(newMes);

        contmp = new Conversation();
        contmp.setListMes(listNewMes);
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
                        //
                        if (receiveMes.getText().contains(MES_HINT_ON)) {
//                            txtHint.setVisibility(View.VISIBLE);
                            txtHint.setText(receiveMes.getFromName() + " đang nhập...");
                        } else if (receiveMes.getText().contains(MES_HINT_OFF)) {
                            txtHint.setText("");
                        } else {
//                            txtHint.setVisibility(View.INVISIBLE);
//                            txtHint.setText("");
                            listMessageChat.add(receiveMes);
                            post = listMessageChat.size() - 1;
                            adapter.notifyItemInserted(post);
                            linearLayoutManager.scrollToPosition(post);
                        }
                    } else if (!receiveMes.getText().contains(MES_HINT_ON) && !receiveMes.getText().contains(MES_HINT_OFF)) {
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
                MyFile myFile = getMyFileFromUri(uri);
                new AsyncTask<MyFile, Void, MyStatus>() {
                    @Override
                    protected MyStatus doInBackground(MyFile... myFiles) {
                        MyStatus status = null;
                        try {
                            status = restTemplate.postForObject(String.format(MyUri.URL_UP_IMAGE, MyUri.IP), myFiles[0], MyStatus.class);
                        } catch (RestClientException e) {
                            Log.e(ConversationChat.class.getName(), e.getMessage());
                            Toast.makeText(ConversationChat.this, "Không thể upload Image!!", Toast.LENGTH_LONG).show();
                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus status) {
                        super.onPostExecute(status);
                        if (status == null) {
                            Toast.makeText(ConversationChat.this, "Gửi file không thành công", Toast.LENGTH_LONG).show();
                        } else {
                            mesHandler = handlerSend.obtainMessage();
                            mesHandler.obj = MessagesListAdapter.CHAR_ZERO + "image:" + status.getObj().toString();
                            handlerSend.sendMessage(mesHandler);
                        }
                    }
                }.execute(myFile);
            }
        };

        new AsyncTask<Void, Void, Conversation>() {

            @Override
            protected Conversation doInBackground(Void... params) {
                try {
                    con = restTemplate.postForObject(String.format(MyUri.CONVERSATION, MyUri.IP), (idCon != -1) ? new long[]{idCon} : idFriends, Conversation.class);
                } catch (RestClientException e) {
                    e.printStackTrace();
                    con = null;
                }
                return con;
            }

            @Override
            protected void onPostExecute(Conversation con) {
                super.onPostExecute(con);
                if (con != null && con.getListMes() != null) {
                    listMessageChat = con.getListMes();
                    linearLayoutManager.scrollToPosition(listMessageChat.size() - 1);
                    adapter.setListMes(listMessageChat);
                    contmp.setIdCon(con.getIdCon());
                    contmp.setFriends(con.getFriends());
                    setTitle(con.selectNames());
                }
            }
        }.execute();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesHandler = handlerSend.obtainMessage();
                mesHandler.obj = inputMsg.getText().toString();
                handlerSend.sendMessage(mesHandler);
                inputMsg.setText("");
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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), PICK_PHOTO_FOR_AVATAR);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
                }
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
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.ACTION_EVENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        for (Conversation c : account.getConversations()) {
            if (c.getIdCon() == con.getIdCon()) {
                if (con.getListMes().size() > 0)
                    c.addMessageChat(con.getListMes().get(con.getListMes().size() - 1));
                c.setReaded(true);
                break;
            }
        }
    }

    private void showTost(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    /**
     * Just for API before KITKAT
     */
    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        try {
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        Log.i("nhuocquy..quy....", path);

        return path;
    }

    private MyFile getMyFileFromUri(Uri uri) {
        String TAG = "nhuoc..quy....";
        MyFile myFile = new MyFile();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String fileName = getPath(uri);
            myFile = new MyFile(fileName);
        } else {
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null, null);
            DataInputStream dis = null;
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.i(TAG, "Display Name: " + displayName);
                    int size = 0;
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (!cursor.isNull(sizeIndex)) {
                        // Technically the column stores an int, but cursor.getString()
                        // will do the conversion automatically.
                        size = cursor.getInt(sizeIndex);
                    }
                    Log.i(TAG, "Size: " + size);
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    dis = new DataInputStream(inputStream);
                    byte[] data = new byte[size];
                    dis.readFully(data);
                    myFile.setFileName(displayName);
                    myFile.setData(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                if (dis != null)
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return myFile;
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

    public void notification(MessageChat mes) {
        Intent intent = new Intent(this, ConversationChat.class);
        intent.putExtra(ListConversationFragment.ID_CON, mes.getIdConversation());

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Bạn có tin nhắn mới từ: " + mes.getFromName())
                .setContentText(mes.getText())
                .setSmallIcon(R.drawable.message)
                .setContentIntent(pIntent).build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
        playBeep();
    }
}
