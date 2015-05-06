package com.trangpig.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;

import com.trangpig.myapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TrangPig on 04/15/2015.
 */
public class Utils {
    private Context context;
    private SharedPreferences sharedPref;

    private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_SESSION_ID = "sessionId",
            FLAG_MESSAGE = "message";

    public static final Map<String,Integer> MAP_ICON = new HashMap<>();

   static {
       MAP_ICON.put(":)", R.drawable.a1);
       MAP_ICON.put(":(", R.drawable.a2);
       MAP_ICON.put(":'(", R.drawable.a3);
       MAP_ICON.put(":D", R.drawable.a4);
       MAP_ICON.put(":P", R.drawable.a5);
       MAP_ICON.put(":/", R.drawable.a6);
       MAP_ICON.put(":v", R.drawable.a7);
    }

    public Utils(Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public void storeSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getSessionId() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("flag", FLAG_MESSAGE);
            jObj.put("sessionId", getSessionId());
            jObj.put("message", message);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}
