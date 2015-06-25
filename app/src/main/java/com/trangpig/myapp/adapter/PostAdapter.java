package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Post;
import com.nhuocquy.model.Topic;
import com.nhuocquy.myfile.MyFile;
import com.nhuocquy.myfile.MyFileException;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ConversationChat;
import com.trangpig.until.IconSetup;
import com.trangpig.until.MyUri;
import com.trangpig.until.Utils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by TrangPig on 05/23/2015.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Activity context;
    List<Post> postList;
    Post post;
    PostImageOfAdapter postImageOfAdapter;
    SpannableString spannableString;
    Drawable drawable;
    ImageSpan imageSpan;
    int cnt;
    RestTemplate restTemplate;
    LruCache mMemoryCache;
    SimpleDateFormat simpleDateFormat;

    public PostAdapter(Activity context, Topic topic) {
        this.context = context;
        this.postList = topic.getListPosts();
        simpleDateFormat = new SimpleDateFormat("hh:MM");
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post_recyclerview, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        post = postList.get(position);
        // draw icon
        String textMes = post.getContext();
        Set<String> keys = IconSetup.MAP_ICON_DRABLE.keySet();
        Iterator<String> iterKey = keys.iterator();
        String next = "";
        spannableString = new SpannableString(textMes);
        while (iterKey.hasNext()) {
            next = iterKey.next();
            if (textMes.contains(next)) {
                drawable = context.getResources().getDrawable(IconSetup.MAP_ICON_DRABLE.get(next));
                drawable.setBounds(0, -10, 60, 60);
                imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                int index = textMes.indexOf(next);
                spannableString.setSpan(imageSpan, index, index + next.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                textMes.replace(next, Utils.getSpace(next.length()));
            }
        }
        holder.topic.setText(spannableString);
        holder.postName.setText(post.getPoster().getName());
        holder.postDate.setText(simpleDateFormat.format(post.getDatePost()));
        postImageOfAdapter = new PostImageOfAdapter(context, post.getImages());
        cnt = post.getImages().size() <= 2 ? post.getImages().size() : 2;
        holder.tvLike.setText(String.valueOf(post.getClike()));
        holder.tvDisLike.setText(String.valueOf(post.getCdislike()));
//        holder.gridViewImage.setNumColumns(cnt);
        holder.gridViewImage.setNumColumns(4);
        holder.v.getLayoutParams().height = post.getImages().size() == 0? 305 : 270+(post.getImages().size()/4 + (post.getImages().size()%4 > 0? 1 : 0)) *270;
        holder.gridViewImage.setAdapter(postImageOfAdapter);
        holder.bntLike.setOnClickListener(null);
        holder.bntLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post.like();
                new AsyncTask<Void, Void, MyStatus>() {

                    @Override
                    protected MyStatus doInBackground(Void... p) {
                        MyStatus status = null;
                        try {
                            status = restTemplate.postForObject(String.format(MyUri.URL_UPDATE_LIKE, MyUri.IP), post, MyStatus.class);

                        } catch (RestClientException e) {
                            Log.e(ConversationChat.class.getName(), e.getMessage());
                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus status) {
                        super.onPostExecute(status);
                        if (status == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.send_files_failed), Toast.LENGTH_LONG).show();
                        } else {
                            if (status.getCode() == MyStatus.CODE_SUCCESS) {
                                Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                                holder.tvLike.setText(String.valueOf(post.getClike()));
                                holder.bntLike.setEnabled(false);
                                holder.bntDisLike.setEnabled(false);
                            }
                        }
                    }
                }.execute();
            }
        });

        holder.bntDisLike.setOnClickListener(null);
        holder.bntDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post.dislike();
                new AsyncTask<Void, Void, MyStatus>() {

                    @Override
                    protected MyStatus doInBackground(Void... p) {
                        MyStatus status = null;
                        try {
                            status = restTemplate.postForObject(String.format(MyUri.URL_UPDATE_LIKE, MyUri.IP), post, MyStatus.class);

                        } catch (RestClientException e) {
                            Log.e(ConversationChat.class.getName(), e.getMessage());
                        }
                        return status;
                    }

                    @Override
                    protected void onPostExecute(MyStatus status) {
                        super.onPostExecute(status);
                        if (status == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.send_files_failed), Toast.LENGTH_LONG).show();
                        } else {
                            if (status.getCode() == MyStatus.CODE_SUCCESS) {
                                Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                                holder.tvDisLike.setText(String.valueOf(post.getCdislike()));
                                holder.bntLike.setEnabled(false);
                                holder.bntDisLike.setEnabled(false);
                            }
                        }
                    }
                }.execute();
            }
        });
        final PostViewHolder postViewHolder = (PostViewHolder) holder;
            final AsyncTask<String, Void, Bitmap> asyncTaskImage = new AsyncTask<String, Void, Bitmap>() {
                String fileName;
                @Override
                protected Bitmap doInBackground(String... params) {
                    MyFile myFile = null;
                    Bitmap bitmap = Utils.getBitMapFromCache(post.getPoster().getAvatar(), context);
                    if (bitmap == null) {
                        try {
                            Log.e("tuyet....server", params[0]);
                            myFile = restTemplate.getForObject(String.format(MyUri.URL_DOWN_IMAGE, MyUri.IP, params[0]), MyFile.class);
                            if (myFile != null) {
                                bitmap = Utils.decodeSampledBitmapFromResource(myFile.getData(), 450, 450);
                                fileName = myFile.getFileName();
                            }
                            addBitMapToCache(fileName, bitmap);
                        } catch (RestClientException | MyFileException e) {
                            e.printStackTrace();
                        }
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap myFile) {
                    super.onPostExecute(myFile);
                    if (myFile != null) {
                        postViewHolder.imgAvata.setImageBitmap(myFile);
                    } else {
                        postViewHolder.imgAvata.setImageResource(R.drawable.left);
                        Toast.makeText(context, context.getResources().getString(R.string.no_upload_img), Toast.LENGTH_LONG).show();
                    }

                }
            }.execute(post.getPoster().getAvatar());



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        View v;
        ImageView imgAvata;
        TextView postName;
        TextView postDate;
        TextView topic;
        GridView gridViewImage;
        ImageButton bntLike, bntDisLike;
        TextView tvLike, tvDisLike;

        public PostViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            imgAvata = (ImageView) itemView.findViewById(R.id.imAvatar);
            postName = (TextView) itemView.findViewById(R.id.tv_new_port_user);
            postDate = (TextView) itemView.findViewById(R.id.tv_new_port_date);
            topic = (TextView) itemView.findViewById(R.id.tv_new_port_text);
            gridViewImage = (GridView) itemView.findViewById(R.id.gridView_new_post);
            bntLike = (ImageButton) itemView.findViewById(R.id.bntLike);
            bntDisLike = (ImageButton) itemView.findViewById(R.id.bntDisLike);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            tvDisLike = (TextView) itemView.findViewById(R.id.tvDisLike);

        }
    }

    private void addBitMapToCache(String fileName, Bitmap value) {
        if (mMemoryCache != null) {
            mMemoryCache.put(fileName, value);
        }
    }
}
