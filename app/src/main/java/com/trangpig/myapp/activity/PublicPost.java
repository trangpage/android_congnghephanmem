package com.trangpig.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.PostImageAdapter;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PublicPost extends Activity {
    static final int PICK_PHOTO_FOR_POST_IMAGES = 1;
    static final int PICK_PHOTO_FOR_POST = 2;

    EditText title, content;
    GridView gridViewPost;
    Button bntPost;
    ImageButton bntImg;
    Handler handlerSentImg;
    Message mesHandler;
    RestTemplate restTemplate;
    List<Uri> image;
    PostImageAdapter postImageAdapter;
    String ImgName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_post);
        title = (EditText) findViewById(R.id.editTitle);
        content = (EditText) findViewById(R.id.editContent);
        gridViewPost = (GridView) findViewById(R.id.gridViewPost);
        bntPost = (Button) findViewById(R.id.bntPost);
        bntImg = (ImageButton) findViewById(R.id.btnImg);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        image = new ArrayList<Uri>();
        postImageAdapter = new PostImageAdapter(PublicPost.this,image,R.layout.icon_page_adapter);
        gridViewPost.setAdapter(postImageAdapter);
        handlerSentImg = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //nhan Uri
                final Uri uri = (Uri) msg.obj;
                final MyFile myFile = Utils.getMyFileFromUri(uri, PublicPost.this);
                new AsyncTask<MyFile, Void, MyStatus>() {
                    @Override
                    protected MyStatus doInBackground(MyFile... myFiles) {
                        MyStatus status = null;
                        try {
                            status = restTemplate.postForObject(String.format(MyUri.URL_UP_IMAGE, MyUri.IP), myFiles[0], MyStatus.class);

                        } catch (RestClientException e) {
                            Log.e(ConversationChat.class.getName(), e.getMessage());
                            Toast.makeText(PublicPost.this, PublicPost.this.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus status) {
                        super.onPostExecute(status);
                        if (status == null) {
                            Toast.makeText(PublicPost.this, PublicPost.this.getResources().getString(R.string.send_files_failed) , Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PublicPost.this, "success" , Toast.LENGTH_LONG).show();
                                image.add(uri);
                                postImageAdapter.notifyDataSetChanged();

                        }
                    }
                }.execute(myFile);
            }
        };


        gridViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(image.get(position));
                    intent.setType("image/*");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.addCategory(Intent.ACTION_VIEW);
                    intent.setData(image.get(position));
                    intent.setType("image/*");
                    startActivity(intent);
                }
            }
        });
        bntImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), PICK_PHOTO_FOR_POST_IMAGES);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO_FOR_POST_IMAGES);
                }
            }


        });


        // ?ang bài
        bntPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_FOR_POST_IMAGES:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    Uri selectedImageUri = data.getData();
                    mesHandler = handlerSentImg.obtainMessage();
                    mesHandler.obj = selectedImageUri;
                    handlerSentImg.sendMessage(mesHandler);
                }
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_public_post, menu);
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
