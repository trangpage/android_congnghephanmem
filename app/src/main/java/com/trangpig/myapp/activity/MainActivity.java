package com.trangpig.myapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    //
    private Account account;
    private SharedPreferences sharedPreferences;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> fragmentList;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private String[] mPlanetTitles;
    private Intent intent;

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
                finish();
            }
        }else{
            //chuan bi cho fragment
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            fragmentList = new ArrayList<>();
            fragmentList.add(new TopicFragment());
            fragmentList.add(new ListFriendFragment());
            fragmentList.add(new ListConversationFragment());
            fragmentList.add(new ListAddFriendFragment());
            fragmentList.add(new ChatRoomFragment());
            myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),fragmentList,this);
            viewPager.setAdapter(myPagerAdapter);
            viewPager.setCurrentItem(0);

            mPlanetTitles = getResources().getStringArray(R.array.string_array_planets);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            mDrawerList.setAdapter(new ListviewMenuLeftAdapter(this, mPlanetTitles));
            mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
            //
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0) {
                        intent = new Intent(MainActivity.this,PersonalActivity.class);
                        startActivity(intent);
//                        setContentView(R.layout.activity_personal);
                    }
                    else if(position==6 || position==7) {
//                        setContentView(R.layout.login);
                        intent = new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else viewPager.setCurrentItem(position-1, true);
                    // Highlight the selected item, update the title, and close the drawer
                    mDrawerList.setItemChecked(position, true);
//                    setTitle(mPlanetTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            });
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
