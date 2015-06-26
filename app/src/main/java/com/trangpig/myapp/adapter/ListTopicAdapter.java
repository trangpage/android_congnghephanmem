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
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.PostActivity;

import java.util.List;

/**
 * Created by user on 5/6/2015.
 */
public class ListTopicAdapter extends RecyclerView.Adapter<ListTopicAdapter.TopicViewHolder>{
    public static final String ID_TOPIC = "idTopic";
    GroupTopic groupTopic;
    List<Topic> topicList;
    Topic topic;
    Context context;
    Intent intentPost;
    public ListTopicAdapter(Context context, GroupTopic groupTopic){
        this.context = context;
        this.groupTopic = groupTopic;
        this.topicList = groupTopic.getLisTopics();
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_topic_recyclerview, parent, false);
        return new TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TopicViewHolder holder, int position) {
        topic = topicList.get(position);
        holder.idTopic = topic.getIdTopic();
        holder.postName.setText(topic.getTitle());
//        holder.postDate.setText("#"+position);

        holder.topic.setText("");
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPost = new Intent(context, PostActivity.class);
                intentPost.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentPost.putExtra(ID_TOPIC, holder.idTopic);
                context.startActivity(intentPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView postName;
//        TextView postDate;
        TextView topic;
        View v;
        Long idTopic;

        public TopicViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            postName = (TextView)itemView.findViewById(R.id.tv_new_topic_user);
//            postDate = (TextView)itemView.findViewById(R.id.tv_new_topic_date);
            topic = (TextView)itemView.findViewById(R.id.tv_new_topic_text);
        }
    }


}
