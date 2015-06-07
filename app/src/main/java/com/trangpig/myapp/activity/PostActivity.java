package com.trangpig.myapp.activity;

import android.app.Activity;
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

import com.nhuocquy.model.Topic;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListTopicAdapter;
import com.trangpig.myapp.adapter.PostAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class PostActivity extends Activity {
    static final int PUBLIC_POST = 1;
    private RecyclerView recyclerViewTopic;
    private Button buttonDang;
    private Intent intentDang;
    private PostAdapter postAdapter;
    private RestTemplate restTemplate;
    Topic topic;
    long idTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port);

        idTopic = getIntent().getLongExtra(ListTopicAdapter.ID_TOPIC,0);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        asyncTask.execute(idTopic);


        buttonDang = (Button) findViewById(R.id.btnSendTopic);
        recyclerViewTopic = (RecyclerView) findViewById(R.id.recycler_view_topic);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopic.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(listMessageChat.size());
        recyclerViewTopic.setItemAnimator(new DefaultItemAnimator());

        buttonDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            intentDang = new Intent(getApplicationContext(),PublicPost.class);
                intentDang.putExtra(PublicPost.ID, topic.getIdTopic());
                intentDang.putExtra(PublicPost.POST_TOPIC, PublicPost.POST);
                startActivityForResult(intentDang, PUBLIC_POST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_port, menu);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PUBLIC_POST:
                if (resultCode == PublicPost.SUCCESS) {
                    asyncTask.execute(idTopic);
                }
                break;
            default:
                break;
        }
    }
   AsyncTask<Long,Void,Void> asyncTask = new AsyncTask<Long,Void,Void>(){
        @Override
        protected Void doInBackground(Long... params) {
            topic = restTemplate.getForObject(String.format(MyUri.URL_GET_TOPIC,MyUri.IP,params[0]),Topic.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            postAdapter = new PostAdapter(PostActivity.this,topic);
            recyclerViewTopic.setAdapter(postAdapter);
            setTitle(topic.getTitle());
        }
    };
}
