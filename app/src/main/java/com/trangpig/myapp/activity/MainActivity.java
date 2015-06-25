package com.trangpig.myapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nhuocquy.model.Account;

import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListviewMenuLeftAdapter;
import com.trangpig.myapp.adapter.MyPagerAdapter;
import com.trangpig.myapp.fragment.ChatRoomFragment;
import com.trangpig.myapp.fragment.ListAddFriendFragment;
import com.trangpig.myapp.fragment.ListConversationFragment;
import com.trangpig.myapp.fragment.ListFriendFragment;
import com.trangpig.myapp.fragment.TopicFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity
{
    private Account account;
    private SharedPreferences sharedPreferences;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> fragmentList;
    private ListView mDrawerList;
    private String[] mPlanetTitles = {"A","B","C","D"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.accountXML), Context.MODE_PRIVATE);
        account = (Account) Data.getInstance().getAttribute(getString(R.string.account));
        long id = -1;
        if(account==null){
             id = sharedPreferences.getLong(getString(R.string.accountid), -1);
            if(id != -1){
            }else{
                Intent intent = new Intent(this, Logo.class);
                startActivity(intent);
            }
        }else{
            //chuan bi cho fragment
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopicFragment());
            fragmentList.add(new ListFriendFragment());
            fragmentList.add(new ListConversationFragment());
            fragmentList.add(new ListAddFriendFragment());
            fragmentList.add(new ChatRoomFragment());
            myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fragmentList,this);
            viewPager.setAdapter(myPagerAdapter);
            viewPager.setCurrentItem(0);

            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            mDrawerList.setAdapter(new ListviewMenuLeftAdapter(this,mPlanetTitles));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
