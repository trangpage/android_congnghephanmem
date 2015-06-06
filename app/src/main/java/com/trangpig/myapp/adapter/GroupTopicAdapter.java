package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhuocquy.model.GroupTopic;
import com.trangpig.data.Data;
import com.trangpig.myapp.R;
import com.trangpig.myapp.fragment.TopicFragment;

import java.util.List;

/**
 * Created by TrangPig on 06/06/2015.
 */


public class GroupTopicAdapter extends ArrayAdapter<GroupTopic> {
    List<GroupTopic> groupTopicsList;
    Activity context;
    public GroupTopicAdapter(Activity context,List<GroupTopic> groupTopics) {
        super(context, R.layout.my_item_layout_room);
        this.context = context;
        this.groupTopicsList = groupTopics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.my_item_layout_room, null);
        }
        TextView tvRoom;
        if (groupTopicsList.size() > 0 && position >= 0) {
            tvRoom = (TextView) convertView.findViewById(R.id.tv_item_room);
            tvRoom.setText(groupTopicsList.get(position).getGroupName());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return groupTopicsList.size();
    }

}
