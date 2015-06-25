package com.trangpig.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TrangPig on 04/22/2015.
 */
public class Data {
    private Map<String, Object> data = new HashMap<>();
    private static Data instance = new Data();
    //
    public static final String ACOUNT = "account";
    public static final String IMAGE_CACHE = "imageCache";
    public static final String ID_CON = "idCon";
   public Object getAttribute(String key){
       return data.get(key);
   }

    public void setAttribute(String key, Object value){
        data.put(key,value);
    }

    public static Data getInstance(){
        return instance;
    }
    private Data(){
        setAttribute(ID_CON, -1l);
    }
}
