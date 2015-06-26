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
 * Created by user on 6/26/2015.
 */
public class CheckRecordAdapter extends ArrayAdapter<Student>{
    Activity context;
    List<Student> students;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public CheckRecordAdapter(Activity context, List<Student> students) {
        super(context, R.layout.check_record_adapter, students);
        this.context = context;
        this.students = students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = context.getLayoutInflater().inflate(R.layout.check_record_adapter, parent);

        TextView sBD, name, birthday, birthAdress;
        if (students.size() > 0 && position >= 0) {
        sBD = (TextView) convertView.findViewById(R.id.tvSBD);
            sBD.setText(students.get(position).getSBD());
        name = (TextView) convertView.findViewById(R.id.tvTenTS);
            name.setText(students.get(position).getName());
        birthday = (TextView) convertView.findViewById(R.id.tvNgaySinh);

            birthAdress = (TextView) convertView.findViewById(R.id.tvNoiSinh);
            birthAdress.setText(students.get(position).getAddress());
        }
        return convertView;
    }
    @Override
    public int getCount() {
        return students.size();

}
}
