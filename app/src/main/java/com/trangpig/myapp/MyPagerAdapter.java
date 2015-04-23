package com.trangpig.myapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.List;

/**
 * Created by TrangPig on 04/21/2015.
 */
public class MyPagerAdapter extends FragmentPagerAdapter   {

    List<Fragment> lisstFragment;
    Context context;
    int[] imageResId = {R.drawable.right, R.drawable.left};

    public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> listFrg, Context context) {
        super(fragmentManager);
        this.lisstFragment = listFrg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lisstFragment.size();
    }

    @Override
    public Fragment getItem(int position) {
        return lisstFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, 50,50);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}


