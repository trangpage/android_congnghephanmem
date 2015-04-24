package com.trangpig.myapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nhuocquy.model.Friend;
import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
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
    EditText edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friendlist, container,false);

        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        arrFr = account.getListFrs();
        listViewFriend =(ListView) v.findViewById(R.id.lvFr);
        edit = (EditText) v.findViewById(R.id.txtFr);
        frAdapter = new ListFriendAdapter(getActivity(),arrFr,R.layout.my_item_layout_friend);
        listViewFriend.setAdapter(frAdapter);

        // su kien cho listview
        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(),)
            }
        });
        return v;
    }
}
