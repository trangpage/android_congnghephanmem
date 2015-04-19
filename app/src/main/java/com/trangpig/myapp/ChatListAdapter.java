package com.trangpig.myapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trangpig.model.Conversation;

import java.util.ArrayList;

/**
 * Created by TrangPig on 04/18/2015.
 */
public class ChatListAdapter extends ArrayAdapter<Conversation>{
        Activity contex;
        ArrayList<Conversation> arr = null;
        int id;

        public ChatListAdapter(Activity context, ArrayList<Conversation> arr, int id) {
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
                tvTen.setText(con.getTen());
                tvLastSMS.setText(con.getListMes().get(0).getText());
                tvDate.setText(con.getListMes().get(0).getDate().toString());
            }
            return convertView;
        }
}
