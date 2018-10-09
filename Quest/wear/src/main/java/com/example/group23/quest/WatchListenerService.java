package com.example.group23.quest;

import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import java.util.concurrent.TimeUnit;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


public class WatchListenerService extends WearableListenerService {
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
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.w("T", "Data Received on watch.");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals("/Quest")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                int questId = dataMapItem.getDataMap().getInt("QuestID");
                String questName = dataMapItem.getDataMap().getString("QuestName");
                String questTips = dataMapItem.getDataMap().getString("QuestTips");
                Asset pictureAsset = dataMapItem.getDataMap().getAsset("Icon");
                Bitmap bitmap = loadBitmapFromAsset(pictureAsset);
                bitmap = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - bitmap.getWidth()) / 3, bitmap.getWidth(), bitmap.getWidth());
                String fileName = "icon";
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                try {
                    FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("QUEST_ID", questId);
                intent.putExtra("QUEST_NAME", questName);
                intent.putExtra("QUEST_TIPS", questTips);
                startActivity(intent);
            }
        }
    }

    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mApiClient.blockingConnect(3000, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            Log.w("Error", "Result not Successful");
            return null;
        }
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mApiClient, asset).await().getInputStream();
        mApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("T", "Requested an unknown Asset.");
            return null;
        }
        return BitmapFactory.decodeStream(assetInputStream);
    }
}