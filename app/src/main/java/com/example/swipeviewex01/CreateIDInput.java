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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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

import java.util.regex.Pattern;

public class CreateIDInput extends Activity {

    private LoginService ls;
    protected boolean isService = false;
    private Intent foregroundServiceIntent;
    private int overlapcount = 0;

    private String theme;
    private ThemeAdapter ta;

    protected AES256Chiper aes256Chiper;

    private CustomSnackbar customSnackbar;

    private LoginService.MyBinder mb;

    private LinearLayout main;

    //이메일 형식
//    private  String emailValidation = "^[_A-Za-z0-9-]+(||.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(||.[A-Za-z0-9-]+)*(||.[A-Za-z]{2,})$";
    
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

    private final BroadcastReceiver iBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CreateSucess();
        }
    };

    private final BroadcastReceiver iBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CreateFail();
        }
    };

    private final BroadcastReceiver iBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IdOverlapSucess();
        }
    };

    private final BroadcastReceiver iBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IdOverlapFail();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.CustomDialogThemeSelector(this,theme);

        setContentView(R.layout.activity_createidinput);


        aes256Chiper = new AES256Chiper();

//        customToast = new CustomToast(this);
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

        final EditText id = findViewById(R.id.custominputid);
        final EditText passward = findViewById(R.id.custominputpassward);
        final EditText confirmpassword = findViewById(R.id.customconfirmpassward);
        final EditText email01 = findViewById(R.id.custominputemail01);
        final EditText email02 = findViewById(R.id.custominputemail02);
        final EditText tell01 = findViewById(R.id.customtell01);
        final EditText tell02 = findViewById(R.id.customtell02);
        final EditText tell03 = findViewById(R.id.customtell03);
        final Button overlapBt = findViewById(R.id.idoverlap);
        final Button okBt = findViewById(R.id.customOk);
        final Button cencleBt = findViewById(R.id.customCencle);
        final TextView idtv = findViewById(R.id.custominputidtv);
        final TextView pwtv = findViewById(R.id.custominputpasswardtv);
        final TextView emailtv = findViewById(R.id.custominputemailtv);
        final TextView confirmpwtv = findViewById(R.id.customconfirmpasswardtv);
        final TextView telltv = findViewById(R.id.customtelltv);
        final Spinner emailsp= findViewById(R.id.custominputemailspinner);
        main = findViewById(R.id.createidinput_main);

        final String[] items = getResources().getStringArray(R.array.email_array);

        IntentFilter ifilter1 = new IntentFilter();
        ifilter1.addAction("CreateSucess");
        registerReceiver(iBroadcastReceiver1,ifilter1);

        IntentFilter ifilter2 = new IntentFilter();
        ifilter2.addAction("CreateFail");
        registerReceiver(iBroadcastReceiver2,ifilter2);

        IntentFilter ifilter3 = new IntentFilter();
        ifilter3.addAction("IdOverlapSucess");
        registerReceiver(iBroadcastReceiver3,ifilter3);

        IntentFilter ifilter4 = new IntentFilter();
        ifilter4.addAction("IdOverlapFail");
        registerReceiver(iBroadcastReceiver4,ifilter4);



        //글자 수 제한
        id.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(9)});
        passward.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(9)});

        ArrayAdapter<String> spadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        emailsp.setAdapter(spadapter);
        ta.SpinnerlistbgThemeColor(theme,emailsp);

        emailsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (emailsp.getSelectedItemPosition()>0) {
                    email02.setText(items[i]);
                }
                //선택전 텍스트 색깔,크기
                ta.SpinnerTextThemeColor(theme,adapterView);
                ((TextView)adapterView.getChildAt(0)).setTextSize(15);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        id.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        id.setOnFocusChangeListener((view, b) -> {
            if(b){
                idtv.setTextColor(Color.parseColor("#95A5FE"));
                overlapcount = 0;
            }else{
                ta.TextViewThemeColor(theme,idtv);
            }
        });

        passward.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        passward.setOnFocusChangeListener((view, b) -> {
            if(b){
                pwtv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,pwtv);
            }
        });

        confirmpassword.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        confirmpassword.setOnFocusChangeListener((view, b) -> {
            if(b){
                confirmpwtv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,confirmpwtv);
            }
        });


        email01.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        email01.setOnFocusChangeListener((view, b) -> {
            if(b){
                emailtv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,emailtv);
            }
        });


        email02.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        email02.setOnFocusChangeListener((view, b) -> {
            if(b){
                emailtv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,emailtv);
            }
        });

        tell01.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        tell01.setOnFocusChangeListener((view, b) -> {
            if(b){
                telltv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,telltv);
            }
        });
        tell02.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        tell02.setOnFocusChangeListener((view, b) -> {
            if(b){
                telltv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,telltv);
            }
        });
        tell03.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);


        tell03.setOnFocusChangeListener((view, b) -> {
            if(b){
                telltv.setTextColor(Color.parseColor("#95A5FE"));
            }else{
                ta.TextViewThemeColor(theme,telltv);
            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        overlapBt.setOnClickListener(view -> {
            String idtext = id.getText().toString().trim();
            if(idtext.length()>=3){
                ls.socket.emit("IDOverlap",idtext);
//                ls.socket.on("IdOverlapSucess",onNewMessage4);
//                ls.socket.on("IdOverlapFail",onNewMessage5);
            }else{
                customSnackbar.toast("아이디가 3자리 미만 입니다. 3~9자리가 되어야 합니다.",main);
            }
        });


        okBt.setOnClickListener(view -> {
            if(overlapcount==1){
                String idtext = id.getText().toString().trim();
                String passwardtext = passward.getText().toString().trim();
                String confirmpasswardtext = confirmpassword.getText().toString().trim();
                String email_01 =email01.getText().toString().trim();
                String email_02 =email02.getText().toString().trim();
                String email = email_01+"@"+email_02;
                String tell_01 =tell01.getText().toString().trim();
                String tell_02 =tell02.getText().toString().trim();
                String tell_03 =tell03.getText().toString().trim();
                String tell = tell_01+tell_02+tell_03;
                if(idtext!=null&&passwardtext!=null&&confirmpasswardtext!=null&&email_01!=null&&email_02!=null&&tell_01!=null&&tell_02!=null&&tell_03!=null){
                    if(confirmpasswardtext.equals(passwardtext)){
                        if(passwardtext.length()>=6){
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("id",idtext);
                                obj.put("pw",passwardtext);
                                obj.put("email",email);
                                obj.put("tell",tell);
                                ls.socket.emit("IDCreate",obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            ls.socket.emit("IDCreate",idtext+"//"+passwardtext+"//"+email+"//"+tell);
//                            ls.socket.on("CreateSucess", onNewMessage2);
//                            ls.socket.on("CreateFail", onNewMessage3);
                            overlapcount = 0;
                        }else {
                            customSnackbar.toast("비밀번호가 6자리 미만 입니다. 6~9자리가 되어야 합니다.",main);
                         }
                    }else{
                        customSnackbar.toast("비밀번호와 비밀번호 확인이 다릅니다.",main);
                    }
                }else{
                    customSnackbar.toast("빈 항목을 채워주세요.",main);
                }
            }else{
                customSnackbar.toast("아이디 중복확인을 해주세요.",main);
            }
        });

        cencleBt.setOnClickListener(view -> finish());


    }

    @Override
    public void onBackPressed() {

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(iBroadcastReceiver1);
        unregisterReceiver(iBroadcastReceiver2);
        unregisterReceiver(iBroadcastReceiver3);
        unregisterReceiver(iBroadcastReceiver4);
        unbindService(conn);
        isService = false;
        ls = null;

        foregroundServiceIntent = null;
        overlapcount = 0;
        mb = null;
        main = null;

        customSnackbar = null;
    }

    //숫자,영문 가능 필터
    protected InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!(charSequence.equals("")||ps.matcher(charSequence).matches())) {
                customSnackbar.toast("영문 대소문자,숫자만 입력 가능합니다.",main);
                return "";
            }
            return null;
        }
    };



    protected void CreateSucess(){
        customSnackbar.toast("아이디가 성공적으로 생성되었습니다.",main);
        finish();
    }

    protected void CreateFail(){
        customSnackbar.toast("잠시 후 시도해주세요.",main);
    }

    protected void IdOverlapSucess(){
        customSnackbar.toast("사용하실 수 있는 아이디 입니다.",main);
        overlapcount = 1;
    }

    protected void IdOverlapFail(){
        customSnackbar.toast("이미 존재하는 아이디 입니다. 다른 아이디로 입력해주세요",main);
    }

//
//    private void signUpRegex(final String email, final String password){
//        //키보드 내리기
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//
//        String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$";
//        Matcher matcher = Pattern.compile(pwPattern).matcher(password);
//
//        pwPattern = "(.)\\1\\1\\1";
//        Matcher matcher2 = Pattern.compile(pwPattern).matcher(password);
//
//        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            Snackbar.make(mainLayout, "이메일 형식이 아닙니다.", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!matcher.matches()) {
//            Snackbar.make(mainLayout, "영문, 숫자, 특수문자를 조합하여 비밀번호를 생성해야 합니다.", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        if(matcher2.find()) {
//            Snackbar.make(mainLayout, "비밀번호에 같은 문자를 네개 이상 사용할 수 없습니다.", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        if(password.contains(email)) {
//            Snackbar.make(mainLayout, "비밀번호가 이메일 중 일부를 포함할 수 없습니다.", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        if(password.contains(" ")) {
//            Snackbar.make(mainLayout, "비밀번호는 공백을 포함할 수 없습니다.", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//    }



}
