package com.trangpig.myapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.MessageChat;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.until.AnimatedGifImageView;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import com.nhuocquy.model.Message;

/**
 * Created by TrangPig on 04/15/2015.
 */
public class MessagesListAdapter
        extends BaseAdapter {
    public static final String CHAR_ZERO = String.valueOf((char) 0);
    public static final String GIF = "gif";
    private Context context;
    private List<MessageChat> messagesItems;
    Account account;
    MessageChat m;
    LayoutInflater mInflater;
    TextView lblFrom,txtMsg;
    //
    Handler handlerReciveImage;
    RestTemplate restTemplate;
    MyFile myFile;
    //for set icon
    SpannableString spannableString;
    ImageSpan imageSpan;
    Drawable drawable;
    private AnimatedGifImageView animatedGifImageView;
    public MessagesListAdapter(Context context, List<MessageChat> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;

//        this.messagesItems = new ArrayList<>();
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        ///
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        handlerReciveImage = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                HashMap<String,Object> hashMap = (HashMap<String, Object>) msg.obj;
                String fileName = (String) hashMap.get("fileName");
                AnimatedGifImageView animatedGif = (AnimatedGifImageView) hashMap.get("imageView");
                try {
                    animatedGif.setImageResource(R.drawable.wait);
                    myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE,MyUri.IP, fileName), MyFile.class);
                    if(fileName.contains(GIF)) {
                        animatedGif.setAnimatedGif(myFile.getData(), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
                    }
                    else{
                        animatedGif.setImageBitmap(BitmapFactory.decodeByteArray(myFile.getData(),0,myFile.getData().length));
                    }
                }catch(RestClientException |MyFileException|FileNotFoundException e){
                    animatedGif.setImageResource(R.drawable.error);
                }
            }
        };
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */

         mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            m = messagesItems.get(position);
        if(m.getText().indexOf(CHAR_ZERO) != 0) {
            if (m.getIdSender() == account.getIdAcc()) {
                convertView = mInflater.inflate(R.layout.list_item_message_right,
                        null);
            } else {
                convertView = mInflater.inflate(R.layout.list_item_message_left,
                        null);
            }


            txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        /*txtMsg.setText(m.getText());*/
//        kiem tra icon
            String textMes = m.getText();
            Set<String> keys = Utils.MAP_ICON_DRABLE.keySet();
            Iterator<String> iterKey = keys.iterator();
            String next = "";
            spannableString = new SpannableString(textMes);
            while (iterKey.hasNext()) {
                next = iterKey.next();
                if (textMes.contains(next)) {
                    drawable = context.getResources().getDrawable(Utils.MAP_ICON_DRABLE.get(next));
                    drawable.setBounds(0, -10, 40, 40);
                    imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                    int index = textMes.indexOf(next);
                    spannableString.setSpan(imageSpan, index, index + next.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    textMes.replace(next, getSpace(next.length()));
                }
            }
            txtMsg.setText(spannableString);

        }else{
            if (m.getIdSender() == account.getIdAcc()) {
                convertView = mInflater.inflate(R.layout.list_item_message_right_image,
                        null);
            } else {
                convertView = mInflater.inflate(R.layout.list_item_message_left_image,
                        null);
            }
            animatedGifImageView = (AnimatedGifImageView) convertView.findViewById(R.id.animatedGifImageView);
            if(m.getText().contains(CHAR_ZERO+"image:")){
                try {
                    animatedGifImageView.setAnimatedGif(new byte[1],AnimatedGifImageView.TYPE.STREACH_TO_FIT);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("fileName",m.getText().substring(':'));
                    hashMap.put("imageView",animatedGifImageView);
                    Message messageHandler = handlerReciveImage.obtainMessage();
                    messageHandler.obj = hashMap;
                    handlerReciveImage.sendMessage(messageHandler);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(m.getText().contains(GIF)){
                animatedGifImageView.setAnimatedGif(Utils.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
            }else{
                animatedGifImageView.setImageResource(Utils.MAP_ICON_DRABLE.get(new String(":)")));
            }

        }
        lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        lblFrom.setText(m.getFromName());
        return convertView;
    }
    private String getSpace(int len){
        StringBuilder sb = new StringBuilder();
        while (--len<0){
            sb.append("");
        }
        return sb.toString();
    }
    public void setListMes (List<MessageChat> list){
        this.messagesItems = list;
    }
}

