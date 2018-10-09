package com.example.group23.quest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 4/19/2016.
 */
public class MyGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private List mRows;
    private String tipsText;
    private Bundle b;

    public MyGridPagerAdapter(Context ctx, FragmentManager fm, String tips) {
        super(fm);
        mContext = ctx;
        tipsText = tips;
    }

    public int getColumnCount(int row) {
        return 3;
    }

    public int getRowCount() {
        return 1;
    }


    @Override
    public Fragment getFragment(int row, int col) {
        Fragment fragment = null;
        switch (col) {
            case 0:
                fragment = InProgressFragment.newInstance(tipsText);
                break;
            case 1:
                fragment = TipsFragment.newInstance(tipsText);
                break;
            case 2:
                fragment = HeartFragment.newInstance();
                break;
        }
        return fragment;
    }
}
