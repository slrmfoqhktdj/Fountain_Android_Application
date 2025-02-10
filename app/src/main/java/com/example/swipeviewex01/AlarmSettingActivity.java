package com.example.swipeviewex01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;



public class AlarmSettingActivity extends AppCompatActivity {


    private CharSequence[] items = {"기본","짧게","길게","연속"};
    private CharSequence[] items1;
    private final long[] pattern = {0,500,200,500,200,1000};
    private final int[] amplitude = {0,100,0,100,0,150};


    private SwitchCompat soundswitchBtn;
    private SwitchCompat vibeswitchBtn;
    private SwitchCompat showswitchBtn;
    private TextView vibepatternTv;
    private TextView vibepatternTvsm;

    private SharedPreferencesAdapter spa;

    private AlertDialogAdapter ada;

    private boolean vibepatternTvSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //테마
        spa = SharedPreferencesAdapter.getInstance();
        String theme = getIntent().getStringExtra("theme");
        ThemeAdapter ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this, theme);

        setContentView(R.layout.activity_alarmsetting);


        // 화면상태 켜짐
//        spa.setItem("appstate","1");


        LinearLayout title = findViewById(R.id.alramsetting_title);
        SwitchCompat existswitchBtn = findViewById(R.id.alarmsetting_existswitchbtn);
        soundswitchBtn = findViewById(R.id.alarmsetting_soundswitchbtn);
        vibeswitchBtn = findViewById(R.id.alarmsetting_vibeswitchbtn);
        showswitchBtn = findViewById(R.id.alarmsetting_showswitchbtn);
        vibepatternTv = findViewById(R.id.alarmsetting_vibepatternTv);
        vibepatternTvsm = findViewById(R.id.alarmsetting_vibepatternTv_sm);

        //테마
        ta.ActivityThemeSelector(this, theme, title);

        String alarmexist = spa.getItem("alarmexist");
        String alarmsound = spa.getItem("alarmsound");
        String alarmvibe = spa.getItem("alarmvibe");
        String alarmshow = spa.getItem("alarmshow");

        ada = new AlertDialogAdapter(AlarmSettingActivity.this, theme);

        if(theme.equals("1")){
            AlertDialogList();
            items = items1;
        }

        if(!alarmexist.equals("0")){
            existswitchBtn.setChecked(true);
        }

        if(!alarmsound.equals("0")){
           soundswitchBtn.setChecked(true);
        }

        if(!alarmvibe.equals("0")){
            vibeswitchBtn.setChecked(true);
            vibepatternTvSelector = true;
            vibepatternTv.setTextColor(Color.parseColor("#35ACD6"));
        }else{
            vibeswitchBtn.setChecked(false);
            vibepatternTvSelector = false;
            vibepatternTv.setTextColor(Color.parseColor("#FFC5C5C5"));
        }

        if(!alarmshow.equals("0")){
            showswitchBtn.setChecked(true);
        }

        switch (alarmvibe){
            case "1": vibepatternTvsm.setText(items[0]);break;
            case "2": vibepatternTvsm.setText(items[1]);break;
            case "3": vibepatternTvsm.setText(items[2]);break;
            case "4": vibepatternTvsm.setText(items[3]);break;
        }



        existswitchBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                spa.setItem("alarmexist","1");
                sendOption(4);
            }else{
                spa.setItem("alarmexist","0");
                soundswitchBtn.setChecked(false);
                vibeswitchBtn.setChecked(false);
                showswitchBtn.setChecked(false);
                spa.setItem("alarmsound","0");
                spa.setItem("alarmvibe","0");
                spa.setItem("alarmshow","0");
                sendOption(5);
            }
        });

        soundswitchBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                spa.setItem("alarmsound","1");
            }else{
                spa.setItem("alarmsound","0");
            }
            sendOption(2);
        });

        vibeswitchBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                spa.setItem("alarmvibe","1");
                vibepatternTvSelector = true;
                vibepatternTv.setTextColor(Color.parseColor("#35ACD6"));
                vibepatternTvsm.setText(items[0]);
            }else{
                spa.setItem("alarmvibe","0");
                vibepatternTvSelector = false;
                vibepatternTv.setTextColor(Color.parseColor("#FFC5C5C5"));
                vibepatternTvsm.setText("");
            }
            sendOption(3);
        });

        showswitchBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                spa.setItem("alarmshow","1");
            }else{
                spa.setItem("alarmshow","0");
            }
            sendOption(1);
        });

        vibepatternTv.setOnClickListener(v -> {
            if(vibepatternTvSelector){
                Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                ada.AlarmSettingVibePattern(vibepatternTvsm,vibrator,items,pattern,amplitude);
            }else{
                ada.AlarmSettingVibeOn(vibeswitchBtn,vibepatternTv,vibepatternTvsm,items[0]);
            }
        });


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        spa.setItem("appstate","0");

        ada = null;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        spa.setItem("pwrock","0");
        finish();
    }
    protected void sendOption(int o){
        Intent intent = new Intent("notialram");
        intent.putExtra("o",o);
        sendBroadcast(intent);
    }

    protected void AlertDialogList(){
        SpannableString sp1 = new SpannableString("기본");
        sp1.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp2 = new SpannableString("짧게");
        sp2.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp3 = new SpannableString("길게");
        sp3.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp4 = new SpannableString("연속");
        sp4.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        items1 = new SpannableString[]{sp1, sp2, sp3, sp4};
    }


}
