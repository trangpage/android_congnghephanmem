package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
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
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.web.client.RestClientException;

/**
 * Created by NhuocQuy on 6/25/2015.
 */
public class ListviewMenuLeftAdapter extends ArrayAdapter<String> {
    String[] arr;
    Activity context;
    int[] arrImage;
    public ListviewMenuLeftAdapter(Activity context, String[] arr) {
        super(context, R.layout.my_item_layout_friend);
        this.arr = arr;
        this.context = context;
        arrImage = new int[8];
        arrImage[0] = R.drawable.menu_per;
        arrImage[1] = R.drawable.menu_topic;
        arrImage[2] = R.drawable.menu_friend;
        arrImage[3] = R.drawable.menu_message;
        arrImage[4] = R.drawable.menu_add_friend;
        arrImage[5] = R.drawable.menu_muti_chat;
        arrImage[6] = R.drawable.menu_logout;
        arrImage[7] = R.drawable.menu_help;
    }

    @Override
    public int getCount() {
        return arr.length;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.my_item_layout_friend, null);
        }
        if (arr.length > 0 && position >= 0) {
            final TextView tvTen = (TextView) convertView.findViewById(R.id.tvFr);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imgFr);
            tvTen.setText(arr[position]);
            imageView.setImageResource(arrImage[position]);
        }
        return convertView;
    }
}
