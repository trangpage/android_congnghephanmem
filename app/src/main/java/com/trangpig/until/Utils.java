package com.trangpig.until;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.nhuocquy.myfile.MyFile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TrangPig on 06/04/2015.
 */
public class Utils {
    public static MyFile getMyFileFromUri(Uri uri, Context context) {
        String TAG = "nhuoc..quy....";
        MyFile myFile = new MyFile();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String fileName = getPath(uri,context);
            myFile = new MyFile(fileName);
        } else {
            Cursor cursor = context.getContentResolver()
                    .query(uri, null, null, null, null, null);
            DataInputStream dis = null;
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.i(TAG, "Display Name: " + displayName);
                    int size = 0;
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (!cursor.isNull(sizeIndex)) {
                        // Technically the column stores an int, but cursor.getString()
                        // will do the conversion automatically.
                        size = cursor.getInt(sizeIndex);
                    }
                    Log.i(TAG, "Size: " + size);
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    dis = new DataInputStream(inputStream);
                    byte[] data = new byte[size];
                    dis.readFully(data);
                    myFile.setFileName(displayName);
                    myFile.setData(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                if (dis != null)
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return myFile;
    }

    public static String getPath(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        try {
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        Log.i("nhuocquy..quy....", path);

        return path;
    }

    public static void playBeep(Context context) {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context,
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
