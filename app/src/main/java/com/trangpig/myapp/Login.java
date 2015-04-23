package com.trangpig.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
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
//                    ArrayList<Conversation> arrCon;
//                    ArrayList<Friend> arrFrs;
//                    ArrayList<Message> arrMes;
//                    arrCon=new ArrayList<Conversation>();
//                    arrFrs = new ArrayList<Friend>();
//                    arrMes = new ArrayList<Message>();
//                    arrFrs.add(new Friend(1, "Trang, Tuyet"));
//                    arrMes.add(new Message(1,1,"Trang, Tuyet","hello",new Date()));
//                    arrCon.add(new Conversation(1,arrFrs,arrMes));
//                    arrCon.add(new Conversation(1,arrFrs,arrMes));
//                    arrCon.add(new Conversation(1,arrFrs,arrMes));
//                    account.setConversations(arrCon);
                            Intent intent = new Intent(Login.this, MainActivity.class);
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

                            Account account = rest.getForObject(String.format(MyUri.LOGIN, edtIp.getText().toString()) +
                                    "?username=" + edtSdt.getText().toString() + "&password=" + edtPass.getText().toString(), Account.class);
                            message.obj = account;
                            message.what = success;

                        } catch (RestClientException e) {
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

