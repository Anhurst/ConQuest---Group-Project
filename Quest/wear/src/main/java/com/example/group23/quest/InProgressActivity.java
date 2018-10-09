package com.example.group23.quest;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class InProgressActivity extends Activity implements HeartBeatService.OnChangeListener {

//    private TextView mTextView;
    String questTips;
//    public static String heartRateString = "";
    ArrayList<Integer> heartRates = new ArrayList<Integer>();
//    int currentHeartRate = 0;
    static int questId;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView mTextView;
    private int[] heartArray = {71,72,73,74,75,76,77,78,79, 80, 85, 90};
    public static String heartRateString = "";
    int currentHeartRate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        questTips = extras.getString("QUEST_TIPS");
        questId = extras.getInt("QUEST_ID");

//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });


//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE), SensorManager.SENSOR_DELAY_NORMAL);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyGridPagerAdapter(this, getFragmentManager(), questTips));

//        bindService(new Intent(InProgressActivity.this, HeartBeatService.class), new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder binder) {
//                Log.d("HEART BEAT", "connected to service.");
//                // set our change listener to get change events
//                ((HeartBeatService.HeartbeatServiceBinder) binder).setChangeListener(InProgressActivity.this);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//
//            }
//        }, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onValueChanged(int newValue) {
        Log.d("HEART RATE", Integer.toString(newValue));
        currentHeartRate = newValue;
        heartRates.add(newValue);
    }

    @Override
    public void finish(){
        Log.d("E", "Requesting Exit");
        setResult(RESULT_OK, null);
        super.finish();
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            currentHeartRate = (int)se.values[0];
            Log.d("HEART_RATE_UPDATED", Integer.toString(currentHeartRate));
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
