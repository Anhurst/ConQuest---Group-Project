package com.example.group23.quest;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements HeartBeatService.OnChangeListener {
    private int questId;
    private TextView mTextView;
    private ImageButton startQuest;
    private String questTips;
    private int REQUEST_EXIT = 9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try{
            questId = extras.getInt("QUEST_ID");
            String questName = extras.getString("QUEST_NAME");
            questTips = extras.getString("QUEST_TIPS");
            Bitmap bitmap = BitmapFactory.decodeStream(openFileInput("icon"));
            ((ImageView) findViewById(R.id.imageIcon)).setImageBitmap(bitmap);
            ((TextView) findViewById(R.id.questName)).setText(questName);
        } catch (Exception e){
            //e.printStackTrace();
            ((ImageButton) findViewById(R.id.startmainactivity)).setVisibility(View.GONE);
            Drawable logo  = ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo);
            ((RelativeLayout) findViewById(R.id.bg)).setBackground(logo);
            questTips = "";
        }
        startQuest = (ImageButton) findViewById(R.id.startmainactivity);
        startQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InProgressActivity.class);
                HeartActivity.heartRateString = "";
                intent.putExtra("QUEST_ID", questId);
                intent.putExtra("QUEST_TIPS", questTips);
                startActivityForResult(intent, REQUEST_EXIT);
            }
        });

        // Example code to show that the service has been called and connected
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        super.onPause();
//        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    public void onValueChanged(int newValue) {
        Log.d("HEART RATE", Integer.toString(newValue));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}
