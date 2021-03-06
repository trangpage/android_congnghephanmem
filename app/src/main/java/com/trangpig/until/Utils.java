package com.trangpig.until;

import android.app.ActivityManager;import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.LruCache;

import com.nhuocquy.model.MessageChat;
import com.nhuocquy.myfile.MyFile;
import com.trangpig.data.Data;import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ConversationChat;
import com.trangpig.myapp.fragment.ListConversationFragment;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TrangPig on 06/04/2015.
 */
public class Utils {
   static LruCache mMemoryCache ;

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

    public static Bitmap decodeSampledBitmapFromResource(byte[] data, int reqWidth, int reqHeight) {

        // BEGIN_INCLUDE (read_bitmap_dimensions)
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // END_INCLUDE (read_bitmap_dimensions)

        // If we're running on Honeycomb or newer, try to use inBitmap

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap b =BitmapFactory.decodeByteArray(data, 0, data.length, options);
//        b = Bitmap.createScaledBitmap(b,reqWidth,reqHeight,true);
        return b;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }
    public static String getSpace(int len) {
        StringBuilder sb = new StringBuilder();
        while (--len < 0) {
            sb.append("");
        }
        return sb.toString();
    }
public static Bitmap getBitMapFromCache(String fileName, Context context) {
        if (mMemoryCache != null) {
            return (Bitmap) mMemoryCache.get(fileName);
        }else {
            setmMemoryCache(context);
        }
        return null;
    }

    public static void addBitMapToCache(String fileName, Bitmap value) {
        if (mMemoryCache != null) {
            mMemoryCache.put(fileName, value);
        }
    }

    public static void setmMemoryCache(Context context){
        //
        if (Build.VERSION.SDK_INT >= 12) {
            mMemoryCache = (LruCache) Data.getInstance().getAttribute(Data.IMAGE_CACHE);
            if (mMemoryCache == null) {
                int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
                int cacheSize = 1024 * 1024 * memClass / 8;
                mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                    protected int sizeOf(String key, Bitmap bitmap) {
                        return bitmap.getByteCount();
                    }
                };
                Data.getInstance().setAttribute(Data.IMAGE_CACHE, mMemoryCache);
            }
        }
    }public static void notification(Context activity,MessageChat mes) {
        Intent intent = new Intent(activity, ConversationChat.class);
        intent.putExtra(ListConversationFragment.ID_CON, mes.getIdConversation());

        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        Notification noti = new NotificationCompat.Builder(activity)
                .setContentTitle(activity.getResources().getString(R.string.new_message) + mes.getFromName())
                .setContentText(mes.getText())
                .setSmallIcon(R.drawable.message)
                .setContentIntent(pIntent).build();
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
        Utils.playBeep(activity);
    }}
