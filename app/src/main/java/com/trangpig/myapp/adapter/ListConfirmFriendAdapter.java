package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuocquy.model.Account;
import com.nhuocquy.model.Friend;
import com.nhuocquy.myfile.MyStatus;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by user on 5/20/2015.
 */
public class ListConfirmFriendAdapter extends ArrayAdapter<Friend> {
    Activity context;
    List<Friend> listConfirmFriend;
    int idLayout;
    Account account;
    RestTemplate restTemplate;


    public ListConfirmFriendAdapter(Activity context) {
        super(context,R.layout.my_item_layout_confirm_friend);
        this.context = context;
        account =(Account) Data.getInstance().getAttribute(Data.ACOUNT);
        this.listConfirmFriend = account.getListMakeFrs();
        this.idLayout = R.layout.my_item_layout_confirm_friend;
         restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

    }

    @Override
    public int getCount() {
        return  listConfirmFriend.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(idLayout, null);
        }

        if (listConfirmFriend.size() > 0 && position >= 0) {
            Log.e("tuyet...getcommitfriend", String.valueOf(position));

            final TextView tvAddFr = (TextView) convertView.findViewById(R.id.tvAddFr);
            final ImageView imgAddFr = (ImageView) convertView.findViewById(R.id.imgAddFr);
            final Button bntConfirmFr = (Button) convertView.findViewById(R.id.bntConfirmFr);
            final Button bntDeleteFr = (Button) convertView.findViewById(R.id.bntDeleteFr);
            bntConfirmFr.setFocusable(false);
            bntConfirmFr.setClickable(false);
            bntDeleteFr.setFocusable(false);
            bntDeleteFr.setClickable(false);
            final Friend con = listConfirmFriend.get(position);
            tvAddFr.setText(con.getName());
            imgAddFr.setImageResource(R.drawable.left);
            final String getAddFr = context.getResources().getString(R.string.addFriend);


            // bắt sự kiện chấp nhận kết bạn
            bntConfirmFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AsyncTask<Long, Void, MyStatus>() {
                        MyStatus status;

                        @Override
                        protected MyStatus doInBackground(Long... params) {
                            try {
                                status = restTemplate.getForObject(String.format(MyUri.URL_GET_COMMIT_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);
                            } catch (RestClientException e) {
                                e.printStackTrace();

                            }
                            return status;
                        }

                        @Override
                        protected void onPostExecute(MyStatus s) {
                            super.onPostExecute(s);
                            if (status == null) {
                                Toast.makeText(context, context.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();

                            } else {
                                if (MyStatus.CODE_SUCCESS == status.getCode()) {
                                    account.getListFrs().add(listConfirmFriend.get(position));
                                    account.getListMakeFrs().remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context,context.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }.execute(account.getIdAcc(), listConfirmFriend.get(position).getIdFriend());


                }
            });
            // hủy kết bạn
            bntDeleteFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Long, Void, MyStatus>() {
                        MyStatus status;

                        @Override
                        protected MyStatus doInBackground(Long... params) {
                            try {
                                status = restTemplate.getForObject(String.format(MyUri.URL_GET_UN_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);
                            } catch (RestClientException e) {
                                e.printStackTrace();

                            }
                            return status;
                        }

                        @Override
                        protected void onPostExecute(MyStatus s) {
                            super.onPostExecute(s);
                            if (status == null) {
                                Toast.makeText(context, context.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();

                            } else {
                                if (MyStatus.CODE_SUCCESS == status.getCode()) {
                                    account.getListMakeFrs().remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context,context.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }.execute(account.getIdAcc(), listConfirmFriend.get(position).getIdFriend());

                }
            });
        }
        return convertView;

    }
}
