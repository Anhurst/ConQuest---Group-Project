package com.example.group23.quest;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.PutDataMapRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;


public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;
    @Override
    public void onCreate() {
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        final int questId = extras.getInt("QUEST_ID");
        final String questName = extras.getString("QUEST_NAME");
        final String questTips = extras.getString("QUEST_TIPS");
        final int questIcon = extras.getInt("QUEST_ICON");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mApiClient.connect();
                sendData(questId, questName, questTips, questIcon);
            }
        }).start();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendData(final int questId, final String questName, final String questTips, final int questIcon){
        new Thread( new Runnable() {
            @Override
            public void run() {
                PutDataMapRequest dataMap = PutDataMapRequest.create("/Quest");
                dataMap.getDataMap().putInt("QuestID", questId);
                dataMap.getDataMap().putString("QuestName", questName);
                dataMap.getDataMap().putString("QuestTips", questTips);
                dataMap.getDataMap().putLong("Time", new Date().getTime());
                try {

                    //int imageResource = R.drawable.example_icon;
                    Drawable image = getDrawable(questIcon);
                    //InputStream bm = getBaseContext().getAssets().open("example_icon.png");
                    Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                    if (bitmap == null){
                        Log.w("T", "Bitmap is null in service");
                    }
                    Asset asset = createAssetFromBitmap(bitmap);
                    dataMap.getDataMap().putAsset("Icon", asset);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                PutDataRequest request = dataMap.asPutDataRequest();
                Log.w("T", "Sending Request");
                Wearable.DataApi.putDataItem(mApiClient, request);
            }
        }).start();
    }

    /* Creates Asset object from bitmap. Code from http://developer.android.com/training/wearables/data-layer/assets.html */
    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}