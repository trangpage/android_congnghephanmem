package com.trangpig.myapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.nhuocquy.model.Student;
import com.trangpig.myapp.R;
import com.trangpig.myapp.activity.ShowScoreActivity;
import com.trangpig.myapp.adapter.StudentsForScoreAdapter;
import com.trangpig.until.MyUri;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class SearchScoreFragment extends Fragment {

    public static String SBD_STUDENT = "sbd";
    TableRow tableRow;
    TextView textViewError, textViewSuccess;
    EditText editSearch;
    Button bntSearch;
    List<Student> studentList;
    StudentsForScoreAdapter searchStudentAdapter;
    RestTemplate restTemplate;
    ListView listViewSearch;
    ProgressDialog ringProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search_thong_tin, container, false);
        editSearch = (EditText) v.findViewById(R.id.edit_search);
        bntSearch = (Button) v.findViewById(R.id.bnt_search);
        listViewSearch = (ListView) v.findViewById(R.id.lv_student);
        textViewError = (TextView) v.findViewById(R.id.tv_search_error);
        textViewSuccess = (TextView) v.findViewById(R.id.tv_search_success);
        tableRow = (TableRow) v.findViewById(R.id.title_student);
        tableRow.setVisibility(View.INVISIBLE);
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
                            Log.e("dat ten gi cung dc",String.format(MyUri.URL_FIND_STUDENT, MyUri.IP,params[0]));
                            list = restTemplate.getForObject(String.format(MyUri.URL_FIND_STUDENT, MyUri.IP,params[0]), Student[].class);

                        } catch (RestClientException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (list.length == 0) {
                            textViewError.setText(getResources().getString(R.string.search_error));
                            textViewSuccess.setText("");
                            tableRow.setVisibility(View.INVISIBLE);
                        }else{
                            textViewError.setText("");
                            textViewSuccess.setText(getResources().getString(R.string.search_success));
                            tableRow.setVisibility(View.VISIBLE);

                        studentList = Arrays.asList(list);
                        searchStudentAdapter = new StudentsForScoreAdapter(getActivity(), studentList);
                        listViewSearch.setAdapter(searchStudentAdapter);
                        }
                        ringProgressDialog.dismiss();
                    }
                }.execute( editSearch.getText().toString());
            }
        });

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowScoreActivity.class);
                intent.putExtra(SBD_STUDENT,studentList.get(position).getSBD());
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
