package com.trangpig.myapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.nhuocquy.model.Conversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrangPig on 04/18/2015.
 */
public class ChatListAdapter extends ArrayAdapter<Conversation>{
        Activity contex;
        List<Conversation> arr = null;
        int id;

        public ChatListAdapter(Activity context, List<Conversation> arr, int id) {
            super(context, id, arr);
            this.contex = context;
            this.arr = arr;
            this.id = id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater layoutInflater = contex.getLayoutInflater();
                convertView = layoutInflater.inflate(id,null);
            }
            if(arr.size()> 0 && position>=0){
                final TextView tvTen = (TextView)convertView.findViewById(R.id.tvTen);
                final TextView tvLastSMS = (TextView)convertView.findViewById(R.id.tvLastSMS);
                final TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
                final Conversation con = arr.get(position);
                tvTen.setText(con.toString());
                tvLastSMS.setText(con.getListMes().get(0).getText());
                tvDate.setText(con.getListMes().get(0).getDate().toString());
            }
            return convertView;
        }
}
