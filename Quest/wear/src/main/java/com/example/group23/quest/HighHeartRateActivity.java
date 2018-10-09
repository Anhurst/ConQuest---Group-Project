package com.example.group23.quest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HighHeartRateActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_heart_rate);
        Intent newIntent = getIntent();
        String highHeartRate = newIntent.getStringExtra("High_Heart_Rate");
        Log.d("HIGH HEART RATE", highHeartRate);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });


        ImageButton startQuest = (ImageButton) findViewById(R.id.sendToastBack);
        startQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WatchToPhoneService.class);
                startService(intent);
            }
        });
    }
}
