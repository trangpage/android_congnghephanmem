package com.trangpig.until;

import android.content.Context;
import android.content.SharedPreferences;

import com.trangpig.myapp.R;
import com.trangpig.myapp.adapter.MessagesListAdapter;

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

    public static final Map<String,Integer> MAP_ICON_DRABLE = new HashMap<>();
    public static final Map<String,Integer> MAP_ICON_RAW = new HashMap<>();

   static {
       MAP_ICON_DRABLE.put(":)", R.drawable.a1);
       MAP_ICON_DRABLE.put(":(", R.drawable.a2);
       MAP_ICON_DRABLE.put(":'(", R.drawable.a3);
       MAP_ICON_DRABLE.put(":D", R.drawable.a4);
       MAP_ICON_DRABLE.put(":P", R.drawable.a5);
       MAP_ICON_DRABLE.put(":/", R.drawable.a6);
       MAP_ICON_DRABLE.put(":v", R.drawable.a7);

       MAP_ICON_DRABLE.put(";)", R.drawable.icon_smile);
       MAP_ICON_DRABLE.put("^_^", R.drawable.icon_smilea);
       MAP_ICON_DRABLE.put(":O", R.drawable.icon_smilebig);
       MAP_ICON_DRABLE.put("B)", R.drawable.icon_smilelike);
       MAP_ICON_DRABLE.put("O.o", R.drawable.icon_hinha);
       MAP_ICON_DRABLE.put(":*", R.drawable.icon_hinhb);
       MAP_ICON_DRABLE.put("-_-", R.drawable.a7);
       MAP_ICON_DRABLE.put("3:)", R.drawable.a7);

       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "1gif)", R.raw.a1);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "2gif)", R.raw.a2);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "3gif)", R.raw.a3);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "4gif)", R.raw.a4);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "5gif)", R.raw.a5);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "6gif)", R.raw.a6);
       MAP_ICON_DRABLE.put(MessagesListAdapter.CHAR_ZERO + "7gif)", R.raw.a7);

       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "1gif)", R.raw.a1);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "2gif)", R.raw.a2);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "3gif)", R.raw.a3);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "4gif)", R.raw.a4);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "5gif)", R.raw.a5);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "6gif)", R.raw.a6);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "7gif)", R.raw.a7);
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
