package com.trangpig.until;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
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
}
