package com.trangpig.myapp.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import com.nhuocquy.model.Message;

/**
 * Created by TrangPig on 04/15/2015.
 */
public class MessagesListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String CHAR_ZERO = String.valueOf((char) 0);
    public static final String GIF = "gif";
    public static final String KEY_IMAGE = CHAR_ZERO + "image:";
    public static final int VH_TEXT_LEFT = 1;
    public static final int VH_TEXT_RIGHT = 2;
    public static final int VH_IMAGE_LEFT = 3;
    public static final int VH_IMAGE_RIGHT = 4;
    public static final int VH_GIF_LEFT = 5;
    public static final int VH_GIF_RIGHT = 6;
    private Context context;
    private List<MessageChat> messagesItems;
    Account account;
    MessageChat m;
    LayoutInflater mInflater;

    //
    RestTemplate restTemplate;
    MyFile myFile;
    //for set icon
    String textMes;
    SpannableString spannableString;
    ImageSpan imageSpan;
    Drawable drawable;
//    AnimatedGifImageView animatedGifImageView;
    //    ImageView imageView;
//    String fileName;

    public MessagesListAdapter(final Context context, List<MessageChat> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;

//        this.messagesItems = new ArrayList<>();
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        ///
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    }
    // view holer

    public abstract class ViewHolderAbs extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView lblFrom;
        ImageView imAvatar;

        public ViewHolderAbs(View v) {
            super(v);
            lblFrom = (TextView) v.findViewById(R.id.lblMsgFrom);
            imAvatar = (ImageView) v.findViewById(R.id.imAvatar);
        }
    }

    public class ViewHolderImg extends ViewHolderAbs {
        ImageView imMes;

        public ViewHolderImg(View v) {
            super(v);
            imMes = (ImageView) v.findViewById(R.id.imMes);
        }
    }

    public class ViewHolderGif extends ViewHolderAbs {
        AnimatedGifImageView gifMes;

        public ViewHolderGif(View v) {
            super(v);
            gifMes = (AnimatedGifImageView) v.findViewById(R.id.gifMes);
        }
    }

    public class ViewHolderText extends ViewHolderAbs {
        TextView txtMes;

        public ViewHolderText(View v) {
            super(v);
            txtMes = (TextView) v.findViewById(R.id.txtMsg);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        m = messagesItems.get(i);
        if (viewHolder instanceof ViewHolderText) {
            ViewHolderText viewHolderText = (ViewHolderText) viewHolder;
            viewHolderText.lblFrom.setText(m.getFromName());
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

            viewHolderText.txtMes.setText(spannableString);
        } else if (viewHolder instanceof ViewHolderImg) {
            ViewHolderImg viewHolderImg = (ViewHolderImg) viewHolder;
            final ImageView imageView = viewHolderImg.imMes;
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
                            imageView.setImageBitmap(BitmapFactory.decodeByteArray(myFile.getData(), 0, myFile.getData().length));
                        } catch (MyFileException e) {
                            e.printStackTrace();
                            imageView.setImageResource(R.drawable.error);
                            Toast.makeText(context, "Không thể load Image", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        imageView.setImageResource(R.drawable.error);
                        Toast.makeText(context, "Không thể load Image", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute(m.getText().replace(KEY_IMAGE, ""));
        } else if (viewHolder instanceof ViewHolderGif) {
            ViewHolderGif viewHolderGif = (ViewHolderGif) viewHolder;
            viewHolderGif.gifMes.setAnimatedGif(Utils.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
        }
        ((ViewHolderAbs) viewHolder).lblFrom.setText(m.getFromName());
        Log.e("tuyet.....ke", textMes);
    }

    @Override
    public int getItemCount() {
        return messagesItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolderAbs viewHolder = null;
        View v = null;
        switch (i) {
            case VH_TEXT_LEFT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_left, viewGroup, false);
                viewHolder = new ViewHolderText(v);
                break;
            case VH_TEXT_RIGHT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_right, viewGroup, false);
                viewHolder = new ViewHolderText(v);
                break;
            case VH_GIF_LEFT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_left_imagegif, viewGroup, false);
                viewHolder = new ViewHolderGif(v);
                break;
            case VH_GIF_RIGHT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_right_imagegif, viewGroup, false);
                viewHolder = new ViewHolderGif(v);
                break;
            case VH_IMAGE_LEFT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_left_image, viewGroup, false);
                viewHolder = new ViewHolderImg(v);
                break;
            case VH_IMAGE_RIGHT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message_right_image, viewGroup, false);
                viewHolder = new ViewHolderImg(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int type = VH_TEXT_LEFT;
        m = messagesItems.get(position);
        textMes = m.getText();
        if (textMes.indexOf(CHAR_ZERO) != 0) {
            if (m.getIdSender() == account.getIdAcc())
                type = VH_TEXT_RIGHT;
            else
                type = VH_TEXT_LEFT;
        } else {
            if (textMes.contains(KEY_IMAGE)) {
                if (m.getIdSender() == account.getIdAcc())
                    type = VH_IMAGE_RIGHT;
                else
                    type = VH_IMAGE_LEFT;
            } else if (textMes.contains(GIF))
                if (m.getIdSender() == account.getIdAcc())
                    type = VH_GIF_RIGHT;
                else
                    type = VH_GIF_LEFT;
        }
        Log.e("tuyet....char0", m.getText() + " : " + type);
        return type;
    }
    //    @Override
