package com.trangpig.myinterface;

import com.trangpig.myapp.activity.IconActivity;

import java.util.Map;

/**
 * Created by NhuocQuy on 5/8/2015.
 */
public interface SmileFragment {
    public void setParentActivity(IconActivity parent);
    public void setListIcon(Map<String,Integer> mapIcon);
}
