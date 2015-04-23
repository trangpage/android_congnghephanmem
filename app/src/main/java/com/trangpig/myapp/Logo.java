package com.trangpig.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

/**
 * Created by TrangPig on 04/19/2015.
 */
public class Logo extends FragmentActivity{

    Button btnDangNhap, btnDangKy ;
    //    View viewDangNhap;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        FragmentManager fm = getSupportFragmentManager();
//        FriendAdapter zaloAdapter = new FriendAdapter(fm,getApplicationContext());
//        pager.setAdapter(zaloAdapter);

        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Logo.this,
                        Login.class);
                startActivity(intent);
            }
        });



    }
}
