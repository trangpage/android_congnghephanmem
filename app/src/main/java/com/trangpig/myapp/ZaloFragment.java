package com.trangpig.myapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by TrangPig on 04/06/2015.
 */
public class ZaloFragment extends Fragment {

    int mCurentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        mCurentPage = data.getInt("currentpage",0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        if(mCurentPage==0){
            v = inflater.inflate(R.layout.logo, container,false);
        }else{
            v = inflater.inflate(R.layout.login, container,false);
        }


//        TextView tv = (TextView ) v.findViewById(R.id.tv);
//        tv.setText("You are viewing the page #" + mCurentPage + "\n\n" + "Swipe Horizontally left / right");
        return v;
    }
}
