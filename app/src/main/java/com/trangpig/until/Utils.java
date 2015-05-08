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

    public static final Map<String,Integer> MAP_ICON_RAWS = new HashMap<>();
    public static final Map<String,Integer> MAP_ICON_RAW = new HashMap<>();
    public static final Map<String,Integer> MAP_ICON_RAW1 = new HashMap<>();

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

       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a1gif)", R.raw.a1);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a2gif)", R.raw.a2);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a3gif)", R.raw.a3);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a4gif)", R.raw.a4);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a5gif)", R.raw.a5);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a6gif)", R.raw.a6);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a7gif)", R.raw.a7);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a8gif)", R.raw.a8);
       MAP_ICON_RAW1.put(MessagesListAdapter.CHAR_ZERO + "a9gif)", R.raw.a9);
       MAP_ICON_RAWS.putAll(MAP_ICON_RAW1);

       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b1gif)", R.raw.b1);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b2gif)", R.raw.b2);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b3gif)", R.raw.b3);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b4gif)", R.raw.b4);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b5gif)", R.raw.b5);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b6gif)", R.raw.b6);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b7gif)", R.raw.b7);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b8gif)", R.raw.b8);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b9gif)", R.raw.b9);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b10gif)", R.raw.b10);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b11gif)", R.raw.b11);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b12gif)", R.raw.b12);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b13gif)", R.raw.b13);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b14gif)", R.raw.b14);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b15gif)", R.raw.b15);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b16gif)", R.raw.b16);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b17gif)", R.raw.b17);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b18gif)", R.raw.b18);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b19gif)", R.raw.b19);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b20gif)", R.raw.b20);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b21gif)", R.raw.b21);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b22gif)", R.raw.b22);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b23gif)", R.raw.b23);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b24gif)", R.raw.b24);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b25gif)", R.raw.b25);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b26gif)", R.raw.b26);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b27gif)", R.raw.b27);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b28gif)", R.raw.b28);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b29gif)", R.raw.b29);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b30gif)", R.raw.b30);
       MAP_ICON_RAW.put(MessagesListAdapter.CHAR_ZERO + "b31gif)", R.raw.b31);
       MAP_ICON_RAWS.putAll(MAP_ICON_RAW);

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
