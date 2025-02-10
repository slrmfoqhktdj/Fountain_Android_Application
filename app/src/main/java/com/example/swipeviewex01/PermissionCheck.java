package com.example.swipeviewex01;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class PermissionCheck extends Activity {


    protected ThemeAdapter ta;

    protected AlertDialogAdapter ada;
    protected CustomSnackbar ct;

    private String permission = "";

    private String[] PERMISSIONS ;

    private LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String theme = getIntent().getStringExtra("theme");
        ta = new ThemeAdapter();
        ta.CustomDialogThemeSelector(this, theme);

        ada = new AlertDialogAdapter(this, theme);
        ct = new CustomSnackbar();

        setContentView(R.layout.activity_permission);

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q) {
            PERMISSIONS = new String[]{
//            Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        permission = (String) getIntent().getSerializableExtra("permission");

        Button next = findViewById(R.id.permission_next);
        main = findViewById(R.id.password_main);

        next.setOnClickListener(v -> {
            //권한 허용을 요청함
            ActivityCompat.requestPermissions(PermissionCheck.this, permission.trim().split(" "),1);
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    //권한 체크 다 된건지 확인
    private static boolean hasPermissions(Context context,String... permissions){
        if(context != null && permissions != null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("onRequestPermission","onRequestPermission");
        //권한을 허용 했을 경우
        if(requestCode == 1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //권한 요청을 일단 다함
                if(hasPermissions(this, PERMISSIONS)){
                    //권한 중 빠진 권한이 있으면
                    ada.PermissionCheckDialog();
                }else{
                    //권한이 다 허용되었을 경우
                    Intent intent = new Intent("permission");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    finish();
                }
            }else{
                //권한 허용을 안했을 경우
                ada.PermissionCheckDialog();
            }

        }
    }

    // 뒤로가기 막기
    @Override
    public void onBackPressed(){}

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
    
    @SuppressLint("ShowToast")
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(hasPermissions(this, PERMISSIONS)){
            Intent intent = new Intent("finish");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            ct.toast("어플을 다시 시작하여 권한을 모두 허용해주세요",main);
        }
        ada = null;
    }


}
