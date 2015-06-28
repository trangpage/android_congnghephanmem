package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.nhuocquy.model.GroupTopic;
import com.nhuocquy.model.Post;
import com.nhuocquy.model.Topic;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.TopicActivity;
import com.trangpig.myapp.adapter.GroupTopicAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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
                groupTopicAdapter.notifyDataSetChanged();
            }
        }.execute();

        return v;
    }



}
