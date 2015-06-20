package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ImageDetailActivity;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrangPig on 06/06/2015.
 */
public class PostImageOfAdapter extends ArrayAdapter<String> {
    List<String> listImg;
    Activity context;
    RestTemplate restTemplate;
    LruCache mMemoryCache;
    public PostImageOfAdapter(Activity context, List<String> listImg) {
        super(context, R.layout.image_post, listImg);
        this.context = context;
        this.listImg = listImg;

        ///
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
            convertView = layoutInflater.inflate(R.layout.image_post, null);
        }
        if (listImg.size() > 0 && position >= 0) {
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imv_post_image);

            new AsyncTask<String, Void, Bitmap>() {
                String fileName;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                imageView.setImageResource(R.drawable.wait);
                imageView.setOnClickListener(null);
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    MyFile myFile = null;
                    Bitmap bitmap = getBitMapFromCache(params[0]);
                    if (bitmap == null)
                        try {
                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                            if (myFile != null) {
                                bitmap = Utils.decodeSampledBitmapFromResource(myFile.getData(), 500, 500);
                                fileName = myFile.getFileName();
                            }
                            addBitMapToCache(fileName, bitmap);
                        } catch (RestClientException | MyFileException e) {
                            e.printStackTrace();
                        }
                    fileName = params[0];
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap myFile) {
                    super.onPostExecute(myFile);
                    if (myFile != null) {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ImageDetailActivity.class);
                                intent.putExtra(ImageDetailActivity.FILE_NAME, fileName);
                                context.startActivity(intent);
                            }
                        });
                        imageView.setImageBitmap(myFile);
                    } else {
                        imageView.setImageResource(R.drawable.error);
                        Toast.makeText(context, context.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                    }

                }
            }.execute(listImg.get(position));
        }
//            imageView.setImageURI(listImg.get(position));

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
