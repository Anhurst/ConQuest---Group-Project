package com.example.group23.quest;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by aaron on 4/25/16.
 */
public class Quest {
    public int questId;
    public String title;
    public String body;
    public String tips;
    public int iconId;
    public ArrayList<DataPoint> heartRateData;

    public Quest(int questId, String title, String body, String tips, int iconId) {
        this.questId = questId;
        this.tips=tips;
        this.title=title;
        this.body=body;
        this.iconId=iconId;
        heartRateData = new ArrayList<DataPoint>();
    }

    /* Takes in a comma delimited string of heart rates, e.g. "72,88,136,112", and loads it into heartRateData */
    // Internal Note! I am using data point count as the X axis. If Shay's heart rate data would make more sense using time as x axis, modify x values
    public void parseHeartRateData(String hrString){
        Log.d("DEBUG", "HRString: " + hrString);
        String[] tokens  = hrString.split(",");
        try {
            for (int i = 0; i < tokens.length; i++) {
                heartRateData.add(new DataPoint(i, Integer.parseInt(tokens[i])));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
