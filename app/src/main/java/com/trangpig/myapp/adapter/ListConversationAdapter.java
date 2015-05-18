package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhuocquy.model.Conversation;
import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by TrangPig on 04/18/2015.
 */
public class ListConversationAdapter extends ArrayAdapter<Conversation> {
    Activity contex;
    List<Conversation> arr = null;
    int id;
    int size;

    public ListConversationAdapter(Activity context, List<Conversation> arr, int id) {
        super(context, id, arr);
        this.contex = context;
        this.arr = arr;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = contex.getLayoutInflater();
            convertView = layoutInflater.inflate(id, null);
        }
        if (arr.size() > 0 && position >= 0) {
            final TextView tvTen = (TextView) convertView.findViewById(R.id.tvTen);
            final TextView tvLastSMS = (TextView) convertView.findViewById(R.id.tvLastSMS);
            final TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            final ImageView imReaded = (ImageView) convertView.findViewById(R.id.imReaded);
            final Conversation con = arr.get(position);
            tvTen.setText(con.selectNames());
            if ((size = con.getListMes().size() - 1) > 0)
                tvLastSMS.setText(con.getListMes().get(size).getText());
            else
                tvLastSMS.setText("...");
            if (size > 0)
                tvDate.setText(con.getListMes().get(size).getDate().toString());
            else
                tvDate.setText("...");
            if(con.isReaded()){
                imReaded.setImageBitmap(null);
            }else{
                imReaded.setImageResource(R.drawable.a1);
            }
        }
        return convertView;
    }
}
