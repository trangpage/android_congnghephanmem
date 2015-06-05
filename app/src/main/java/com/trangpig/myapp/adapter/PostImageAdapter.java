package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by user on 6/5/2015.
 */
public class PostImageAdapter extends ArrayAdapter<Uri>{
    Activity context;
    List<Uri> listImg;
    int idPost;

    public PostImageAdapter(Activity context, List<Uri> listImg, int idPost) {
        super(context, idPost, listImg);
        this.context = context;
        this.listImg = listImg;
        this.idPost = idPost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(idPost, null);
        }
        if (listImg.size() > 0 && position >= 0) {
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imv);
            imageView.setImageURI(listImg.get(position));
        }
        return convertView;
    }
}
