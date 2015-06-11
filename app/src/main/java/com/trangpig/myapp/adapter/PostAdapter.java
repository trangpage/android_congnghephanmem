package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhuocquy.model.Post;
import com.nhuocquy.model.Topic;
import com.trangpig.myapp.R;
import com.trangpig.until.IconSetup;
import com.trangpig.until.Utils;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by TrangPig on 05/23/2015.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Activity context;
    List<Post> postList ;
    Post post;
    PostImageOfAdapter postImageOfAdapter;
    SpannableString spannableString;
    Drawable drawable;
    ImageSpan imageSpan;
    int cnt;


    SimpleDateFormat simpleDateFormat;
    public PostAdapter(Activity context,Topic topic) {
        this.context = context;
        this.postList = topic.getListPosts();
        simpleDateFormat = new SimpleDateFormat("hh:MM");
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post_recyclerview, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
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
            cnt = post.getImages().size() <= 3 ? post.getImages().size() : post.getImages().size() / 2;
//        holder.gridViewImage.setNumColumns(cnt);
            holder.gridViewImage.setAdapter(postImageOfAdapter);


        }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postName;
        TextView postDate;
        TextView topic;
        GridView gridViewImage;
        ImageButton bntLike, bntDisLike;
        TextView tvLike, tvDisLike;

        public PostViewHolder(View itemView) {
            super(itemView);
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


}
