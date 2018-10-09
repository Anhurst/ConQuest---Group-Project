package com.example.group23.quest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class WatchToPhoneService extends Service {

    private GoogleApiClient mWatchApiClient;
    NodeApi.GetConnectedNodesResult nodes;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
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
        //and actually connect it
//        mWatchApiClient.connect();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DEBUG", "STARTING WTPh");
        if(intent == null) {
            Log.d("DEBUG", "INTENT IS NULL");
            return -1;
        }
        String id = Integer.toString(intent.getIntExtra("QUEST_ID", 0));
        String hr = intent.getStringExtra("HR_DATA");
        HeartActivity.heartRateString = "";
        Log.d("HEARTSTRING", HeartActivity.heartRateString);

        if(hr.isEmpty()){
            hr = "0";
        }

        final String msg = id + " " + hr;
        Log.d("DEBUG", "Sent message: " + msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                Log.d("DEBUG", "started thread");
                mWatchApiClient.connect();
                Log.d("DEBUG", "About to call interior sendMessage");
                sendMessage("BACKTOPHONE", msg);
            }
        }).start();

        return START_STICKY;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.d("DEBUG", "getConnectedNodes");
                if (nodes == null) {
                    nodes = Wearable.NodeApi.getConnectedNodes( mWatchApiClient ).await();
                }
                Log.d("DEBUG", "getNodes");
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    Log.d("DEBUG", "before send call");
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mWatchApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                    Log.d("DEBUG", "Done sending");
                }
            }
        }).start();
    }
}