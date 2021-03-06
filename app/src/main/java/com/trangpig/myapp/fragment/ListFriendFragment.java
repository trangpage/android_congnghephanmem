package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhuocquy.model.Friend;
import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
import com.trangpig.myapp.activity.ConversationChat;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListFriendAdapter;

import java.util.List;

/**
 * Created by TrangPig on 04/06/2015.
 */
public class ListFriendFragment extends Fragment {

    private Account account;
    private ListFriendAdapter frAdapter;
    private ListView listViewFriend;
    List<Friend> arrFr;
    Intent intent;
    public static final String ID_FRIENDS = "idFriends";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friendlist, container,false);
        //
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        arrFr = account.getListFrs();
        listViewFriend =(ListView) v.findViewById(R.id.lvFr);
        frAdapter = new ListFriendAdapter(getActivity(),arrFr,R.layout.my_item_layout_friend);
        listViewFriend.setAdapter(frAdapter);

        // su kien cho listview
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent = new Intent(ListFriendFragment.this.getActivity(), ConversationChat.class);
                    intent.putExtra(ID_FRIENDS, new long[]{account.getIdAcc(), arrFr.get(position).getIdFriend()});
                    startActivity(intent);
            }
        });
        return v;
    }
}
