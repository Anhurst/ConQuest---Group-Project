package com.example.group23.quest;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by aaron on 4/25/16.
 */
public class Achievement {
    public int achId;
    public String title;
    public String body;
    public int iconId;

    public Achievement(int achId, String title, String body, int iconId) {
        this.achId = achId;
        this.title=title;
        this.body=body;
        this.iconId=iconId;
    }
}
