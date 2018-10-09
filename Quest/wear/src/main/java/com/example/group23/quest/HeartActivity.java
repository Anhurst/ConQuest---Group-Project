package com.example.group23.quest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HeartActivity extends Activity implements HeartBeatService.OnChangeListener {

    private TextView mTextView;
    public static String heartRateString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });

        ImageButton startQuest = (ImageButton) findViewById(R.id.startmainactivity);
        startQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InProgressActivity.class); //change this to watchtophoneservice
                startActivity(intent);
            }
        });
    }

    @Override
    public void onValueChanged(int newValue) {
        Log.d("HEART RATE", Integer.toString(newValue));
        if (newValue > 90){
            Intent newIntent = new Intent(HeartActivity.this, HighHeartRateActivity.class);
            newIntent.putExtra("High_Heart_Rate", newValue);
            startActivity(newIntent);
        }
    }

}
