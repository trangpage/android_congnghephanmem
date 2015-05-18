package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Friend;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.PersonalActivity;
import com.trangpig.myapp.adapter.ListAddFriendAdapter;
import com.trangpig.until.MyUri;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 5/18/2015.
 */
public class ListAddFriendFragment extends Fragment {
    private Account account;
    private ListAddFriendAdapter listAddFriendAdapter;
    private ListView listViewAddFriend;
    List<Friend> arrAddFr;
    Friend[] arrAddFr2;
    EditText editSearch;
    Intent intent;
    public static final String ID_FRIENDS = "idFriends";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friendlist, container,false);

        editSearch = (EditText) v.findViewById(R.id.txtFr);
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);

        //l?y danh s�ch b?n b� ch?a k?t b?n
        new AsyncTask<Long, Void, Friend[]>(){
            RestTemplate restTemplateAddFr = new RestTemplate();
            @Override
            protected Friend[] doInBackground(Long... params) {
                try {
                    arrAddFr2 = new Friend[0];
                    arrAddFr2 = restTemplateAddFr.getForObject(String.format(MyUri.URL_GET_LIST_ADD_FRIEND, MyUri.IP, account.getIdAcc()), Friend[].class);
                }catch (RestClientException e){
                    e.printStackTrace();

                }
                return arrAddFr2;
            }

            @Override
            protected void onPostExecute(Friend[] friends) {
                super.onPostExecute(friends);
                arrAddFr = Arrays.asList(friends);
                listAddFriendAdapter.setListAddFriend(arrAddFr);
                listAddFriendAdapter.notifyDataSetChanged();
            }
        }.execute(account.getIdAcc());
//        arrAddFr = account.getListMakeFrs();


        listViewAddFriend =(ListView) v.findViewById(R.id.lvFr);
        listAddFriendAdapter = new ListAddFriendAdapter(getActivity(),arrAddFr,R.layout.my_item_layout_add_friend);
        listViewAddFriend.setAdapter(listAddFriendAdapter);

        // su kien cho listview
        listViewAddFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(ListAddFriendFragment.this.getActivity(), PersonalActivity.class);
                intent.putExtra(ID_FRIENDS, new long[]{account.getIdAcc(), arrAddFr.get(position).getIdFriend()});
                startActivity(intent);
            }
        });
        return v;
    }

}
