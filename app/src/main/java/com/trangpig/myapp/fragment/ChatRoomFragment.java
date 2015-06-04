package com.trangpig.myapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ChatRoomActivity;
import com.trangpig.myapp.adapter.ChatRoomAdapter;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyConstant;

import org.java_websocket.WebSocket;


public class ChatRoomFragment extends Fragment {
    WebSocket webSocket;
    ListView listViewRoom;
    ChatRoomAdapter chatRoomAdapter;
    BroadcastReceiver broadcastRoom;
    Handler handler;
    Message message;

    public ChatRoomFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webSocket = (WebSocket)Data.getInstance().getAttribute(MyService.WEB);
        if(webSocket == null || webSocket.isClosed()){
            getActivity().finish();
            return;
        }

        chatRoomAdapter = new ChatRoomAdapter(getActivity());
        broadcastRoom = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mes = intent.getStringExtra(MyService.REGISTRY_ROOM);
                if(mes.contains(MyConstant.MESSAGE_REGISTRY_CHAT_GROUP) && mes.contains("success")){
                   Intent intentRoom = new Intent(getActivity(), ChatRoomActivity.class);
                    intentRoom.putExtra(ChatRoomActivity.ROOM_ID,Integer.parseInt(mes.substring(mes.indexOf('+')+1,mes.indexOf(':'))));
                    startActivity(intentRoom);
                }
            }
        };
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                webSocket.send(msg.obj.toString());
            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_room, container, false);
        listViewRoom = (ListView) v.findViewById(R.id.lv_room);
        listViewRoom.setAdapter(chatRoomAdapter);
        listViewRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                message = handler.obtainMessage();
                message.obj = String.format("%s+%s", MyConstant.MESSAGE_REGISTRY_CHAT_GROUP, position);
                handler.sendMessage(message);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastRoom, new IntentFilter(MyService.ACTION_OPEN_ROOM_CHAT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastRoom);
    }
}
