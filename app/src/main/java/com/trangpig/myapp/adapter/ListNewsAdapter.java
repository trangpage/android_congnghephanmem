package com.trangpig.myapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Friend;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by user on 5/6/2015.
 */
public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ViewNews>{
    Account account;
    List<Friend> listFr;
    Button btnComment, btnLike;
    EditText edtComment;
    ListView listView;
    CommentAdapter commentAdapter;

    public ListNewsAdapter(){
        this.account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        listFr = account.getListFrs();

    }

    @Override
    public ViewNews onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewNews(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewNews holder, int position) {
//        commentAdapter = new CommentAdapter();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewNews extends RecyclerView.ViewHolder{
        TextView tvPostUser, tvPostTime, tvPost ;
        Button btnLike, btnComment, btnSendComment;
        EditText edtComment;
        ListView listView;
        public ViewNews(View itemView) {
            super(itemView);
        }
    }
}
