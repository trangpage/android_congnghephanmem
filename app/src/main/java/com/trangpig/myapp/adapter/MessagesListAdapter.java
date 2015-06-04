package com.trangpig.myapp.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.LruCache;
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
import com.trangpig.myapp.activity.ImageDetailActivity;
import com.trangpig.until.AnimatedGifImageView;
import com.trangpig.until.MyUri;
import com.trangpig.until.IconSetup;

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
    LruCache mMemoryCache;
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
        //
        if (Build.VERSION.SDK_INT >= 12) {
            final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
            final int cacheSize = 1024 * 1024 * memClass / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }
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
            Set<String> keys = IconSetup.MAP_ICON_DRABLE.keySet();
            Iterator<String> iterKey = keys.iterator();
            String next = "";
            spannableString = new SpannableString(textMes);
            while (iterKey.hasNext()) {
                next = iterKey.next();
                if (textMes.contains(next)) {
                    drawable = context.getResources().getDrawable(IconSetup.MAP_ICON_DRABLE.get(next));
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
            new AsyncTask<String, Void, Bitmap>() {
                String fileName;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    imageView.setImageResource(R.drawable.wait);
                    imageView.setOnClickListener(null);
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    MyFile myFile = null;
                    Bitmap bitmap = getBitMapFromCache(params[0]);
                    if (bitmap == null)
                        try {
                            Log.e("tuyet....server", params[0]);
                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                            if (myFile != null) {
                                bitmap = decodeSampledBitmapFromResource(myFile.getData(), 450, 450);
                                fileName = myFile.getFileName();
                            }
                            addBitMapToCache(fileName,bitmap);
                        } catch (RestClientException | MyFileException e) {
                            e.printStackTrace();
                        }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap myFile) {
                    super.onPostExecute(myFile);
                    if (myFile != null) {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ImageDetailActivity.class);
                                intent.putExtra(ImageDetailActivity.FILE_NAME, fileName);
                                context.startActivity(intent);
                            }
                        });
                        imageView.setImageBitmap(myFile);
                    } else {
                        imageView.setImageResource(R.drawable.error);
                        Toast.makeText(context, context.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                    }

                }
            }.execute(m.getText().replace(KEY_IMAGE, ""));
        } else if (viewHolder instanceof ViewHolderGif) {
            ViewHolderGif viewHolderGif = (ViewHolderGif) viewHolder;
            viewHolderGif.gifMes.setAnimatedGif(IconSetup.MAP_ICON_RAWS.get(m.getText()), AnimatedGifImageView.TYPE.STREACH_TO_FIT);
        }
        ((ViewHolderAbs) viewHolder).lblFrom.setText(m.getFromName());
        Log.e("tuyet.....ke", textMes);
    }

    @Override
    public int getItemCount() {
        Log.e("tuyet.....count",String.valueOf( messagesItems.size()));
        return messagesItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.e("trang.....ke", String.valueOf( i));
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


    public static Bitmap decodeSampledBitmapFromResource(byte[] data, int reqWidth, int reqHeight) {

        // BEGIN_INCLUDE (read_bitmap_dimensions)
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // END_INCLUDE (read_bitmap_dimensions)

        // If we're running on Honeycomb or newer, try to use inBitmap

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // BEGIN_INCLUDE (calculate_sample_size)
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    private Bitmap getBitMapFromCache(String fileName) {
        if (mMemoryCache != null) {
            return (Bitmap) mMemoryCache.get(fileName);
        }
        return null;
    }

    private void addBitMapToCache(String fileName, Bitmap value) {
        if (mMemoryCache != null) {
            mMemoryCache.put(fileName, value);
        }
    }
}

