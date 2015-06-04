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
import com.trangpig.myinterface.SmileFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 4/6/2015.
 */
public class SmileBigFragment extends android.support.v4.app.Fragment implements SmileFragment{
    List<Integer> image =new ArrayList<>();
    List<String> stringIcon   = new ArrayList<>();
    GridView listView;
    GridIconAdapter adapterImag;
    IconActivity parentActivity;
@Override
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

//        //
//      setListIcon(Utils.MAP_ICON_RAW);

        listView = (GridView) view.findViewById(R.id.lvImg);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               parentActivity.sendString(ConversationChat.ICON_BIG,stringIcon.get(position));
            }
        });
        adapterImag = new GridIconAdapter(getActivity(), image, R.layout.icon_page_adapter);
        listView.setAdapter(adapterImag);
        return view;
    }

    @Override
    public void setListIcon(Map<String,Integer> mapIcon){
        String sIcon ="";
        Iterator<String> iterator = mapIcon.keySet().iterator();
        while (iterator.hasNext()){
            sIcon = iterator.next();
            image.add(mapIcon.get(sIcon));
            stringIcon.add(sIcon);
        }

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
