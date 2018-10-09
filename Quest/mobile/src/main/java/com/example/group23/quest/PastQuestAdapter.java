package com.example.group23.quest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by aaron on 2/29/16.
 */

class QuestHolder {
    TextView t;
    ImageView i;
}


public class PastQuestAdapter extends BaseAdapter {
    List<Quest> data;
    private LayoutInflater mInflater;
    private Context context;

    public PastQuestAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void setData(List<Quest> d) {
        data = d;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(data == null) return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if(data == null) return null;
        if(data.size()==0) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        QuestHolder holder;

        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.past_quest_cell, null);
            holder = new QuestHolder();
            holder.t  = (TextView) convertView.findViewById(R.id.quest_title);
            holder.i = (ImageView) convertView.findViewById(R.id.quest_icon);
            convertView.setTag(holder);
        } else {
            holder = (QuestHolder) convertView.getTag();
        }
        Quest q = data.get(position);
        holder.t.setText(q.title);
        holder.i.setImageResource(q.iconId);
        return convertView;
    }



}


