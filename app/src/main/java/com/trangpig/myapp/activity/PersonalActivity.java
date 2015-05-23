package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.ListFriendAdapter;
import com.trangpig.myapp.fragment.ListFriendFragment;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PersonalActivity extends Activity {
public static final String ID_ACC = "idacc";
    String getAddFr;
    EditText editTimBan, editDiaChi, editNgaySinh;
    TextView textViewName;
    ImageView imgPerAvatar;
    ListView listFr;
    Button btnKetBan, btnTinNhan;
    Account account;
    ListFriendAdapter listFriendAdapter;
    RestTemplate restTemplate;
    Intent intent;
    long idAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        //
        getAddFr = this.getResources().getString(R.string.addFriend);
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        imgPerAvatar = (ImageView) findViewById(R.id.imgPer_Avatar);
        textViewName = (TextView) findViewById(R.id.tvlPer_Name);
        editDiaChi = (EditText) findViewById(R.id.txtPer_DiaChi);
        editNgaySinh = (EditText) findViewById(R.id.txtPer_NgaySinh);
        editTimBan = (EditText) findViewById(R.id.txtPer_TimBan);
        listFr = (ListView) findViewById(R.id.lvPer_Fr);
        btnKetBan = (Button) findViewById(R.id.btnKetBan);
        btnKetBan.setVisibility(View.INVISIBLE);
        btnTinNhan = (Button) findViewById(R.id.btnTinNhan);
        //
        intent = getIntent();
        idAcc = intent.getLongExtra(ID_ACC, 0);

        for(int i = 0; i<account.getListFrs().size();i++){
            if(idAcc == account.getListFrs().get(i).getIdFriend())
                btnKetBan.setVisibility(View.VISIBLE);
        }

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
                textViewName.setText(account.getName());
                editDiaChi.setText(account.getAddress());
                editNgaySinh.setText(account.getBirthday().toString());
                listFriendAdapter = new ListFriendAdapter(PersonalActivity.this,account.getListFrs(),R.layout.my_item_layout_friend);
                listFr.setAdapter(listFriendAdapter);
            }
        }.execute(idAcc);

        btnKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Long, Void, MyStatus>() {
                    MyStatus status;

                    @Override
                    protected MyStatus doInBackground(Long... params) {
                        try {
                            if (btnKetBan.getText().equals(getAddFr)) {
                                status = restTemplate.getForObject(String.format(MyUri.URL_GET_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);

                            } else {
                                status = restTemplate.getForObject(String.format(MyUri.URL_GET_UN_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);

                            }
                        } catch (RestClientException e) {
                            e.printStackTrace();

                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus s) {
                        super.onPostExecute(s);
                        if (status == null) {
                            Toast.makeText(PersonalActivity.this, PersonalActivity.this.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();

                        } else {
                            if (btnKetBan.getText().equals(getAddFr)) {
                                btnKetBan.setText(PersonalActivity.this.getResources().getString(R.string.makeFriend));
                            } else {
                                if (MyStatus.CODE_SUCCESS == status.getCode()) {
                                    btnKetBan.setText(PersonalActivity.this.getResources().getString(R.string.addFriend));
                                    Toast.makeText(PersonalActivity.this, PersonalActivity.this.getResources().getString(R.string.canceled_sent_request), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(PersonalActivity.this, PersonalActivity.this.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }
                }.execute(idAcc, account.getIdAcc());


            }
        });

        btnTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCon = new Intent(PersonalActivity.this,ConversationChat.class);
                intentCon.putExtra(ListFriendFragment.ID_FRIENDS, new long[]{account.getIdAcc(), idAcc});
                startActivity(intentCon);
            }
        });
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
