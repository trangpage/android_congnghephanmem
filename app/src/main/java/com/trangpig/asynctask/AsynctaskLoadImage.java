package com.trangpig.asynctask;

import android.os.AsyncTask;

import com.nhuocquy.myfile.MyFile;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by NhuocQuy on 5/10/2015.
 */
public class AsynctaskLoadImage extends AsyncTask<String, Void, MyFile> {
    @Override
    protected MyFile doInBackground(String... params) {
        MyFile myFile = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
        }catch (RestClientException e){
            e.printStackTrace();
        }
        return myFile;
    }
}
