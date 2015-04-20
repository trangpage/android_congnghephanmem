package com.trangpig.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.model.Friend;
import com.nhuocquy.model.Message;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by TrangPig on 04/19/2015.
 */
public class Login extends Activity {

    Button btnDangNhap ;
    EditText edtIp, edtSdt, edtPass;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        FragmentManager fm = getSupportFragmentManager();
//        ZaloFragmentPagerAdapter zaloAdapter = new ZaloFragmentPagerAdapter(fm,getApplicationContext());
//        pager.setAdapter(zaloAdapter);
        handler = new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                Account account = (Account) msg.obj;
                if(account != null){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    ArrayList<Conversation> arrCon;
                    ArrayList<Friend> arrFrs;
                    ArrayList<Message> arrMes;
                    arrCon=new ArrayList<Conversation>();
                    arrFrs = new ArrayList<Friend>();
                    arrMes = new ArrayList<Message>();
                    arrFrs.add(new Friend(1, "Trang, Tuyet"));
                    arrMes.add(new Message(1,1,"Trang, Tuyet","hello",new Date()));
                    arrCon.add(new Conversation(1,arrFrs,arrMes));
                    arrCon.add(new Conversation(1,arrFrs,arrMes));
                    arrCon.add(new Conversation(1,arrFrs,arrMes));
                    account.setConversations(arrCon);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("acc",account);
                    intent.putExtra("acc", account);
                    startActivity(intent);

                }else{
                    Toast.makeText(Login.this,"Dang nhap ko thanh cong",Toast.LENGTH_LONG).show();
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
               new Thread( new Runnable() {
                   @Override
                   public void run() {
                       try {
                           RestTemplate rest = new RestTemplate();
                           rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                           Account account = rest.getForObject(String.format(MyUri.LOGIN, edtIp.getText().toString()) +
                                   "?username=" + edtSdt.getText().toString() + "&password=" + edtPass.getText().toString(), Account.class);
                           android.os.Message  message = handler.obtainMessage();
                           message.obj = account;
                           handler.sendMessage(message);

                   }catch(RestClientException e){
                           Toast.makeText(Login.this,"Ko thể kết nối máy chủ",Toast.LENGTH_LONG).show();
                   }}
               }).start();
            }
        });


            }


    }

