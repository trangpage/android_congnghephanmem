package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SignedUpActivity extends Activity {
    private Button bntsignedUp;
    private EditText edtUser, edtPass, edtRePass, edtName, edtAddress, edtBirth;
    Account account;
    String userName, pass, repass;
    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView tvBirthDisplay;
    private ImageButton mPickDate;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    RestTemplate restTemplate;
    ProgressDialog ringProgressDialog;

    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_up);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvBirthDisplay.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear + 1, year));
            }
        };
        datePickerDialog = new DatePickerDialog(SignedUpActivity.this, onDateSetListener, 1990, 1, 1);


        tvBirthDisplay = (TextView) findViewById(R.id.tv_hienthi);
        mPickDate = (ImageButton) findViewById(R.id.bntNgaySinh);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        //
        bntsignedUp = (Button) findViewById(R.id.btn_sign);
        edtUser = (EditText) findViewById(R.id.edt_user_sign);
        edtPass = (EditText) findViewById(R.id.edt_pass_sign);
        edtRePass = (EditText) findViewById(R.id.edt_re_pass_sign);
        edtName = (EditText) findViewById(R.id.edt_name_sign);
        edtAddress = (EditText) findViewById(R.id.edt_address_sign);


        bntsignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edtUser.getText().toString();
                pass = edtPass.getText().toString();
                repass = edtRePass.getText().toString();
                try {
                    if (userName.length() != 0) {
                        if (pass.length() > 8) {
                            if (pass.equals(repass)) {
                                account = new Account();
                                account.setAddress(edtAddress.getText().toString());
                                account.setBirthday(simpleDateFormat.parse(tvBirthDisplay.getText().toString()));
                                account.setName(edtName.getText().toString());
                                account.setUsername(userName);
                                account.setPassword(pass);
                                new AsyncTask<Account, Void, MyStatus>() {
                                    MyStatus status;

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        ringProgressDialog = ProgressDialog.show(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.wait), SignedUpActivity.this.getResources().getString(R.string.conecting), true);
                                    }

                                    @Override
                                    protected MyStatus doInBackground(Account... params) {
                                        Log.e("tuyet ??ng kí........",account.toString() );
                                        status = restTemplate.postForObject(String.format(MyUri.URL_SIGN_UP, MyUri.IP), account, MyStatus.class);
                                        return status;
                                    }

                                    @Override
                                    protected void onPostExecute(MyStatus myStatus) {
                                        super.onPostExecute(myStatus);
                                        Log.e("tuyet ??ng kí........", myStatus.getObj().toString());
                                        if (myStatus == null) {
                                            Toast.makeText(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.fail_sign_up), Toast.LENGTH_LONG).show();
                                        } else {

                                            if (myStatus.getCode() == myStatus.CODE_SUCCESS) {
                                                Toast.makeText(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.success_sign_up), Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                Toast.makeText(SignedUpActivity.this, status.getObj().toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        ringProgressDialog.dismiss();
                                    }
                                }.execute();
                            } else {
                                Toast.makeText(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.fail_pass), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.require_pass), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(SignedUpActivity.this, SignedUpActivity.this.getResources().getString(R.string.fail_user), Toast.LENGTH_LONG).show();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signed_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
