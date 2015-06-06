package com.trangpig.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.IconPagerAdapter;
import com.trangpig.myapp.fragment.SmileBigFragment;
import com.trangpig.myapp.fragment.SmileSmallFragment;
import com.trangpig.myinterface.SmileFragment;
import com.trangpig.until.IconSetup;

import java.util.ArrayList;
import java.util.List;


public class  IconActivity extends FragmentActivity {
    IconPagerAdapter adapterViewPager;
    ViewPager vpPager;
    List<Fragment> list;

    public List<Fragment> getFragments() {
        list = new ArrayList<Fragment>();
        //
        SmileFragment smileFragment = new SmileSmallFragment();
        smileFragment.setListIcon(IconSetup.MAP_ICON_DRABLE);
        smileFragment.setParentActivity(this);
        list.add((Fragment)smileFragment);
        //
        smileFragment = new SmileBigFragment();
        smileFragment.setListIcon(IconSetup.MAP_ICON_RAW1);
        smileFragment.setParentActivity(this);
        list.add((Fragment)smileFragment);
        //
        smileFragment = new SmileBigFragment();
        smileFragment.setListIcon(IconSetup.MAP_ICON_RAW);
        smileFragment.setParentActivity(this);
        list.add((Fragment)smileFragment);
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new IconPagerAdapter(getSupportFragmentManager(), getFragments(), getApplicationContext());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(0);
    }

    public void sendString(int typeIcon,String s) {
        Intent intent = getIntent();
        intent.putExtra(ConversationChat.KEY_ICON_STRING, s);
        setResult(typeIcon, intent);
        finish();
    }


}
