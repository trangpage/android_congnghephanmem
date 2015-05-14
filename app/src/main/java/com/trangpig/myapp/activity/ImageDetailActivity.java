package com.trangpig.myapp.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends Activity {
    public static final String FILE_NAME = "fileName";
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        final ImageView imgDetail = (ImageView) findViewById(R.id.imgdetail);
        imgDetail.setLongClickable(true);
        imgDetail.setClickable(true);
//        imgDetail.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.e("tuyet....click","long click");
//                registerForContextMenu();
//                return true;
//            }
//        });
        registerForContextMenu(imgDetail);

        Intent intent = getIntent();
        fileName = intent.getStringExtra(FILE_NAME);
        new AsyncTask<String, Void, MyFile>() {
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
        if (id == R.id.download) {
            Log.e("tuyet.......context", fileName);
            new AsyncTask<String, Void, MyFile>() {
                RestTemplate restTemplate;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                }

                @Override
                protected MyFile doInBackground(String... params) {
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
//                        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator);
//                        dir.mkdirs();
                        String pathFile = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator + fileName;
                        File f = new File(pathFile);
                        try {
                            Log.e("tuyet.......context", pathFile);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(myFile.getData());
                            fo.close();
                            //
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_VIEW);
                            File file = new File(pathFile);
                            intent.setDataAndType(Uri.fromFile(file), "image/*");

                            PendingIntent pIntent = PendingIntent.getActivity(ImageDetailActivity.this, 0, intent, 0);

                            Notification noti = new NotificationCompat.Builder(ImageDetailActivity.this)
                                    .setContentTitle("Download thành công")
                                    .setContentText(fileName)
                                    .setSmallIcon(R.drawable.download)
                                    .setContentIntent(pIntent).build();
                            noti.flags |= Notification.FLAG_AUTO_CANCEL;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0, noti);
                            playBeep();
                        } catch (IOException | MyFileException e) {
                            e.printStackTrace();
                            Toast.makeText(ImageDetailActivity.this, "Không thể tạo file", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ImageDetailActivity.this, "Không thể tạo file", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute(fileName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
