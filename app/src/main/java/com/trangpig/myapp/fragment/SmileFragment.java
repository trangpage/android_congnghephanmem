package com.trangpig.myapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ConversationChat;
import com.trangpig.myapp.activity.IconActivity;
import com.trangpig.until.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 4/6/2015.
 */
public class SmileFragment extends android.support.v4.app.Fragment {
    List<Integer> image;
    List<String> stringIcon;
    GridView listView;
    GridIconAdapter adapterImag;
    IconActivity parentActivity;

   public void setParentActivity(IconActivity parent){
       this.parentActivity = parent;
   }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_smile, container, false);
        image = new ArrayList<>();
        stringIcon = new ArrayList<>();
//        //
        String sIcon ="";
        Iterator<String> iterator = Utils.MAP_ICON_DRABLE.keySet().iterator();
        while (iterator.hasNext()){
            sIcon = iterator.next();
            image.add(Utils.MAP_ICON_DRABLE.get(sIcon));
            stringIcon.add(sIcon);
        }

        listView = (GridView) view.findViewById(R.id.lvImg);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               parentActivity.sendString(ConversationChat.ICON_SMALL,stringIcon.get(position));
            }
        });
        adapterImag = new GridIconAdapter(getActivity(), image, R.layout.icon_page_adapter);
        listView.setAdapter(adapterImag);
        return view;
    }
    class GridIconAdapter extends ArrayAdapter<Integer> {
        Activity context;
        List<Integer> listImage;
        int id;
        public GridIconAdapter(Activity context, List<Integer> listImage, int id) {
            super(context, id, listImage);
            this.context = context;
            this.listImage = listImage;
            this.id = id;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                convertView = layoutInflater.inflate(id, null);
            }
            if (listImage.size() > 0 && position >= 0) {
                final ImageView imageView = (ImageView) convertView.findViewById(R.id.imv);
                final int imgCon = listImage.get(position);
                imageView.setImageResource(imgCon);
            }
            return convertView;
        }
    }
}
