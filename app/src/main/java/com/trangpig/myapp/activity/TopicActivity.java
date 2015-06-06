package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nhuocquy.model.GroupTopic;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.GroupTopicAdapter;
import com.trangpig.myapp.adapter.ListTopicAdapter;
import com.trangpig.myapp.adapter.MessagesListAdapter;
import com.trangpig.myapp.fragment.TopicFragment;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class TopicActivity extends Activity {
    public static final String GROUP_TOPIC_ID = "group_topic_name";
    private RestTemplate restTemplate;
    private ListTopicAdapter listTopicAdapter;
    private RecyclerView recyclerViewTopic;
    private Button buttonDang;
    private Intent intentDang;
    private GroupTopic groupTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        long pos = getIntent().getLongExtra(GROUP_TOPIC_ID, 0);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        new AsyncTask<Long,Void,Void>(){
            @Override
            protected Void doInBackground(Long... params) {
                groupTopic = restTemplate.getForObject(String.format(MyUri.URL_GET_GROUPTOPIC,MyUri.IP,params[0]),GroupTopic.class);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listTopicAdapter = new ListTopicAdapter(getApplicationContext(),groupTopic);
                recyclerViewTopic.setAdapter(listTopicAdapter);
                setTitle(groupTopic.getGroupName());

            }
        }.execute(pos);

//        groupTopic = (GroupTopic[])Data.getInstance().getAttribute(TopicFragment.GROUP_TOPIC_LIST);
//        Data.getInstance().setAttribute(TopicFragment.GROUP_TOPIC,groupTopic[pos]);

        recyclerViewTopic = (RecyclerView) findViewById(R.id.recycler_view_topic);
        buttonDang = (Button) findViewById(R.id.btnSendTopic);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopic.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(listMessageChat.size());
        recyclerViewTopic.setItemAnimator(new DefaultItemAnimator());

                buttonDang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentDang = new Intent(getApplicationContext(),PublicPost.class);
                        startActivity(intentDang);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
