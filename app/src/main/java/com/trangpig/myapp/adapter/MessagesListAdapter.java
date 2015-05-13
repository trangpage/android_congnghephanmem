package com.trangpig.myapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView lblFrom, txtMsg;
    //
    RestTemplate restTemplate;
    MyFile myFile;
    //for set icon
    String textMes;
    SpannableString spannableString;
    ImageSpan imageSpan;
    Drawable drawable;
    AnimatedGifImageView animatedGifImageView;
//    ImageView imageView;
    String fileName;

    public MessagesListAdapter(final Context context, List<MessageChat> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;

//        this.messagesItems = new ArrayList<>();
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        ///
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

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
        textMes = m.getText();
        if (textMes.indexOf(CHAR_ZERO) != 0) {
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

        } else { // co chua ky tu CHAR_ZERO
            if (m.getIdSender() == account.getIdAcc()) {
                if (textMes.contains(CHAR_ZERO + "image:")) {
                    convertView = mInflater.inflate(R.layout.list_item_message_right_image,
                            null);
                } else if (textMes.contains(GIF)) {
                    convertView = mInflater.inflate(R.layout.list_item_message_right_imagegif,
                            null);
                }
            } else {
                if (textMes.contains(CHAR_ZERO + "image:")) {
                    convertView = mInflater.inflate(R.layout.list_item_message_left_image,
                            null);
                } else if (textMes.contains(GIF)) {
                    convertView = mInflater.inflate(R.layout.list_item_message_left_imagegif,
                            null);
                }
            }
            if (textMes.contains(CHAR_ZERO + "image:")) {
               final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                new AsyncTask<String, Void, MyFile>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        imageView.setImageResource(R.drawable.wait);
                    }

                    @Override
                    protected MyFile doInBackground(String... params) {
                        MyFile myFile = null;
                        try {
                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                        }catch (RestClientException e){
                            e.printStackTrace();

                        }
                        return myFile;
                    }

                    @Override
                    protected void onPostExecute(MyFile myFile) {
                        super.onPostExecute(myFile);
                        if(myFile != null){
                            try {
                                imageView.setImageBitmap(BitmapFactory.decodeByteArray(myFile.getData(), 0, myFile.getData().length));
                            } catch (MyFileException e) {
                                e.printStackTrace();
                                imageView.setImageResource(R.drawable.error);
                                Toast.makeText(context,"Không thể load Image", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            imageView.setImageResource(R.drawable.error);
                            Toast.makeText(context,"Không thể load Image", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(m.getText().substring(m.getText().indexOf(':') + 1));
            } else if (textMes.contains(GIF)) {
                animatedGifImageView = (AnimatedGifImageView) convertView.findViewById(R.id.animatedGifImageView);
                animatedGifImageView.setAnimatedGif(Utils.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
            }
//            animatedGifImageView = (AnimatedGifImageView) convertView.findViewById(R.id.animatedGifImageView);
//            animatedGifImageView.setImageResource(R.drawable.icon_hinhb);
//                animatedGifImageView.setAnimatedGif(R.raw.loading, AnimatedGifImageView.TYPE.STREACH_TO_FIT);
//            if (m.getText().contains(CHAR_ZERO + "image:")) {
//                ImageView imageView = new ImageView(context);
//                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                imageView.setImageResource(R.drawable.wait);
//                parent.addView(imageView);
//                animatedGifImageView.setAnimatedGif(R.drawable.wait, AnimatedGifImageView.TYPE.STREACH_TO_FIT);
//                HashMap<String, Object> hashMap = new HashMap<>();
//                Log.i(MessagesListAdapter.class.getName(), m.getText());
//                hashMap.put("fileName", m.getText().substring(m.getText().indexOf(':') + 1));
//                hashMap.put("imageView", animatedGifImageView);
//                Message messageHandler = handlerReciveImage.obtainMessage();
//                messageHandler.obj = hashMap;
//                handlerReciveImage.sendMessage(messageHandler);
//            } else if (m.getText().contains(GIF)) {
//                animatedGifImageView.setAnimatedGif(Utils.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
//            }
        }
        lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        lblFrom.setText(m.getFromName());
        return convertView;
    }

    private String getSpace(int len) {
        StringBuilder sb = new StringBuilder();
        while (--len < 0) {
            sb.append("");
        }
        return sb.toString();
    }

    public void setListMes(List<MessageChat> list) {
        this.messagesItems = list;
    }
}

