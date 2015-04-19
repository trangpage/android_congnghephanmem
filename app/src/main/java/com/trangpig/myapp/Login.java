package com.trangpig.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trangpig.model.Account;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by TrangPig on 04/19/2015.
 */
public class Login extends Activity {

    Button btnDangNhap ;
    EditText edtIp, edtSdt, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        FragmentManager fm = getSupportFragmentManager();
//        ZaloFragmentPagerAdapter zaloAdapter = new ZaloFragmentPagerAdapter(fm,getApplicationContext());
//        pager.setAdapter(zaloAdapter);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhapLogin);
        edtIp = (EditText) findViewById(R.id.edtIp);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestTemplate rest = new RestTemplate();
                rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Account account = rest.getForObject(String.format( MyUri.LOGIN,edtIp.getText().toString())+
                        "?username="+edtSdt.getText().toString()+"&password="+edtPass.getText().toString(),Account.class);
                if(account != null){
                    Toast.makeText(Login.this,"Dang nhap thanh cong"+account.getName(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this,"Dang nhap ko thanh cong",Toast.LENGTH_LONG).show();
                }
            }
        });


            }


    }

