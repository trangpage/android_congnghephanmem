package com.trangpig.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nhuocquy.model.*;
import com.nhuocquy.model.Conversation;
import com.trangpig.data.Data;

import java.util.List;

/**
 * Created by TrangPig on 04/21/2015.
 */
public class ChatFragment extends Fragment {
    private Account account;
    private ChatListAdapter adapter;
    List<Conversation> arrCon;
    private ListView listViewFriend;
    EditText edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatlist, container,false);
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        arrCon = account.getConversations();
        listViewFriend =(ListView) v.findViewById(R.id.lvChat);
        edit = (EditText) v.findViewById(R.id.txtBanBe);
        adapter = new ChatListAdapter(getActivity(),arrCon,R.layout.myitemlayout);
        listViewFriend.setAdapter(adapter);

        // su kien cho listview
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return v;
    }
}
