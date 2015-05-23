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
 * Created by user on 5/18/2015.
 */
public class ListAddFriendAdapter extends ArrayAdapter<Friend> {

    Activity context;
    List<Friend> listAddFriend;
    int idLayout = R.layout.my_item_layout_add_friend;
    ;

    Account account;
    RestTemplate restTemplate;

    public ListAddFriendAdapter(Activity context, List<Friend> listFr) {
        super(context, R.layout.my_item_layout_add_friend);
        this.context = context;
        this.listAddFriend = listFr;
        account = (Account) Data.getInstance().getAttribute(Data.ACOUNT);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public int getCount() {
        return listAddFriend.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            Log.e("tuyet...getview", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            convertView = layoutInflater.inflate(idLayout, null);
        }
        if (listAddFriend.size() > 0 && position >= 0) {
            final TextView tvAddFr = (TextView) convertView.findViewById(R.id.tvAddFr);
            final ImageView imgAddFr = (ImageView) convertView.findViewById(R.id.imgAddFr);
            final Button bntAddFr = (Button) convertView.findViewById(R.id.bntAddFr);
            bntAddFr.setFocusable(false);
            bntAddFr.setClickable(false);
            final Friend con = listAddFriend.get(position);
            tvAddFr.setText(con.getName());
            imgAddFr.setImageResource(R.drawable.left);
            final String getAddFr = context.getResources().getString(R.string.addFriend);
            Log.e("tuyet...getaddfriend", String.valueOf(position));
            bntAddFr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AsyncTask<Long, Void, MyStatus>() {
                        MyStatus status;

                        @Override
                        protected MyStatus doInBackground(Long... params) {
                            try {
                                if (bntAddFr.getText().equals(getAddFr)) {
                                    status = restTemplate.getForObject(String.format(MyUri.URL_GET_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);

                                } else {
                                    status = restTemplate.getForObject(String.format(MyUri.URL_GET_UN_MAKE_FRIEND, MyUri.IP, params[0], params[1]), MyStatus.class);

                                }
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
                                if (bntAddFr.getText().equals(getAddFr)) {
                                    bntAddFr.setText(context.getResources().getString(R.string.makeFriend));
                                } else {
                                    if (MyStatus.CODE_SUCCESS == status.getCode()) {
                                        bntAddFr.setText(context.getResources().getString(R.string.addFriend));
                                        Toast.makeText(context, context.getResources().getString(R.string.canceled_sent_request), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(context, context.getResources().getString(R.string.sent_request_fail), Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }
                    }.execute(listAddFriend.get(position).getIdFriend(), account.getIdAcc());


                }
            });
        }
        return convertView;

    }
}
