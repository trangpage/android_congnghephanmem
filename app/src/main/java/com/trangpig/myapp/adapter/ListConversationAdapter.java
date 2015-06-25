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

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by TrangPig on 04/18/2015.
 */
public class ListConversationAdapter extends ArrayAdapter<Conversation> {
    Activity contex;
    ImageView imReaded;
    TextView tvTen, tvLastSMS, tvDate;
    Conversation con;
    Account account;
    List<Conversation> arr = null;
    int id;
    int size;
    LruCache mMemoryCache;
    RestTemplate restTemplate;
    SimpleDateFormat f;

    public ListConversationAdapter(Activity context, List<Conversation> arr, int id) {
        super(context, id, arr);
        this.contex = context;
        this.arr = arr;
        this.id = id;
        f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
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

        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = contex.getLayoutInflater();
            convertView = layoutInflater.inflate(id, null);
        }
        if (arr.size() > 0 && position >= 0) {
             tvTen = (TextView) convertView.findViewById(R.id.tvTen);
             tvLastSMS = (TextView) convertView.findViewById(R.id.tvLastSMS);
             tvDate = (TextView) convertView.findViewById(R.id.tvDate);
             imReaded = (ImageView) convertView.findViewById(R.id.imReaded);
             con = arr.get(position);

            tvTen.setText(con.selectNames());
            if ((size = con.getListMes().size() - 1) > 0)
                tvLastSMS.setText(con.getListMes().get(size).getText());
            else
                tvLastSMS.setText("...");
            if (size > 0)
                tvDate.setText(f.format(con.getListMes().get(size).getDate()).toString());
            else
                tvDate.setText("...");
            if(con.isReaded()){
                imReaded.setImageBitmap(null);
            }else{
                imReaded.setImageResource(R.drawable.message);
            }

            ///
          final  ImageView avatar = (ImageView) convertView.findViewById(R.id.avatarCon);
             String fileNameAvatar = "";
            for(int i = 0; i< con.getFriends().size(); i++){
                if(account.getIdAcc() == con.getFriends().get(i).getIdFriend())
                    continue;
                else {
                    fileNameAvatar = con.getFriends().get(i).getAvatar();
                    break;
                }
            }
            new AsyncTask<String, Void, Bitmap>() {
                String fileName;
                @Override
                protected Bitmap doInBackground(String... params) {
                    MyFile myFile = null;
                    Bitmap bitmap = getBitMapFromCache(params[0]);
                    if (bitmap == null)
                        try {
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
                        avatar.setImageBitmap(myFile);
                    } else {
                        avatar.setImageResource(R.drawable.left);
                    }

                }
            }.execute(fileNameAvatar);

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
