package com.trangpig.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nhuocquy.model.Student;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.DisplayInformationActivity;
import com.trangpig.myapp.adapter.CheckRecordAdapter;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by user on 6/26/2015.
 */
public class SearchInforFragment extends Fragment{
    Button bntSearch;
    ListView lvDSSV;
    CheckRecordAdapter checkRecordAdapter;
    List<Student> students;
    RestTemplate restTemplate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.check_record_activity, container,false);
        bntSearch = (Button) v.findViewById(R.id.bntSearch);
        lvDSSV = (ListView) v.findViewById(R.id.lvDSSV);
        checkRecordAdapter = new CheckRecordAdapter(getActivity(),students);
        lvDSSV.setAdapter(checkRecordAdapter);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        bntSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lvDSSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayInformationActivity.class);
                startActivity(intent);

            }
        });
        return v;
    }

}
