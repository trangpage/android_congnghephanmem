package com.trangpig.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
import com.trangpig.myapp.service.MyService;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * Created by TrangPig on 04/19/2015.
 */
public class Login extends Activity {

    Button btnDangNhap;
    EditText edtIp, edtSdt, edtPass;
    Handler handler;
    Data instanceData;
    private final int success = 1, failure = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        FragmentManager fm = getSupportFragmentManager();
//        ZaloFragmentPagerAdapter zaloAdapter = new ZaloFragmentPagerAdapter(fm,getApplicationContext());
//        pager.setAdapter(zaloAdapter);
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case success:
                        Account account = (Account) msg.obj;
                        if (account != null) {
                            instanceData = Data.getInstance();
                            instanceData.setAttribute(Data.ACOUNT, account);

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startService(new Intent(Login.this, MyService.class));
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case failure:
                        Toast.makeText(Login.this, "Không thể kết nối tới máy chủ!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        btnDangNhap = (Button) findViewById(R.id.btnDangNhapLogin);
        edtIp = (EditText) findViewById(R.id.edtIp);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(Login.this, "Xin chờ ...", "Đang kết nối ...", true);
                new Thread(new Runnable() {
                    Message message = handler.obtainMessage();

                    @Override
                    public void run() {
                        try {
                            RestTemplate rest = new RestTemplate();
                            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                            MyUri.IP = edtIp.getText().toString();
                            Log.v("Nhuoc Quy",String.format(MyUri.LOGIN, MyUri.IP, edtSdt.getText().toString(), edtPass.getText().toString()));
                            Account account = rest.getForObject(String.format(MyUri.LOGIN, MyUri.IP, edtSdt.getText().toString(), edtPass.getText().toString()), Account.class);
                            message.obj = account;
                            message.what = success;

                        } catch (RestClientException e) {
                            e.printStackTrace();
                            message.what = failure;
                        } finally {
                            ringProgressDialog.dismiss();
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
    }
}

