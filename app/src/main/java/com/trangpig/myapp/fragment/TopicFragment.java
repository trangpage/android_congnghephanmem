package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhuocquy.model.GroupTopic;
import com.nhuocquy.model.Post;
import com.nhuocquy.model.Topic;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.TopicActivity;
import com.trangpig.myapp.adapter.ChatRoomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrangPig on 06/05/2015.
 */
public class TopicFragment extends Fragment {
public static final String GROUP_TOPIC = "groupTopic";
    GroupTopic[] groupTopic;
    List<Topic> topicList;
    Topic topic ;
    List<Post> postList;
    Post post;
    Intent intentTopic;
    ListView listViewGroupTopic;
    ChatRoomAdapter chatRoomAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatRoomAdapter = new ChatRoomAdapter(getActivity());
        groupTopic = new GroupTopic[3];
        // tao du lieu cho group topic--------------------------------
        for(int i=0;i<3;i++){
            groupTopic[i] = new GroupTopic();
            groupTopic[i].setGroupName("Group Topic "+i);
            topicList = new ArrayList<>();
            for (int j = 0; j<10;j++){
                topic = new Topic();
                topic.setIdTopic(j);
                postList = new ArrayList<>();
                for(int k=0; k<10;k++){
                    post = new Post();
                    post.setContext("context post"+ k);
                    postList.add(post);
                }
                topic.setListPosts(postList);
                topicList.add(topic);
            }
            groupTopic[i].setLisTopics(topicList);
            Data.getInstance().setAttribute(GROUP_TOPIC+i,groupTopic[i]);
        }
        //----------------------------------------------------------

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_topic, container, false);
        listViewGroupTopic = (ListView) v.findViewById(R.id.lv_group_topic);
        listViewGroupTopic.setAdapter(chatRoomAdapter);
        listViewGroupTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentTopic = new Intent(getActivity(), TopicActivity.class);
                intentTopic.putExtra(TopicActivity.GROUP_TOPIC_ID,position);
                startActivity(intentTopic);
            }
        });

        return v;
    }

}
