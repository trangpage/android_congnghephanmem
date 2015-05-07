package com.trangpig.myapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by user on 4/6/2015.
 */
public class IconPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    Context context;
    int[] imageResId = {R.drawable.icon_smile, R.drawable.icon_smile,R.drawable.icon_smile};

    public IconPagerAdapter(android.support.v4.app.FragmentManager fragmentManager, List<Fragment> fragments, Context context) {
        super(fragmentManager);
        this.fragments = fragments;
        this.context = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }
    // lấy page thứ position
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
     return fragments.get(position);
    }

    // trả về một vị trí trang ở trên đầu....
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
