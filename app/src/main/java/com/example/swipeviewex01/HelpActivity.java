package com.example.swipeviewex01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    private SharedPreferencesAdapter spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spa = SharedPreferencesAdapter.getInstance();
        String theme = getIntent().getStringExtra("theme");
//        theme = spa.getItem("theme");
        ThemeAdapter ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this, theme);
        setContentView(R.layout.activity_help);

        ScrollView help_bg = findViewById(R.id.helper_bg);
        TextView hp_s = findViewById(R.id.helper_show);

        //테마
        ta.ActivityThemeSelector(theme, help_bg);

        hp_s.setOnClickListener(v -> {
            startActivity(new Intent(this,GuideActivity.class));
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
