package com.trangpig.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.trangpig.myapp.R;

import java.util.ArrayList;

/**
 * Created by TrangPig on 05/23/2015.
 */
public class CommentAdapter extends ArrayAdapter<Object>{
    TextView tvCommentName, tvCommentText;

    public CommentAdapter(Context context) {
        super(context, R.layout.list_item_new_comment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        tvCommentName = (TextView) convertView.findViewById(R.id.tv_comment_new_name);
        return null;
    }
}
