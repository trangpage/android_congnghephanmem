package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trangpig.myapp.R;

/**
 * Created by TrangPig on 04/19/2015.
 */
public class Logo extends Activity {

    Button btnDangNhap, btnDangKy ;
    Intent intent;
    TextView textViewLinkSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        textViewLinkSearch = (TextView) findViewById(R.id.tv_search);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Logo.this,
                        Login.class);
                startActivity(intent);
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Logo.this,SignedUpActivity.class);
                startActivity(intent);
            }
        });

        textViewLinkSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Logo.this,LockUpActivity.class);
                startActivity(intent);
            }
        });

    }
}