//    public int getCount() {
//        return messagesItems.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return messagesItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @SuppressLint("InflateParams")
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        /**
//         * The following list not implemented reusable list items as list items
//         * are showing incorrect data Add the solution if you have one
//         * */
//
//        mInflater = (LayoutInflater) context
//                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        m = messagesItems.get(position);
//        textMes = m.getText();
//        if (textMes.indexOf(CHAR_ZERO) != 0) {
//            if (m.getIdSender() == account.getIdAcc()) {
//                convertView = mInflater.inflate(R.layout.list_item_message_right,
//                        null);
//            } else {
//                convertView = mInflater.inflate(R.layout.list_item_message_left,
//                        null);
//            }
//
//
//            txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        /*txtMsg.setText(m.getText());*/
//        kiem tra icon

    //            Set<String> keys = Utils.MAP_ICON_DRABLE.keySet();
//            Iterator<String> iterKey = keys.iterator();
//            String next = "";
//            spannableString = new SpannableString(textMes);
//            while (iterKey.hasNext()) {
//                next = iterKey.next();
//                if (textMes.contains(next)) {
//                    drawable = context.getResources().getDrawable(Utils.MAP_ICON_DRABLE.get(next));
//                    drawable.setBounds(0, -10, 40, 40);
//                    imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
//                    int index = textMes.indexOf(next);
//                    spannableString.setSpan(imageSpan, index, index + next.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    textMes.replace(next, getSpace(next.length()));
//                }
//            }
//            txtMsg.setText(spannableString);
//
//        } else { // co chua ky tu CHAR_ZERO
//            if (m.getIdSender() == account.getIdAcc()) {
//                if (textMes.contains(CHAR_ZERO + "image:")) {
//                    convertView = mInflater.inflate(R.layout.list_item_message_right_image,
//                            null);
//                } else if (textMes.contains(GIF)) {
//                    convertView = mInflater.inflate(R.layout.list_item_message_right_imagegif,
//                            null);
//                }
//            } else {
//                if (textMes.contains(CHAR_ZERO + "image:")) {
//                    convertView = mInflater.inflate(R.layout.list_item_message_left_image,
//                            null);
//                } else if (textMes.contains(GIF)) {
//                    convertView = mInflater.inflate(R.layout.list_item_message_left_imagegif,
//                            null);
//                }
//            }
//            if (textMes.contains(CHAR_ZERO + "image:")) {
//               final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                new AsyncTask<String, Void, MyFile>() {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        imageView.setImageResource(R.drawable.wait);
//                    }
//
//                    @Override
//                    protected MyFile doInBackground(String... params) {
//                        MyFile myFile = null;
//                        try {
//                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
//                        }catch (RestClientException e){
//                            e.printStackTrace();
//
//                        }
//                        return myFile;
//                    }
//
//                    @Override
//                    protected void onPostExecute(MyFile myFile) {
//                        super.onPostExecute(myFile);
//                        if(myFile != null){
//                            try {
//                                imageView.setImageBitmap(BitmapFactory.decodeByteArray(myFile.getData(), 0, myFile.getData().length));
//                            } catch (MyFileException e) {
//                                e.printStackTrace();
//                                imageView.setImageResource(R.drawable.error);
//                                Toast.makeText(context,"Không thể load Image", Toast.LENGTH_LONG).show();
//                            }
//                        }else{
//                            imageView.setImageResource(R.drawable.error);
//                            Toast.makeText(context,"Không thể load Image", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }.execute(m.getText().substring(m.getText().indexOf(':') + 1));
//            } else if (textMes.contains(GIF)) {
//                animatedGifImageView = (AnimatedGifImageView) convertView.findViewById(R.id.animatedGifImageView);
//                animatedGifImageView.setAnimatedGif(Utils.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
//            }
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
//        }
//        lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
//        lblFrom.setText(m.getFromName());
//        return convertView;
//    }
//
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

