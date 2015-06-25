package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Friend;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Created by TrangPig on 04/06/2015.
 */
public class ListFriendAdapter extends ArrayAdapter<Friend> {

    Activity context;
    List<Friend> listFriend;
    int cur;
    LruCache mMemoryCache;
    RestTemplate restTemplate;

    public ListFriendAdapter(Activity context, List<Friend> listFriend, int cur) {
        super(context, cur, listFriend);
        this.context = context;
        this.listFriend = listFriend;
        this.cur = cur;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
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
            new AsyncTask<String, Void, Bitmap>() {
                String fileName;
                @Override
                protected Bitmap doInBackground(String... params) {
                    MyFile myFile = null;
                    Bitmap bitmap = Utils.getBitMapFromCache(params[0], context);
                    if (bitmap == null)
                        try {
                            Log.e("tuyet....server", params[0]);
                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                            if (myFile != null) {
                                bitmap = Utils.decodeSampledBitmapFromResource(myFile.getData(), 450, 450);
                                fileName = myFile.getFileName();
                            }
                            addBitMapToCache(fileName, bitmap);
                        } catch (RestClientException | MyFileException e) {
                            e.printStackTrace();
                        }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap myFile) {
                    super.onPostExecute(myFile);
                    if (myFile != null) {
                        imageView.setImageBitmap(myFile);
                    } else {
                        imageView.setImageResource(R.drawable.left);
                        Toast.makeText(context, context.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                    }

                }
            }.execute(con.getAvatar());

        }
        return convertView;
    }

    private Bitmap getBitMapFromCache(String fileName) {
        if (mMemoryCache != null) {
            return (Bitmap) mMemoryCache.get(fileName);
        }
        return null;
    }

    private void addBitMapToCache(String fileName, Bitmap value) {
        if (mMemoryCache != null) {
            mMemoryCache.put(fileName, value);
        }
    }
}

