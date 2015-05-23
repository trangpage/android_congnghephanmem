package com.trangpig.myapp.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.nhuocquy.model.Friend;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.MainActivity;
import com.trangpig.myapp.service.MyService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class BroadcastNotifyAddFriend extends BroadcastReceiver {
    ObjectMapper objectMapper ;
    public BroadcastNotifyAddFriend() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mes = intent.getStringExtra(MyService.MES);
        Friend friend = null;

        try {
            friend = objectMapper.readValue(mes,Friend.class);
            if(friend == null)
                return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intentBroadcast = new Intent(context,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle("Yeu cau ket ban")
                .setContentText("")
                .setSmallIcon(R.drawable.download)
                .setContentIntent(pIntent).build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
        // play beep
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context,
                notification);
        r.play();
    }
    public void playBeep() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
