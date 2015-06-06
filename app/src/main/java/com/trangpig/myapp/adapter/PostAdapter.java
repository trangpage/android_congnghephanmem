package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.GroupTopic;
import com.nhuocquy.model.Post;
import com.nhuocquy.model.Topic;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ImageDetailActivity;
import com.trangpig.myapp.fragment.TopicFragment;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by TrangPig on 05/23/2015.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Activity context;
    List<Post> postList ;
    Post post;
    PostImageOfAdapter postImageOfAdapter;
    int cnt;

    SimpleDateFormat simpleDateFormat;
    public PostAdapter(Activity context,Topic topic) {
        this.context = context;
        this.postList = topic.getListPosts();
        simpleDateFormat = new SimpleDateFormat("hh:MM");
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post_recyclerview, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        post = postList.get(position);
        holder.postName.setText(post.getPoster().getName());
        holder.postDate.setText(simpleDateFormat.format(post.getDatePost()));
        holder.topic.setText(post.getContext());
        postImageOfAdapter = new PostImageOfAdapter(context,post.getImages());
        cnt = post.getImages().size() <= 3?post.getImages().size():post.getImages().size()/2;
        holder.gridViewImage.setNumColumns(cnt);
        holder.gridViewImage.setAdapter(postImageOfAdapter);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postName;
        TextView postDate;
        TextView topic;
        GridView gridViewImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            postName = (TextView) itemView.findViewById(R.id.tv_new_port_user);
            postDate = (TextView) itemView.findViewById(R.id.tv_new_port_date);
            topic = (TextView) itemView.findViewById(R.id.tv_new_port_text);
            gridViewImage = (GridView) itemView.findViewById(R.id.gridView_new_post);
        }
    }


}
