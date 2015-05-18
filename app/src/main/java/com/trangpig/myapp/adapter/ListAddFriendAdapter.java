package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.nhuocquy.model.Friend;
import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by user on 5/18/2015.
 */
public class ListAddFriendAdapter extends ArrayAdapter<Friend> {

    Activity context;
    List<Friend> listFriend;
    int cur;

    public ListAddFriendAdapter(Activity context, List<Friend> listFriend, int cur) {
        super(context, cur, listFriend);
        this.context = context;
        this.listFriend = listFriend;
        this.cur = cur;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(cur, null);
        }
        if (listFriend.size() > 0 && position >= 0) {
            final TextView tvAddFr = (TextView) convertView.findViewById(R.id.tvAddFr);
            final ImageView imgAddFr = (ImageView) convertView.findViewById(R.id.imgAddFr);
            final Button bntAddFr = (Button) convertView.findViewById(R.id.bntAddFr);
            final Friend con = listFriend.get(position);
            tvAddFr.setText(con.getName());
            imgAddFr.setImageResource(R.drawable.left);

        }
        return convertView;

    }
}
