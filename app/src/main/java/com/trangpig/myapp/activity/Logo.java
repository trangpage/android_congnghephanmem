package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.trangpig.myapp.R;

/**
 * Created by TrangPig on 04/19/2015.
 */
public class Logo extends Activity {

    Button btnDangNhap, btnDangKy ;
    Intent intent;
    ShimmerTextView textViewLinkSearch;
    Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        textViewLinkSearch = (ShimmerTextView) findViewById(R.id.tv_search);
//        shimmer = new Shimmer();
//        shimmer.setRepeatCount(0)
//                .setDuration(500)
//                .setStartDelay(300)
//                .setDirection(Shimmer.ANIMATION_DIRECTION_RTL)
//                .setAnimatorListener(new Animator.AnimatorListener(){});


    toggleAnimation();
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Logo.this,
                        Login.class);
                startActivity(intent);
                finish();
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
    public void toggleAnimation() {
        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.start(textViewLinkSearch);
        }
    }
}
