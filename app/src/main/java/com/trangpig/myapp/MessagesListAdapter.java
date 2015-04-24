package com.trangpig.myapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.MessageChat;
import com.trangpig.data.Data;

import java.util.ArrayList;
import java.util.List;

//import com.nhuocquy.model.Message;

/**
 * Created by TrangPig on 04/15/2015.
 */
public class MessagesListAdapter
        extends BaseAdapter {

    private Context context;
    private List<MessageChat> messagesItems;
    Account account;
    MessageChat m;
    LayoutInflater mInflater;
    TextView lblFrom,txtMsg;


    public MessagesListAdapter(Context context, List<MessageChat> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
//        this.messagesItems = new ArrayList<>();
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */

         mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        m = messagesItems.get(position);
            if (m.getIdSender() == account.getIdAcc()) {
                convertView = mInflater.inflate(R.layout.list_item_message_right,
                        null);
            } else {
                convertView = mInflater.inflate(R.layout.list_item_message_left,
                        null);
        }

         lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
         txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        txtMsg.setText(m.getText());
        lblFrom.setText(m.getfromName());

        return convertView;
    }
    public void setListMes (List<MessageChat> list){
        this.messagesItems = list;
    }
}

