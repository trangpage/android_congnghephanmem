package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
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
    EditText edtSdt, edtPass;
    CheckBox checkboxLuuDangNhap;
    Handler handler;
    Data instanceData;
    SharedPreferences sharedPreferences;
    boolean saveSignIn;
    private final int success = 1, failure = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.accountXML), Context.MODE_PRIVATE);
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
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (saveSignIn) {
                                editor.putLong(getString(R.string.accountid), account.getIdAcc());
                            } else {
                                editor.putLong(getString(R.string.accountid), -1);
                            }
                            editor.putBoolean(getString(R.string.savesignin), saveSignIn);
                            editor.putString(getString(R.string.saveuser), account.getUsername());
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startService(new Intent(Login.this, MyService.class));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, Login.this.getResources().getString(R.string.login_unsucess), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case failure:
                        Toast.makeText(Login.this, Login.this.getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        btnDangNhap = (Button) findViewById(R.id.btnDangNhapLogin);
        checkboxLuuDangNhap = (CheckBox) findViewById(R.id.checkbox_ldn);
        saveSignIn = sharedPreferences.getBoolean(getString(R.string.savesignin), true);
//        saveSignIn = true;
        checkboxLuuDangNhap.setChecked(saveSignIn);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtSdt.setText(sharedPreferences.getString(getString(R.string.saveuser), ""));
        //
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(Login.this, Login.this.getResources().getString(R.string.wait), Login.this.getResources().getString(R.string.conecting), true);
                new Thread(new Runnable() {
                    Message message = handler.obtainMessage();

                    @Override
                    public void run() {
                        try {
                            RestTemplate rest = new RestTemplate();
                            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                            MyUri.IP = edtIp.getText().toString();
                            Log.v("Nhuoc Quy", String.format(MyUri.LOGIN, MyUri.IP, edtSdt.getText().toString(), edtPass.getText().toString()));
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

        checkboxLuuDangNhap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSignIn = isChecked;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Logo.class);
        startActivity(intent);
        finish();
    }
}
