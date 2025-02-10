package com.example.swipeviewex01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ThemeSettingActivity extends AppCompatActivity {

    protected SharedPreferencesAdapter spa;
    protected ThemeAdapter ta;

    private RadioButton white_rb;
    private RadioButton black_rb;


    private Disposable backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spa = SharedPreferencesAdapter.getInstance();
        String theme = getIntent().getStringExtra("theme");
//        theme = spa.getItem("theme");
        ta = new ThemeAdapter();

        //테마
        ta.ActivityThemeSelector(this, theme);

        setContentView(R.layout.activity_themesetting);


        LinearLayout title = findViewById(R.id.themesetting_title);
        white_rb = findViewById(R.id.themesetting_wrb);
        black_rb = findViewById(R.id.themesetting_brb);



        if(theme.equals("0")| theme.equals("")){
            white_rb.setChecked(true);
        }else{
            black_rb.setChecked(true);
        }

        //테마
        ta.ActivityThemeSelector(this, theme, title);


        white_rb.setOnClickListener(view -> {
            black_rb.setChecked(false);
            spa.setItem("theme","0");
            restartApp();

        });

        black_rb.setOnClickListener(view -> {
            white_rb.setChecked(false);
            spa.setItem("theme","1");
            restartApp();

        });


    }

    private void restartApp() {

        //앱이 완전히 꺼지고 난후 다시 시작해야 서비스가 안꼬이기 때문에 동기화 해줌
        backgroundTask = Observable.fromCallable(() -> {
            //doInBackground(task에서 실행할 코드 여기에 작성)

            ActivityCompat.finishAffinity(this);

            return false;

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((result)-> {
            //onPostExecute(task 끝난 후 실행될 코드 여기에 작성)

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);


            backgroundTask.dispose();
        });


//        ActivityCompat.finishAffinity(this);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
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
        try {
            if(!backgroundTask.isDisposed()){
                backgroundTask.dispose();
            }
        }catch (Exception r){
            r.printStackTrace();
        }

    }



}

