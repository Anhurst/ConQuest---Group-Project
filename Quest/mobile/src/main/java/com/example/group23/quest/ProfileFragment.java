package com.example.group23.quest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by aaron on 4/24/16.
 */
public class ProfileFragment extends Fragment {
    LayoutInflater inflater;
    private BroadcastReceiver receiver;

    public Achievement a1 =
            new Achievement(1,"First Words","Congratulations you have finished your first Public Speaking Quest!", R.drawable.achievement1);
    public Achievement a2 =
            new Achievement(2, "Getting to Know You","Congratulations you have finished your first Self Awareness Quest",R.drawable.achievement2);
    public Achievement a3 =
            new Achievement(3, "Talk to Me","Congratulations you have finished your first Conversations Quest!",R.drawable.achievement3);
    public Achievement a4 =
            new Achievement(4, "Just Do It","Congratulations you have finished your first Completing Tasks Quest!",R.drawable.achievement4);
    public Achievement a5 =
            new Achievement(5, "Welcome to the Jungle","Congratulations you have finished your first Social Environments Quest!",R.drawable.achievement5);
    public Achievement a6 =
            new Achievement(6, "Speaker Extraordinaire","Congratulations you have finished 4 Public Speaking Quests!", R.drawable.achievement6);
    public Achievement a7 =
            new Achievement(7, "Introspection Guru","Congratulations you have finished 4 Self Awareness Quests!",R.drawable.achievement7);
    public Achievement a8 =
            new Achievement(8,"Conversation Chief","Congratulations you have finished 4 Conversations Quests!",R.drawable.achievement8);
    public Achievement a9 =
            new Achievement(9, "Task Master","Congratulations you have finished 4 Completing Tasks Quests!",R.drawable.achievement9);
    public Achievement a10 =
            new Achievement(10,"Domain Champion","Congratulations you have finished 4 Social Environments Quests!", R.drawable.achievement10);


    public boolean updating = false;

    public int currentExp = 0;
    public int nextExp = 100;
    public int level = 1;
    public String currentTitle = "Social Novice";
    public String nextTitle = "Socialite Trainee";
    public boolean a1locked = true;
    public boolean a2locked = true;
    public boolean a3locked = true;
    public boolean a4locked = true;
    public boolean a5locked = true;
    public boolean a6locked = true;
    public boolean a7locked = true;
    public boolean a8locked = true;
    public boolean a9locked = true;
    public boolean a10locked = true;
    public String completedCategory;

    public int bar1percent = 1;
    public int bar2percent = 1;
    public int bar3percent = 1;
    public int bar4percent = 1;
    public int bar5percent = 1;

