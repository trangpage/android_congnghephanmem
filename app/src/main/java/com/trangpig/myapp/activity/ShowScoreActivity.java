package com.trangpig.myapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.nhuocquy.model.Student;
import com.trangpig.myapp.R;
import com.trangpig.myapp.fragment.SearchScoreFragment;
import com.trangpig.myapp.adapter.SubjectAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

public class ShowScoreActivity extends Activity {

    TextView tvSBD,tvName,tvAddress,tvBirdth,tvNganh,tvTongDiem;
    ListView lvScore;
    RestTemplate restTemplate;
    String sbd;
    SimpleDateFormat f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_thong_tin);
        tvSBD = (TextView) findViewById(R.id.tv_tra_cuu_sbd);
        tvName = (TextView) findViewById(R.id.tv_tra_cuu_name);
        tvAddress = (TextView) findViewById(R.id.tv_tra_cuu_noi_sinh);
        tvBirdth = (TextView) findViewById(R.id.tv_tra_cuu_nam_sinh);
        tvNganh = (TextView) findViewById(R.id.tv_tra_cuu_ma_nganh);
        tvTongDiem = (TextView) findViewById(R.id.tv_tra_cuu_tong_diem);
        lvScore = (ListView) findViewById(R.id.lv_score);

        sbd = getIntent().getStringExtra(SearchScoreFragment.SBD_STUDENT);
        f = new SimpleDateFormat("dd/MM/yyyy");
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        new AsyncTask<Void, Void, Void>() {
            Student student;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    student = restTemplate.getForObject(String.format(MyUri.URL_FIND_STUDENT_BY_SBD, MyUri.IP, sbd), Student.class);
                } catch (RestClientException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                tvSBD.setText(student.getSBD());
                tvName.setText(student.getName());
                tvAddress.setText(student.getAddress());
                tvBirdth.setText(f.format(student.getBirthday()));
                tvNganh.setText(student.getSchoolRegisterCode());
                tvTongDiem.setText(String.valueOf(student.totalScrore()));
                lvScore.setAdapter(new SubjectAdapter(ShowScoreActivity.this,student.getSubjects()));
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tra_cuu_thong_tin, menu);
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
