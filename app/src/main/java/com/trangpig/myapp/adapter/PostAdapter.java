package com.trangpig.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Intent;

import com.nhuocquy.model.GroupTopic;
import com.nhuocquy.model.Post;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.PostActivity;
import com.trangpig.myapp.fragment.TopicFragment;

import java.util.List;

/**
 * Created by TrangPig on 05/23/2015.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.TopicViewHolder> {
    Context context;
    List<Post> postList= ((GroupTopic)Data.getInstance().getAttribute(TopicFragment.GROUP_TOPIC)).getLisTopics().get(0).getListPosts();
    Post post;
    public PostAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post_recyclerview, parent, false);
        return new TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        post = postList.get(position);
        holder.postName.setText(post.getIdPost()+"");
        holder.postDate.setText(position+":00");
        holder.topic.setText(holder.topic.getText()+post.getContext());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView postName;
        TextView postDate;
        TextView topic;

        public TopicViewHolder(View itemView) {
            super(itemView);
            postName = (TextView) itemView.findViewById(R.id.tv_new_port_user);
            postDate = (TextView) itemView.findViewById(R.id.tv_new_port_date);
            topic = (TextView) itemView.findViewById(R.id.tv_new_port_text);
        }
    }


}
