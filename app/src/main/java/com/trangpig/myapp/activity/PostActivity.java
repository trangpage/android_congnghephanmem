package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.PostAdapter;

public class PostActivity extends Activity {
    private RecyclerView recyclerViewTopic;
    private Button buttonDang;
    private Intent intentDang;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port);

        buttonDang = (Button) findViewById(R.id.btnSendTopic);
        recyclerViewTopic = (RecyclerView) findViewById(R.id.recycler_view_topic);
        postAdapter = new PostAdapter(this);
        recyclerViewTopic.setAdapter(postAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopic.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.scrollToPosition(listMessageChat.size());
        recyclerViewTopic.setItemAnimator(new DefaultItemAnimator());

        buttonDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
