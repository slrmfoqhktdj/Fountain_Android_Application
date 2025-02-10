package com.example.swipeviewex01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CreateIDCheck extends Activity {
    private CustomSnackbar customSnackbar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        String theme = spa.getItem("theme");
        ThemeAdapter ta = new ThemeAdapter();
        ta.CustomDialogThemeSelector(this, theme);

        setContentView(R.layout.activity_createidcheck);

        final CheckBox checkBox01 = findViewById(R.id.createidcheck01);
        final CheckBox checkBox02 = findViewById(R.id.createidcheck02);
        final Button nextbt = findViewById(R.id.createidchecknext);
        final ScrollView scrollViewb = findViewById(R.id.createidchecknextsv_b);
        final ScrollView scrollViews01 = findViewById(R.id.createidchecknextsv_s01);
        final ScrollView scrollViews02 = findViewById(R.id.createidchecknextsv_s02);
        final TextView terms = findViewById(R.id.termsTv);
        final TextView privacy = findViewById(R.id.privacyTv);
        final LinearLayout main = findViewById(R.id.createidcheck_main);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        customToast = new CustomToast(this);
        customSnackbar = new CustomSnackbar();

        ta.CheckBoxTextThemeColor(theme,checkBox01);
        ta.CheckBoxTextThemeColor(theme,checkBox02);

        terms.setText(getTermsString("termsofuse"));
        privacy.setText(getTermsString("privacypolicy"));


        scrollViews01.setOnTouchListener((view, motionEvent) -> {
            scrollViewb.requestDisallowInterceptTouchEvent(motionEvent.getAction() != MotionEvent.ACTION_UP);
            return false;
        });

        scrollViews02.setOnTouchListener((view, motionEvent) -> {
            scrollViewb.requestDisallowInterceptTouchEvent(motionEvent.getAction() != MotionEvent.ACTION_UP);
            return false;
        });

        nextbt.setOnClickListener(view -> {
            if(checkBox01.isChecked()&&checkBox02.isChecked()){
                Intent intent = new Intent(CreateIDCheck.this,CreateIDInput.class);
                startActivity(intent);
                finish();
            }else {
                customSnackbar.toast("모든 항목의 동의가 필요합니다.",main);
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        customSnackbar = null;
    }

    private String getTermsString(String filename){
        StringBuilder termsString = new StringBuilder();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(getAssets().open(filename+".txt")));
            String str;
            while ((str = reader.readLine()) != null){
                termsString.append(str);
            }
            reader.close();
            return termsString.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
