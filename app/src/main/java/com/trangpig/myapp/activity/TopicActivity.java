package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.nhuocquy.model.GroupTopic;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListTopicAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class TopicActivity extends Activity {
    public static final String GROUP_TOPIC_ID = "group_topic_name";
    static final int PUBLIC_TOPIC = 1;

    private RestTemplate restTemplate;
    private ListTopicAdapter listTopicAdapter;
    private RecyclerView recyclerViewTopic;
    private Button buttonDang;
    private Intent intentDang;
    private GroupTopic groupTopic;
    AsyncTaskLoadGroupTopic asyncTaskLoadGroupTopic;
    long idGroupTopic;
    ProgressDialog ringProgressDialog;
    ImageButton bntTop, bntBottom;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
         idGroupTopic = getIntent().getLongExtra(GROUP_TOPIC_ID, 0);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        asyncTaskLoadGroupTopic = new AsyncTaskLoadGroupTopic();
        asyncTaskLoadGroupTopic.execute(idGroupTopic);

        recyclerViewTopic = (RecyclerView) findViewById(R.id.recycler_view_topic);
        buttonDang = (Button) findViewById(R.id.btnSendTopic);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopic.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(listMessageChat.size());
        recyclerViewTopic.setItemAnimator(new DefaultItemAnimator());

                buttonDang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentDang = new Intent(getApplicationContext(), PublicPost.class);
                        intentDang.putExtra(PublicPost.ID, groupTopic.getIdGroupTopic());
                        intentDang.putExtra(PublicPost.POST_TOPIC, PublicPost.TOPIC);
                        startActivityForResult(intentDang, PUBLIC_TOPIC);

                    }
                });

        bntTop = (ImageButton) findViewById(R.id.bntTop);
        bntTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            linearLayoutManager.scrollToPosition(0);
            }
        });
        bntBottom = (ImageButton) findViewById(R.id.bntBottom);
        bntBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            linearLayoutManager.scrollToPosition(groupTopic.getLisTopics().size()-1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PUBLIC_TOPIC:
                if (resultCode == PublicPost.SUCCESS) {
                    asyncTaskLoadGroupTopic = new AsyncTaskLoadGroupTopic();
                    asyncTaskLoadGroupTopic.execute(idGroupTopic);
                }
                break;
            default:
                break;
        }
    }
    class AsyncTaskLoadGroupTopic extends AsyncTask<Long, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        ringProgressDialog = ProgressDialog.show(TopicActivity.this, TopicActivity.this.getResources().getString(R.string.wait), TopicActivity.this.getResources().getString(R.string.conecting), true);
        }

        @Override
        protected Void doInBackground(Long... params) {
            groupTopic = restTemplate.getForObject(String.format(MyUri.URL_GET_GROUPTOPIC, MyUri.IP, params[0]), GroupTopic.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listTopicAdapter = new ListTopicAdapter(getApplicationContext(),groupTopic);
            recyclerViewTopic.setAdapter(listTopicAdapter);
            setTitle(groupTopic.getGroupName());
            ringProgressDialog.dismiss();
        }
    };

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
