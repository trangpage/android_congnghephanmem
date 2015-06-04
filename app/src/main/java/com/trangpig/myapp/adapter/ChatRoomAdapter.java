package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trangpig.myapp.R;

/**
 * Created by TrangPig on 06/03/2015.
 */

public class ChatRoomAdapter extends ArrayAdapter<String> {
    String[] rooms;
    Activity context;
    public ChatRoomAdapter(Activity context) {
        super(context, R.layout.my_item_layout_room);
        this.context = context;
        rooms = context.getResources().getStringArray(R.array.string_array_room);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.my_item_layout_room, null);
        }
        TextView tvRoom;
        if (rooms.length > 0 && position >= 0) {
              tvRoom = (TextView) convertView.findViewById(R.id.tv_item_room);
            tvRoom.setText(rooms[position]);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return rooms.length;
    }
}
