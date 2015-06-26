package com.trangpig.myapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by TrangPig on 06/26/2015.
 */
public class LockUpAdapter extends FragmentPagerAdapter {
    List<Fragment> lisstFragment;
    Context context;
    int[] imageResId = {R.drawable.icon_hinhb, R.drawable.hinh1};

    public LockUpAdapter(FragmentManager fm, List<Fragment> listFrg, Context context) {
        super(fm);
        this.lisstFragment = listFrg;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return lisstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lisstFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, 60,50);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
