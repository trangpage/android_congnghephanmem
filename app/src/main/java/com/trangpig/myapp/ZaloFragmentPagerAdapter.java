package com.trangpig.myapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;


/**
 * Created by TrangPig on 04/06/2015.
 */
public class ZaloFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    Context context;
 public ZaloFragmentPagerAdapter(FragmentManager fm, Context context){
     super(fm);
     this.context = context;
 }

    @Override
    public Fragment getItem(int i) {
        ZaloFragment zfm = new ZaloFragment();
        Bundle data = new Bundle();
        data.putInt("currentpage", i++);
        zfm.setArguments(data);
        return zfm;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb = new SpannableStringBuilder(""); // space added before text for convenience

        Drawable drawable = null ;
        if(position == 0){
             drawable= context.getResources().getDrawable(R.drawable.hinh1);
        }else{
             drawable= context.getResources().getDrawable(R.drawable.hinh2);
        }
        drawable.setBounds(0, 0, 50, 50);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

}

