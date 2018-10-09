package com.example.group23.quest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aaron on 4/25/16.
 */
public class QuestCardPagerAdapter extends PagerAdapter
{
    private Context mContext;
    private List<Quest> data;


    public QuestCardPagerAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Quest> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.current_quest_card, collection, false);

        final Quest q = data.get(position);

        TextView title = (TextView) layout.findViewById(R.id.quest_title);
        TextView body = (TextView) layout.findViewById(R.id.quest_body);
        TextView tips = (TextView) layout.findViewById(R.id.quest_tips);
        ImageView icon = (ImageView) layout.findViewById(R.id.quest_icon);
        Button startButton = (Button) layout.findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(mContext, PhoneToWatchService.class);
                int questId = q.questId;
                String questName = q.title;
                String questTips = q.tips;
                int questIcon = q.iconId;
                sendIntent.putExtra("QUEST_ID", questId);
                sendIntent.putExtra("QUEST_NAME", questName);
                sendIntent.putExtra("QUEST_TIPS", questTips);
                sendIntent.putExtra("QUEST_ICON", questIcon);
                mContext.startService(sendIntent);
            }
        });


        title.setText(q.title);
        body.setText(q.body);
        tips.setText(q.tips);
        icon.setImageResource(q.iconId);


        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
