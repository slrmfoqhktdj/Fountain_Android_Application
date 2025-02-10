package com.example.swipeviewex01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class PasswordSettingActivity extends AppCompatActivity {

    protected SharedPreferencesAdapter spa;
    protected ThemeAdapter ta;

    boolean transpwSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String theme = getIntent().getStringExtra("theme");

        spa = SharedPreferencesAdapter.getInstance();
//        theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this, theme);

        setContentView(R.layout.activity_passwordsetting);


        final SwitchCompat switchBtn = findViewById(R.id.passwordsetting_switchbtn);
        final TextView transpw = findViewById(R.id.passwordsetting_transpwbtn);
        LinearLayout title = findViewById(R.id.option_Title);

        String pwsetting = spa.getItem("pwsetting");

        if(pwsetting.equals("1")){
            switchBtn.setChecked(true);
            transpwSelector = true;
            transpw.setTextColor(Color.parseColor("#35ACD6"));
        }else{
            switchBtn.setChecked(false);
            transpwSelector = false;
            transpw.setTextColor(Color.parseColor("#FFC5C5C5"));
        }

        //테마
        ta.ActivityThemeSelector(this, theme,title);



        switchBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
//                spa.setItem("pwsetting","1");
//                spa.setItem("pw","0000");
                Intent intent = new Intent(PasswordSettingActivity.this,PasswordActivity.class);
                intent.putExtra("state","Option2");
                startActivity(intent);
                finish();
                transpw.setTextColor(Color.parseColor("#35ACD6"));
                transpwSelector = true;
            }else{
                spa.setItem("pwsetting","0");
                transpw.setTextColor(Color.parseColor("#FFC5C5C5"));
                transpwSelector = false;
            }
        });

        transpw.setOnClickListener(view -> {
            if(transpwSelector){
                Intent intent = new Intent(PasswordSettingActivity.this,PasswordActivity.class);
                intent.putExtra("state","Option");
                startActivity(intent);
                finish();
            }

        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        spa.setItem("pwrock","0");
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
