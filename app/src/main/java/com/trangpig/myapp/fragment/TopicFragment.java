package com.trangpig.myapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import com.trangpig.myapp.activity.ChatRoomActivity;
import com.trangpig.myapp.activity.TopicActivity;
import com.trangpig.myapp.adapter.ChatRoomAdapter;
import com.trangpig.myapp.adapter.GroupTopicAdapter;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyConstant;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TrangPig on 06/05/2015.
 */
public class TopicFragment extends Fragment {
public static final String GROUP_TOPIC_LIST = "groupTopicList";
public static final String GROUP_TOPIC = "groupTopic";
    private RestTemplate restTemplate;
    List<GroupTopic> groupTopic;
    List<Topic> topicList;
    Topic topic ;
    List<Post> postList;
    Post post;
    Intent intentTopic;
    ListView listViewGroupTopic;
    GroupTopicAdapter groupTopicAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                groupTopic = Arrays.asList(restTemplate.getForObject(String.format(MyUri.URL_GET_ALL_GROUPTOPIC,MyUri.IP),GroupTopic[].class));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                groupTopicAdapter = new GroupTopicAdapter(getActivity(),groupTopic);
                listViewGroupTopic.setAdapter(groupTopicAdapter);
            }
        }.execute();

//
//        groupTopic = new GroupTopic[3];
//
//
//        // tao du lieu cho group topic--------------------------------
//        for(int i=0;i<3;i++){
//            groupTopic[i] = new GroupTopic();
//            groupTopic[i].setGroupName("Group Topic "+i);
//            topicList = new ArrayList<>();
//            for (int j = 0; j<10;j++){
//                topic = new Topic();
//                topic.setIdTopic(j);
//                topic.setTitle("Topic "+j);
//                postList = new ArrayList<>();
//                for(int k=0; k<10;k++){
//                    post = new Post();
//                    post.setIdPost(k);
//                    post.setContext("context post \n \n \n \n \n \n"+ k);
//                    postList.add(post);
//                }
//                topic.setListPosts(postList);
//                topicList.add(topic);
//            }
//            groupTopic[i].setLisTopics(topicList);
//        }
//        Data.getInstance().setAttribute(GROUP_TOPIC_LIST, groupTopic);
        //----------------------------------------------------------


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_topic, container, false);
        listViewGroupTopic = (ListView) v.findViewById(R.id.lv_group_topic);

        listViewGroupTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentTopic = new Intent(getActivity(), TopicActivity.class);
                intentTopic.putExtra(TopicActivity.GROUP_TOPIC_ID, groupTopic.get(position).getIdGroupTopic());
                startActivity(intentTopic);
            }
        });

        return v;
    }



}
