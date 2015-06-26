package com.trangpig.myapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.LockUpAdapter;
import com.trangpig.myapp.fragment.SearchInforFragment;
import com.trangpig.myapp.fragment.SearchScoreFragment;

import java.util.ArrayList;
import java.util.List;

public class LockUpActivity extends FragmentActivity {

    LockUpAdapter lockUpAdapter;

    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_up);
        fragmentList = new ArrayList<>();
        fragmentList.add(new SearchScoreFragment());
        fragmentList.add(new SearchInforFragment());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_lock_up);
        lockUpAdapter = new LockUpAdapter(getSupportFragmentManager(),fragmentList,this);
        viewPager.setAdapter(lockUpAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lock_up, menu);
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
