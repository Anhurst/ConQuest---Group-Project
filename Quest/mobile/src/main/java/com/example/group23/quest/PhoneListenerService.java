package com.example.group23.quest;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String DETAILED_VIEW = "/Select";
    private static final String NEW_LOCATION = "/Random";
    LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase("BACKTOPHONE") ) {
            String s = new String(messageEvent.getData());
            Log.d("DEBUG", "Message received from watch: " + s);
            String[] res = s.split(" ");
            JSONObject json = new JSONObject();
            String hr; //JSONArray hr;// = new JSONArray();
            int questId;
            String category = "";
            try {
                questId = Integer.parseInt(res[0]);
                hr = res[1]; //new JSONArray("[" + res[1] + "]");
                json = MainActivity.json; //new JSONObject(getJSONString(getApplicationContext()));
                String[] types = {"public speaking", "self awareness", "conversations", "completing tasks", "social environments"};
                boolean breakflag = false;
                for (String type : types) {
                    JSONArray quests = json.getJSONArray(type);
                    for (int i = 0; i < quests.length(); i++) {
                        JSONObject quest = quests.getJSONObject(i);
                        if (quest.getInt("id") == questId) {
                            Log.d("PHONELISTENER", "Found and updated hr");
                            quest.put("heart rate", hr);
                            quest.put("complete", true);
                            category = type;

                            MainActivity.profile.updating = true;
//                            MainActivity.profileFragment.updateExp(70);

                            breakflag = true;
                            break;
                        }
                    }
                    if (breakflag) {
                        break;
                    }
                }

                Intent intent = new Intent("QuestComplete");
                intent.putExtra("COMPLETED_QUEST_CATEGORY", category);
                broadcaster.sendBroadcast(intent);
                Log.d("T", "Broadcast sent.");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    private String getJSONString(Context context)
    {
        String str = "";
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("files.json");
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str;
    }
}
