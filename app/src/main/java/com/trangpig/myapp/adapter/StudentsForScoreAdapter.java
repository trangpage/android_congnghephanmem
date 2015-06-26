package com.trangpig.myapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nhuocquy.model.Student;
import com.trangpig.myapp.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by TrangPig on 06/26/2015.
 */
public class StudentsForScoreAdapter extends ArrayAdapter<String> {

    List<Student> studentList;
    private Activity context;
    private SimpleDateFormat f;
    private TextView tvSBD, tvName, tvNgaySinh, tvNoiSinh;
    public StudentsForScoreAdapter(Activity context, List<Student> studentList) {
        super(context, R.layout.my_item_layout_search);
        this.context = context;
        this.studentList = studentList;
        f = new SimpleDateFormat("dd/MM/yyyy");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.my_item_layout_search, null);
        }
        if (studentList.size() > 0 && position >= 0) {
            tvSBD = (TextView) convertView.findViewById(R.id.tv_search_sbd);
            tvName = (TextView) convertView.findViewById(R.id.tv_search_ten);
            tvNgaySinh = (TextView) convertView.findViewById(R.id.tv_search_ngay_sinh);
            tvNoiSinh = (TextView) convertView.findViewById(R.id.tv_search_noi_sinh);
            //
            tvSBD.setText(studentList.get(position).getSBD());
            tvName.setText(studentList.get(position).getName());
            tvNgaySinh.setText(f.format(studentList.get(position).getBirthday()));
            tvNoiSinh.setText(studentList.get(position).getAddress());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

}
