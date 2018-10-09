package com.example.group23.quest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static JSONObject json = null;
    private BroadcastReceiver receiver;
    static ProfileFragment profile;
    static CurrentQuestFragment quests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = getWindow().getDecorView().getRootView();
        profile = new ProfileFragment();
        quests = new CurrentQuestFragment();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("T", "Broadcast received.");
                Bundle extras = intent.getExtras();
                String category = extras.getString("COMPLETED_QUEST_CATEGORY");
                int experience = 20;
                showProfileFragment(category, experience);
                showQuestCompleteAlert();

            }
        };

        try {
            json = new JSONObject(getJSONString(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showCurrentQuestFragment();
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void showQuestCompleteAlert(){
        AnimationDrawable completeAnimation;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertView = inflater.inflate(R.layout.quest_complete_alert, null);
        ImageView animationView = (ImageView) alertView.findViewById(R.id.animationView);
        animationView.setBackgroundResource(R.drawable.quest_complete);
        completeAnimation = (AnimationDrawable) animationView.getBackground();

        builder.setView(alertView);
        final AlertDialog d = builder.create();

        d.show();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        completeAnimation.start();
    }

    public void showProfileFragment(String category, int experience) {

        if(profile.updating) {
            //profile.a3locked = false;
            profile.currentExp += experience;
            profile.completedCategory = category;
            if (profile.currentExp >= profile.nextExp) {
                profile.currentExp -= profile.nextExp;
                profile.nextExp *= 2;
                profile.level += 1;
                profile.currentTitle = profile.nextTitle;
            }

            if(!category.isEmpty()){
                if (category == "public speaking"){
                    profile.bar1percent += 15;
                    profile.c1 += 1;
                } else if( category == "self awareness"){
                    profile.bar2percent += 15;
                    profile.c2 += 1;
                } else if( category == "conversations"){
                    profile.bar3percent += 15;
                    profile.c3 += 1;
                } else if (category == "completing tasks"){
                    profile.bar4percent += 15;
                    profile.c4 += 1;
                } else{
                    profile.bar5percent += 15;
                    profile.c5 += 1;
                }
            }

            if (profile.c1 >= 1){
                profile.a1locked = false;
            }
            if (profile.c2 >= 1){
                profile.a5locked = false;
            }
            if (profile.c3 >= 1){
                profile.a4locked = false;
            }
            if (profile.c4 >= 1){
                profile.a2locked = false;
            }
            if (profile.c5 >= 1){
                profile.a3locked = false;
            }
            if (profile.c1 >= 4){
                profile.a6locked = false;
            }
            if (profile.c2 >= 4){
                profile.a7locked = false;
            }
            if (profile.c3 >= 4){
                profile.a8locked = false;
            }
            if (profile.c4 >= 4){
                profile.a10locked = false;
            }
            if (profile.c5 >= 4){
                profile.a9locked = false;
            }

            profile.updating = false;
        }
        //MainActivity.profileFragment.updateExp(70);



        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profile).addToBackStack("abc").commit();
    }

    public void showCurrentQuestFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, quests).addToBackStack("abc").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter("QuestComplete")
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

}
