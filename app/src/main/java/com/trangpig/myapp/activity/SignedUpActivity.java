package com.trangpig.myapp.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.trangpig.myapp.R;

public class SignedUpActivity extends Activity {
    private Button bntsignedUp;
    private EditText edtUser, edtPass, edtRePass, edtName, edtAddress, edtBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_up);
        //
        bntsignedUp = (Button) findViewById(R.id.btn_sign);
        edtUser = (EditText)findViewById(R.id.edt_user_sign);
        edtPass = (EditText)findViewById(R.id.edt_pass_sign);
        edtRePass = (EditText)findViewById(R.id.edt_re_pass_sign);
        edtName = (EditText)findViewById(R.id.edt_name_sign);
        edtAddress = (EditText)findViewById(R.id.edt_address_sign);
        edtBirth = (EditText)findViewById(R.id.edt_birth_sign);
        //
        bntsignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
