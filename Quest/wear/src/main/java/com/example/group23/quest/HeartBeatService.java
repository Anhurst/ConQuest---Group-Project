package com.example.group23.quest;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.util.List;

/**
 * Created by ShayanthS on 4/20/16.
 */
public class HeartBeatService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private int currentValue=0;
    private static final String LOG_TAG = "MyHeart";
    private IBinder binder = new HeartbeatServiceBinder();
    private OnChangeListener onChangeListener;
    private GoogleApiClient mGoogleApiClient;

    public interface OnChangeListener {
        void onValueChanged(int newValue);
    }

    public class HeartbeatServiceBinder extends Binder {
        public void setChangeListener(OnChangeListener listener) {
            onChangeListener = listener;
            // return currently known value
            listener.onValueChanged(currentValue);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mHearRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        boolean res = mSensorManager.registerListener(this, mHearRateSensor, SensorManager.SENSOR_DELAY_UI);
        Log.d(LOG_TAG, " sensor registered: " + (res ? "yes" : "no"));

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, " sensor unregistered");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // is this a heartbeat event and does it have data?
        if(sensorEvent.sensor.getType()==Sensor.TYPE_HEART_RATE && sensorEvent.values.length>0 ) {
            int newValue = Math.round(sensorEvent.values[0]);
            Log.d("NEW SENSOR VALUE", Integer.toString(newValue));
            if(currentValue != newValue && newValue!=0) {
                currentValue = newValue;
                if(onChangeListener!=null) {
                    Log.d(LOG_TAG,"sending new value to listener: " + newValue);
                    onChangeListener.onValueChanged(newValue);
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
