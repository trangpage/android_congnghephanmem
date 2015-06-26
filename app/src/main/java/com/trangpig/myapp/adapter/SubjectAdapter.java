package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhuocquy.model.Subject;
import com.trangpig.myapp.R;

import java.util.List;

/**
 * Created by TrangPig on 06/26/2015.
 */
public class SubjectAdapter extends ArrayAdapter<String> {
    TextView tvName,tvScorre;
    Activity context;
    List<Subject> subjectList;
    public SubjectAdapter(Activity context, List<Subject> subjects) {
        super(context, R.layout.my_item_layout_subjects );
        this.subjectList = subjects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.my_item_layout_subjects, null);
        }
        if (subjectList.size() > 0 && position >= 0) {
            tvName = (TextView) convertView.findViewById(R.id.tv_subject_name);
            tvScorre = (TextView) convertView.findViewById(R.id.tv_subject_score);
            //
            tvName.setText(subjectList.get(position).getSubject());
            tvScorre.setText(String.valueOf(subjectList.get(position).getScore()));
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }
}
