package com.trangpig.myapp.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhuocquy.model.Account;
import com.nhuocquy.model.MessageChat;
import com.trangpig.data.Data;
import com.trangpig.myapp.activity.ConversationChat;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyConstant;
import com.trangpig.until.Utils;

import java.io.IOException;

public class BroadcastMessage extends BroadcastReceiver {
    Message mesHandler;
    String mesBroadCast;
    Account account;
    ObjectMapper objectMapper;
    MessageChat receiveMes;
    Context context;
    Intent intentBroadcast = new Intent();

    public BroadcastMessage() {
        objectMapper = new ObjectMapper();
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
    }

    Handler handlerRecive = handlerRecive = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mesBroadCast = msg.obj.toString();
            try {
                Log.e("tuyet....handler", mesBroadCast);
                receiveMes = objectMapper.readValue(mesBroadCast, MessageChat.class);
                if ((long) Data.getInstance().getAttribute(Data.ID_CON) == receiveMes.getIdConversation()) {
                    intentBroadcast.setAction(MyService.ACTION_CONVERSATION_CHAT);
                    intentBroadcast.putExtra(MyService.MES, mesBroadCast);
                    context.sendBroadcast(intentBroadcast);
                } else if (!receiveMes.getText().contains(ConversationChat.MES_HINT_ON) && !receiveMes.getText().contains(ConversationChat.MES_HINT_OFF)) {
                    Log.e("tuyet....Broadcast", receiveMes.getText() + ":" + receiveMes.getIdConversation() + ":" + account.getConversations());
                    for (int i = 0; i < account.getConversations().size(); i++) {
                        if (receiveMes.getIdConversation() == account.getConversations().get(i).getIdCon()) {
                            account.getConversations().get(i).setReaded(false);
                            account.getConversations().get(i).addMessageChat(receiveMes);
                            Utils.notification( context, receiveMes);
                            Log.e("tuyet....Broadcast", receiveMes.getText());
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    ;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mesHandler = handlerRecive.obtainMessage();
        mesHandler.obj = intent.getStringExtra(MyService.MES);
        handlerRecive.sendMessage(mesHandler);
    }
}
