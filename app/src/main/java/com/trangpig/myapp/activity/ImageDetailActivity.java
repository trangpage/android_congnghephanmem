package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ImageDetailActivity extends Activity {
    public static final String FILE_NAME = "fileName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        final ImageView imgDetail = (ImageView) findViewById(R.id.imgdetail);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra(FILE_NAME);

        new AsyncTask<String,Void,MyFile>(){
             RestTemplate restTemplate;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgDetail.setImageResource(R.drawable.wait);
                restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            }


            @Override
            protected MyFile doInBackground(String... params) {
                Log.e("tuyet.............img", params[0]);
                MyFile myFile = null;
                try {
                    myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                } catch (RestClientException e) {
                    e.printStackTrace();

                }
                return myFile;

            }
            @Override
            protected void onPostExecute(MyFile myFile) {
                super.onPostExecute(myFile);
                if (myFile != null) {
                    try {
                            imgDetail.setImageBitmap(BitmapFactory.decodeByteArray(myFile.getData(), 0, myFile.getData().length));

                    } catch (MyFileException e) {
                        e.printStackTrace();
                        imgDetail.setImageResource(R.drawable.error);
                        Toast.makeText(ImageDetailActivity.this, "Không thể load Image", Toast.LENGTH_LONG).show();
                    }
                } else {
                    imgDetail.setImageResource(R.drawable.error);
                    Toast.makeText(ImageDetailActivity.this, "Không thể load Image", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(fileName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
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
