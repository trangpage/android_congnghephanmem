package com.trangpig.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhuocquy.model.GroupTopic;
import com.nhuocquy.model.Topic;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.PostActivity;
import com.trangpig.myapp.fragment.TopicFragment;

import java.util.List;

/**
 * Created by user on 5/6/2015.
 */
public class ListTopicAdapter extends RecyclerView.Adapter<ListTopicAdapter.TopicViewHolder>{
    GroupTopic groupTopic =(GroupTopic) Data.getInstance().getAttribute(TopicFragment.GROUP_TOPIC);
    List<Topic> topicList = groupTopic.getLisTopics();
    Topic topic;
    Context context;
    Intent intentPost;
    public ListTopicAdapter(Context context){
        this.context = context;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_topic_recyclerview, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPost = new Intent(context, PostActivity.class);
                context.startActivity(intentPost);
            }
        });
        return new TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        topic = topicList.get(position);
        holder.postName.setText(topic.getTitle());
        holder.postDate.setText(position+":00");
        holder.topic.setText(holder.topic.getText()+topic.getListPosts().get(0).getContext());
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView postName;
        TextView postDate;
        TextView topic;

        public TopicViewHolder(View itemView) {
            super(itemView);
            postName = (TextView)itemView.findViewById(R.id.tv_new_topic_user);
            postDate = (TextView)itemView.findViewById(R.id.tv_new_topic_date);
            topic = (TextView)itemView.findViewById(R.id.tv_new_topic_text);
        }
    }


}
