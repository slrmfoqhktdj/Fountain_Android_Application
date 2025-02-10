package com.example.swipeviewex01;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;


public class IntroActivity extends AppCompatActivity {

    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        spa.init(getApplicationContext());

        theme = spa.getItem("theme");
        ThemeAdapter ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this,theme);

        setContentView(R.layout.activity_intro);

        //업데이트체크
        updateCheck();

        String pw = spa.getItem("pw");
        String pwsetting = spa.getItem("pwsetting");

        //알람 기본세팅
        if(spa.getItem("alarmexist").equals("")){
            Log.e("IntroActivity","alarmexist is null");
            spa.setItem("alarmexist","1");
            spa.setItem("alarmsound","1");
            spa.setItem("alarmvibe","1");
            spa.setItem("alarmshow","1");
        }

        //비밀번호 설정
        if(!((spa.getItem("loginid")).equals(""))){
            Log.e("login", spa.getItem("loginid"));

            Intent intent;
            if(pwsetting.equals("1")){
                intent = new Intent(IntroActivity.this,PasswordActivity.class);
                intent.putExtra("state","Intro");
            }else {
                intent = new Intent(IntroActivity.this,MainActivity.class);
            }
            startActivity(intent);
            finish();
        }else {
            IntroThread introThread = new IntroThread(handler);
            introThread.start();
        }


        //브로드캐스트
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,new IntentFilter("permission"));
        LocalBroadcastManager.getInstance(this).registerReceiver(fBroadcastReceiver,new IntentFilter("finish"));

    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequestPermission", "onRequestPermission");
        //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
                if (i == length - 1) {
                    Intent intent2 = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }

        }

    }

    public void checkSelfPermission() {
        Log.e("checkSelfPermission","checkSelfPermission");
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
            }
        }

        //위치 권한 확인
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            temp += Manifest.permission.ACCESS_FINE_LOCATION + " ";
//        }
        if (!TextUtils.isEmpty(temp)) {
            //허용이 안됬을 떄
            Intent intent = new Intent(this,PermissionCheck.class);
            intent.putExtra("permission",temp);
            intent.putExtra("theme",theme);
            startActivity(intent);
        }else {
            // 모두 허용 상태
//            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(IntroActivity.this,LoginActivity.class);
            startActivity(intent2);
            finish();
        }
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    checkSelfPermission();
                }else{
                    Intent intent2 = new Intent(IntroActivity.this,LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }
        }
    };

    //권한 허락후 permissioncheck activity에서 브로드캐스트를 쏘면 받는 리시버
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent2 = new Intent(IntroActivity.this,LoginActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
            
            //넘어가는 중간에 공백이 있어 1초 늦게 꺼줌
            Handler handler = new Handler();
            handler.postDelayed(() -> finish(), 1000);
        }
    };

    //권한이 허락이 안된체 permissioncheck activity를 닫으면 받는 리시버
    private final BroadcastReceiver fBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //늦게 꺼줌
            Handler handler = new Handler();
            handler.postDelayed(() -> finish(), 500);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fBroadcastReceiver);
    }


    protected void updateCheck(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // The current activity making the update request.
                            this,
                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                            // flexible updates.
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                    .setAllowAssetPackDeletion(true)
                                    .build(),
                            // Include a request code to later monitor this update request.
                            1);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //업데이트 콜백
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                Log.e("IntroActivity ","Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

}