    public int c1 = 0;
    public int c2 = 0;
    public int c3 = 0;
    public int c4 = 0;
    public int c5 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);
        this.inflater=inflater;

        TextView levelView = (TextView) v.findViewById(R.id.level_text);
        levelView.setText(Integer.toString(level));

        TextView expAmount = (TextView) v.findViewById(R.id.level_exp);
        expAmount.setText(Integer.toString(currentExp)+"/"+Integer.toString(nextExp));

        TextView levelTitle = (TextView) v.findViewById(R.id.level_title);
        levelTitle.setText(currentTitle);

        View expBar = v.findViewById(R.id.exp_bar_foreground);

        ViewGroup.LayoutParams expBarLayoutParams = expBar.getLayoutParams();
        int expBarH = expBarLayoutParams.height;
        int expBarW = (expBarLayoutParams.width * currentExp) / nextExp;

        expBar.setLayoutParams(new RelativeLayout.LayoutParams(expBarW, expBarH));

        View bar1 = v.findViewById(R.id.bar1);
        View bar2 = v.findViewById(R.id.bar2);
        View bar3 = v.findViewById(R.id.bar3);
        View bar4 = v.findViewById(R.id.bar4);
        View bar5 = v.findViewById(R.id.bar5);

        //setting the height of the bars
        ViewGroup.LayoutParams p1 = bar1.getLayoutParams();
        int bar1w = p1.width;
        int bar1h = (p1.height * bar1percent)/100;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bar1w, bar1h);
        bar1.setLayoutParams(params);

        ViewGroup.LayoutParams p2 = bar2.getLayoutParams();
        int bar2w = p2.width;
        int bar2h = (p2.height * bar2percent)/100;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(bar2w, bar2h);
        bar2.setLayoutParams(params2);

        ViewGroup.LayoutParams p3 = bar3.getLayoutParams();
        int bar3w = p3.width;
        int bar3h = (p3.height * bar3percent)/100;
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(bar3w, bar3h);
        bar3.setLayoutParams(params3);

        ViewGroup.LayoutParams p4 = bar4.getLayoutParams();
        int bar4w = p4.width;
        int bar4h = (p4.height * bar4percent)/100;
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(bar4w, bar4h);
        bar4.setLayoutParams(params4);

        ViewGroup.LayoutParams p5 = bar5.getLayoutParams();
        int bar5w = p5.width;
        int bar5h = (p5.height * bar5percent)/100;
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(bar5w, bar5h);
        bar5.setLayoutParams(params5);

        Button profileButton = (Button) v.findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showProfileFragment("", 0);
            }
        });


        Button questButton = (Button) v.findViewById(R.id.quest_button);
        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showCurrentQuestFragment();
            }
        });

        Button achievement1 = (Button) v.findViewById(R.id.achievement1);

        if(!a1locked) {
            achievement1.setBackgroundResource(a1.iconId);
            achievement1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a1);
                }
            });
        }

        if(!a2locked) {
            Button achievement2 = (Button) v.findViewById(R.id.achievement2);
            achievement2.setBackgroundResource(a2.iconId);
            achievement2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a2);
                }
            });
        }

        if(!a3locked) {
            Button achievement3 = (Button) v.findViewById(R.id.achievement3);
            achievement3.setBackgroundResource(a3.iconId);
            achievement3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a3);
                }
            });
        }

        if(!a4locked) {
            Button achievement4 = (Button) v.findViewById(R.id.achievement4);
            achievement4.setBackgroundResource(a4.iconId);
            achievement4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a4);
                }
            });
        }

        if(!a5locked) {
            Button achievement5 = (Button) v.findViewById(R.id.achievement5);
            achievement5.setBackgroundResource(a5.iconId);
            achievement5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a5);
                }
            });
        }

        if(!a6locked) {
            Button achievement6 = (Button) v.findViewById(R.id.achievement6);
            achievement6.setBackgroundResource(a6.iconId);
            achievement6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a6);
                }
            });
        }

        if(!a7locked) {
            Button achievement7 = (Button) v.findViewById(R.id.achievement7);
            achievement7.setBackgroundResource(a7.iconId);
            achievement7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a7);
                }
            });
        }

        if(!a8locked) {
            Button achievement8 = (Button) v.findViewById(R.id.achievement8);
            achievement8.setBackgroundResource(a8.iconId);
            achievement8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a8);
                }
            });
        }

        if(!a9locked) {
            Button achievement9 = (Button) v.findViewById(R.id.achievement9);
            achievement9.setBackgroundResource(a9.iconId);
            achievement9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a9);
                }
            });
        }

        if(!a10locked) {
            Button achievement10 = (Button) v.findViewById(R.id.achievement10);
            achievement10.setBackgroundResource(a10.iconId);
            achievement10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(a10);
                }
            });
        }

        return v;
    }


    private void showDialog(Achievement a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View alertView1 = inflater.inflate(R.layout.achievement_alert, null);
        ImageButton close = (ImageButton) alertView1.findViewById(R.id.close_button);

        TextView titleText = (TextView) alertView1.findViewById(R.id.achievement_title);
        TextView bodyText = (TextView) alertView1.findViewById(R.id.achievement_body);
        ImageView icon = (ImageView) alertView1.findViewById(R.id.achievement_icon);

        titleText.setText(a.title);
        bodyText.setText(a.body);
        icon.setImageResource(a.iconId);

        builder.setView(alertView1);

        final AlertDialog d = builder.create();
        d.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void updateExp(int change) {
        View v = getView();

        a3locked = false;
        currentExp += change;
        if (currentExp >= nextExp) {
            currentExp -= nextExp;
            nextExp *= 2;
            level += 1;
            currentTitle = nextTitle;

            if (v == null) {
                return;
            }


            TextView levelView = (TextView) v.findViewById(R.id.level_text);
            levelView.setText(Integer.toString(level));

            TextView expAmount = (TextView) v.findViewById(R.id.level_exp);
            expAmount.setText(Integer.toString(currentExp)+"/"+Integer.toString(nextExp));

            TextView levelTitle = (TextView) v.findViewById(R.id.level_title);
            levelTitle.setText(currentTitle);

        }
        if (v == null) {
            return;
        }

        TextView expAmount = (TextView) v.findViewById(R.id.level_exp);
        expAmount.setText(Integer.toString(currentExp)+"/"+Integer.toString(nextExp));

        TextView levelTitle = (TextView) v.findViewById(R.id.level_title);
        levelTitle.setText(currentTitle);

        View expBar = v.findViewById(R.id.exp_bar_foreground);

        ViewGroup.LayoutParams expBarLayoutParams = expBar.getLayoutParams();
        int expBarH = expBarLayoutParams.height;
        int expBarW = (expBarLayoutParams.width * currentExp) / nextExp;

        expBar.setLayoutParams(new RelativeLayout.LayoutParams(expBarW, expBarH));

    }
}
