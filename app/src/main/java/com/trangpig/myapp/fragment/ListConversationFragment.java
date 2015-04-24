package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.trangpig.data.Data;
import com.trangpig.myapp.ConversationChat;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListConversationAdapter;

import java.util.List;

/**
 * Created by TrangPig on 04/21/2015.
 */
public class ListConversationFragment extends Fragment {
    private Account account;
    private ListConversationAdapter adapter;
    List<Conversation> arrCon;
    private ListView listViewFriend;
    EditText edit;
    Intent intentCons;
    public static final String ID_CON = "idCon";

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
        adapter = new ListConversationAdapter(getActivity(),arrCon,R.layout.myitemlayout);
        listViewFriend.setAdapter(adapter);

        // su kien cho listview
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intentCons = new Intent(ListConversationFragment.this.getActivity(), ConversationChat.class);
            intentCons.putExtra(ID_CON, arrCon.get(position).getIdCon());
            startActivity(intentCons);
            }
        });
        return v;
    }
}
