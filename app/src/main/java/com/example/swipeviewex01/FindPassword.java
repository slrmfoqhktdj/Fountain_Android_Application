package com.example.swipeviewex01;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPassword extends Activity {

    private LoginService ls;
    protected boolean isService = false;
    private Intent foregroundServiceIntent;

    private String theme;
    private ThemeAdapter ta;

    protected AES256Chiper aes256Chiper;

    private CustomSnackbar customSnackbar;

    private LoginService.MyBinder mb;

    private LinearLayout main;

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mb = (LoginService.MyBinder) iBinder;
            ls = mb.getService();
            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
            Log.e("cccccccc", "서비스 연결 끊김");
        }
    };

    private final BroadcastReceiver fBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FindPwSucess();
        }
    };

    private final BroadcastReceiver fBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FindPwFail();
        }
    };

    private final BroadcastReceiver fBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FindIdFail();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.CustomDialogThemeSelector(this,theme);

        setContentView(R.layout.activity_findpw);

        aes256Chiper = new AES256Chiper();

        customSnackbar = new CustomSnackbar();

//        if (LoginService.serviceIntent == null) {
//            foregroundServiceIntent = new Intent(this, LoginService.class);
//            startService(foregroundServiceIntent);
//            bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);
////            Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_LONG).show();
//        } else {
            foregroundServiceIntent = LoginService.serviceIntent;
            bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);
//            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
//        }

        final TextView findpw_id_tv = findViewById(R.id.findpwidtv);
        final TextView findpw_email_tv = findViewById(R.id.findpwemailtv);
        final TextView findpw_tell_tv = findViewById(R.id.findpwtelltv);
        final EditText findpw_id_et = findViewById(R.id.findpwid);
        final EditText findpw_email01_et = findViewById(R.id.findpwemail01);
        final EditText findpw_email02_et = findViewById(R.id.findpwemail02);
        final EditText findpw_tell01_et = findViewById(R.id.findpwtell01);
        final EditText findpw_tell02_et = findViewById(R.id.findpwtell02);
        final EditText findpw_tell03_et = findViewById(R.id.findpwtell03);
        final Button findpw_ok_bt = findViewById(R.id.findpwOk);
        final Spinner findpw_emailsp = findViewById(R.id.findpwemailspinner);
        main = findViewById(R.id.findpwmain);


        IntentFilter ffilter1 = new IntentFilter();
        ffilter1.addAction("FindPwSucess");
        registerReceiver(fBroadcastReceiver1,ffilter1);

        IntentFilter ffilter2 = new IntentFilter();
        ffilter2.addAction("FindPwFail");
        registerReceiver(fBroadcastReceiver2,ffilter2);

        IntentFilter ffilter3 = new IntentFilter();
        ffilter3.addAction("FindIdFail");
        registerReceiver(fBroadcastReceiver3,ffilter3);

        final String[] items = getResources().getStringArray(R.array.email_array);

        ArrayAdapter<String> spadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        findpw_emailsp.setAdapter(spadapter);
        
        ta.SpinnerlistbgThemeColor(theme,findpw_emailsp);

        findpw_emailsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (findpw_emailsp.getSelectedItemPosition()>0) {
                    findpw_email02_et.setText(items[i]);
                }
                //선택전 텍스트 색깔,크기
               ta.SpinnerTextThemeColor(theme,adapterView);
                ((TextView)adapterView.getChildAt(0)).setTextSize(15);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findpw_id_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_id_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_id_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_id_tv);
            }
        });

        findpw_email01_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_email01_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_email_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_email_tv);
            }
        });

        findpw_email02_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_email02_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_email_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_email_tv);
            }
        });

        findpw_tell01_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_tell01_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_tell_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_tell_tv);
            }
        });

        findpw_tell02_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_tell02_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_tell_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_tell_tv);
            }
        });

        findpw_tell03_et.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        findpw_tell03_et.setOnFocusChangeListener((view, b) -> {
            if(b){
                findpw_tell_tv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,findpw_tell_tv);
            }
        });

        findpw_ok_bt.setOnClickListener(view -> {
            String idtext = findpw_id_et.getText().toString().trim();
            String email_01 =findpw_email01_et.getText().toString().trim();
            String email_02 =findpw_email02_et.getText().toString().trim();
            String email = email_01+"@"+email_02;
            String tell_01 =findpw_tell01_et.getText().toString().trim();
            String tell_02 =findpw_tell02_et.getText().toString().trim();
            String tell_03 =findpw_tell03_et.getText().toString().trim();
            String tell = tell_01+tell_02+tell_03;

            if(!(idtext.equals(""))&&!(email_01.equals(""))&&!(email_02.equals(""))&&!(tell_01.equals(""))&&!(tell_02.equals(""))&&!(tell_03.equals(""))){
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id",idtext);
                    obj.put("email",email);
                    obj.put("tell",tell);
                    ls.socket.emit("FindPw",obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                ls.socket.emit("FindPw",idtext+"//"+email+"//"+tell);
//                ls.socket.on("FindPwSucess",onNewMessage2);
//                ls.socket.on("FindPwFail",onNewMessage3);
//                ls.socket.on("FindIdFail",onNewMessage4);
            }else{
                customSnackbar.toast("빈 항목을 채워주세요.",main);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(fBroadcastReceiver1);
        unregisterReceiver(fBroadcastReceiver2);
        unregisterReceiver(fBroadcastReceiver3);
        unbindService(conn);
        isService = false;
        ls = null;
        foregroundServiceIntent = null;
        main = null;
        customSnackbar = null;
        mb = null;
    }

    public void FindPwSucess(){
        customSnackbar.toast("새로운 비밀번호를 등록된 이메일로 보내드렸습니다.",main);
        Log.e("FindPassword","FindPwSucess");
        finish();
    }

    public void FindPwFail(){
        customSnackbar.toast("일치하지 않는 항목이 있습니다. 다시 확인해주세요.",main);
        Log.e("FindPassword","FindPwFail");
    }

    public void FindIdFail(){
        customSnackbar.toast("존재하지 않는 아이디 입니다.",main);
        Log.e("FindPassword","FindIdFail");
    }

}
