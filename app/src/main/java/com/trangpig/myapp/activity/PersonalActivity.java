package com.trangpig.myapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Conversation;
import com.nhuocquy.model.Friend;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListFriendAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class PersonalActivity extends ActionBarActivity {
public static final String ID_ACC = "idacc";
    EditText editTimBan, editDiaChi, editNgaySinh;
    TextView textViewName;
    ImageView imgPerAvatar;
    ListView listFr;
    Account account;
    ListFriendAdapter listFriendAdapter;
    RestTemplate restTemplate;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        imgPerAvatar = (ImageView) findViewById(R.id.imgPer_Avatar);
        textViewName = (TextView) findViewById(R.id.tvlPer_Name);
        editDiaChi = (EditText) findViewById(R.id.txtPer_DiaChi);
        editNgaySinh = (EditText) findViewById(R.id.txtPer_NgaySinh);
        editTimBan = (EditText) findViewById(R.id.txtPer_TimBan);
        listFr = (ListView) findViewById(R.id.lvPer_Fr);
        //
        intent = getIntent();
        long idAcc = intent.getLongExtra(ID_ACC, 0);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        new AsyncTask<Long, Void, Account>() {
            @Override
            protected Account doInBackground(Long... params) {
                if(params[0] != account.getIdAcc() )
                    try {
                        account = restTemplate.getForObject(String.format(MyUri.URL_GET_ACCOUNT, MyUri.IP, params[0]), Account.class);
                    } catch (RestClientException e) {
                        e.printStackTrace();
                    }
                return account;
            }
            @Override
            protected void onPostExecute(Account account) {
                super.onPostExecute(account);
                listFriendAdapter = new ListFriendAdapter(PersonalActivity.this,account.getListFrs(),R.layout.my_item_layout_friend);
                listFr.setAdapter(listFriendAdapter);
            }
        }.execute(idAcc);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal, menu);
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
