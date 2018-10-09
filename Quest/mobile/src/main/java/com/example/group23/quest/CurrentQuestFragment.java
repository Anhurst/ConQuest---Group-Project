package com.example.group23.quest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by aaron on 4/17/16.
 */
public class CurrentQuestFragment extends Fragment {
    ImageView im1,im2,im3,im4,im5;
    LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View v = inflater.inflate(R.layout.current_quest, container, false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        im1 = (ImageView) v.findViewById(R.id.nav_dot_1);
        im2 = (ImageView) v.findViewById(R.id.nav_dot_2);
        im3 = (ImageView) v.findViewById(R.id.nav_dot_3);
        im4 = (ImageView) v.findViewById(R.id.nav_dot_4);
        im5 = (ImageView) v.findViewById(R.id.nav_dot_5);

        QuestCardPagerAdapter adapter = new QuestCardPagerAdapter(getActivity());


        ArrayList<Quest> data = new ArrayList<>();
        ArrayList<Quest> pastQuestData = new ArrayList<>();
        Quest q;

        JSONObject json = MainActivity.json;
        int id;
        String title;
        String description;
        String tip;
        String hrData;
        int picID;
        try {
            String[] types = {"public speaking", "self awareness", "conversations", "completing tasks", "social environments"};
            for (String type : types) {
                JSONArray quests = json.getJSONArray(type);
                for (int i = 0; i < quests.length(); i++) {
                    JSONObject quest = quests.getJSONObject(i);
                    if (quest.getBoolean("complete") == false) {
                        id = quest.getInt("id");
                        title = quest.getString("title");
                        description = quest.getString("description");
                        tip = quest.getString("tip");
                        picID = getPicId(id);
                        q = new Quest(id, title, description, tip, picID);

                        if(data.size()<5) {
                            data.add(q);
                            break;
                        }
                    }
                    else{
                        id = quest.getInt("id");
                        title = quest.getString("title");
                        description = quest.getString("description");
                        tip = quest.getString("tip");
                        picID = getPicId(id);
                        hrData = quest.getString("heart rate");
                        q = new Quest(id, title, description, tip, picID);
                        q.parseHeartRateData(hrData);
                        pastQuestData.add(q);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        viewPager.setAdapter(adapter);

        adapter.setData(data);

        updateNavDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateNavDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Button profileButton = (Button) v.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showProfileFragment("", 0);
            }
        });



        final PastQuestAdapter pastQuestAdapter = new PastQuestAdapter(getActivity());
        final ListView l = (ListView) v.findViewById(R.id.past_quest_list);

        pastQuestAdapter.setData(pastQuestData);
        l.setAdapter(pastQuestAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View alertView1 = mInflater.inflate(R.layout.past_quest_alert, null);

                TextView titleText = (TextView) alertView1.findViewById(R.id.quest_title);

                TextView descriptionText = (TextView) alertView1.findViewById(R.id.quest_body);

                ImageView icon = (ImageView) alertView1.findViewById(R.id.quest_icon);
                final Quest q = (Quest) pastQuestAdapter.getItem(position);

                GraphView g = (GraphView) alertView1.findViewById(R.id.quest_graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(q.heartRateData.toArray(new DataPoint[q.heartRateData.size()]));
                g.addSeries(series);

                Button b = (Button) alertView1.findViewById(R.id.retry_button);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent(getContext(), PhoneToWatchService.class);
                        int questId = q.questId;
                        String questName = q.title;
                        String questTips = q.tips;
                        int questIcon = q.iconId;
                        sendIntent.putExtra("QUEST_ID", questId);
                        sendIntent.putExtra("QUEST_NAME", questName);
                        sendIntent.putExtra("QUEST_TIPS", questTips);
                        sendIntent.putExtra("QUEST_ICON", questIcon);
                        getContext().startService(sendIntent);
                    }
                });

                titleText.setText(q.title);
                descriptionText.setText(q.body);
                icon.setImageResource(q.iconId);
                builder.setView(alertView1);

                final AlertDialog d = builder.create();


                ImageButton close = (ImageButton) alertView1.findViewById(R.id.close_button);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.show();
            }
        });

        return v;
    }


    public void updateNavDots(int pos) {
        im1.setImageResource(R.drawable.circle);
        im2.setImageResource(R.drawable.circle);
        im3.setImageResource(R.drawable.circle);
        im4.setImageResource(R.drawable.circle);
        im5.setImageResource(R.drawable.circle);


        if(pos==0) {
            im1.setImageResource(R.drawable.circle_dgray);
        } else if(pos==1) {
            im2.setImageResource(R.drawable.circle_dgray);
        } else if(pos==2) {
            im3.setImageResource(R.drawable.circle_dgray);
        } else if(pos==3) {
            im4.setImageResource(R.drawable.circle_dgray);
        } else if(pos==4) {
            im5.setImageResource(R.drawable.circle_dgray);
        }
    }

    private String getJSONString(Context context)
    {
        String str = "";
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("files.json");
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str;
    }

    private int getPicId(int questId){
        Resources resources = getActivity().getResources();
        final int resourceId = resources.getIdentifier("questicon" + Integer.toString(questId), "drawable",
                getActivity().getPackageName());
        return resourceId;
    }
}
