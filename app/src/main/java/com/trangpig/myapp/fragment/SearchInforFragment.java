package com.trangpig.myapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.nhuocquy.model.Student;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.DisplayInformationActivity;
import com.trangpig.myapp.adapter.CheckRecordAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 6/26/2015.
 */
public class SearchInforFragment extends Fragment{
    EditText editSearch;
    Button bntSearch;
    ListView lvDSSV;
    CheckRecordAdapter checkRecordAdapter;
    List<Student> students;
    RestTemplate restTemplate;
    ProgressDialog ringProgressDialog;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.check_record_activity, container,false);
        editSearch = (EditText) v.findViewById(R.id.editSearch);
        bntSearch = (Button) v.findViewById(R.id.bntSearch);
        lvDSSV = (ListView) v.findViewById(R.id.lvDSSV);
        students = new ArrayList<>();

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        bntSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, Void>() {
                    Student[] list;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ringProgressDialog = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.wait), getActivity().getResources().getString(R.string.conecting), true);
                    }

                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            Log.e("dat ten gi cung dc", String.format(MyUri.URL_FIND_STUDENT, MyUri.IP, params[0]));
                            list = restTemplate.getForObject(String.format(MyUri.URL_FIND_STUDENT, MyUri.IP, params[0]), Student[].class);

                        } catch (RestClientException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (list == null) {

                        } else {
                            students = Arrays.asList(list);
                            checkRecordAdapter = new CheckRecordAdapter(getActivity(), students);
                            lvDSSV.setAdapter(checkRecordAdapter);
                            checkRecordAdapter.notifyDataSetChanged();
                            ringProgressDialog.dismiss();
                        }
                    }
                }.execute(editSearch.getText().toString());
            }
        });
        lvDSSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DisplayInformationActivity.class);
                intent.putExtra(SearchScoreFragment.SBD_STUDENT,students.get(position).getSBD().toString());
                startActivity(intent);

            }
        });
        return v;
    }

}
