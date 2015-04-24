package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhuocquy.model.Friend;
import com.trangpig.myapp.R;

import java.util.List;


/**
 * Created by TrangPig on 04/06/2015.
 */
public class ListFriendAdapter extends ArrayAdapter<Friend> {

    Activity context;
    List<Friend> listFriend;
    int cur;

    public ListFriendAdapter(Activity context, List<Friend> listFriend, int cur) {
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
            final TextView tvTen = (TextView) convertView.findViewById(R.id.tvFr);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imgFr);
            final Friend con = listFriend.get(position);
            tvTen.setText(con.getName());
            imageView.setImageResource(R.drawable.left);

        }
        return convertView;
    }
}

