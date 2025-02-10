package com.example.swipeviewex01;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    private EditText pwcode01;
    private EditText pwcode02;
    private EditText pwcode03;
    private EditText pwcode04;
    private TextView pwTitle;
    private TextView pwsubTitle;

    private String state = "as";
    private int counter = 0;

    protected SharedPreferencesAdapter spa;
    private String theme;
    protected ThemeAdapter ta;
    private String pw;

    private String confirmpw;

    private LinearLayout main;

    protected CustomSnackbar customSnackbar;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spa = SharedPreferencesAdapter.getInstance();
        theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this,theme);

        setContentView(R.layout.activity_password);

        pw = spa.getItem("pw");

        customSnackbar = new CustomSnackbar();

        pwTitle = findViewById(R.id.passwordTitle);
        pwsubTitle = findViewById(R.id.passwordsubTitle);
        pwcode01 = findViewById(R.id.etPassword01);
        pwcode01.setClickable(false);
        pwcode01.setFocusable(false);
        pwcode02 = findViewById(R.id.etPassword02);
        pwcode02.setClickable(false);
        pwcode02.setFocusable(false);
        pwcode03 = findViewById(R.id.etPassword03);
        pwcode03.setClickable(false);
        pwcode03.setFocusable(false);
        pwcode04 = findViewById(R.id.etPassword04);
        pwcode04.setClickable(false);
        pwcode04.setFocusable(false);
        final Button btn0 = findViewById(R.id.btn0);
        final Button btn1 = findViewById(R.id.btn1);
        final Button btn2 = findViewById(R.id.btn2);
        final Button btn3 = findViewById(R.id.btn3);
        final Button btn4 = findViewById(R.id.btn4);
        final Button btn5 = findViewById(R.id.btn5);
        final Button btn6 = findViewById(R.id.btn6);
        final Button btn7 = findViewById(R.id.btn7);
        final Button btn8 = findViewById(R.id.btn8);
        final Button btn9 = findViewById(R.id.btn9);
        final Button btndel = findViewById(R.id.btndel);
        main = findViewById(R.id.password_main);
        pwcodeclear();

        state = getIntent().getStringExtra("state");
//        if(state.equals("Option")){
//            pwTitle.setText("비밀번호 변경");
//            pwsubTitle.setText("변경 할 비밀번호 4자리를 입력해주세요.");
//        }else
            if(state.equals("Option2")){
            pwTitle.setText("비밀번호 설정");
            pwsubTitle.setText("설정 할 비밀번호 4자리를 입력해주세요.");
        }
        Log.e("password",state);


        btn0.setOnClickListener(view -> InputButton("0"));
        btn1.setOnClickListener(view -> InputButton("1"));
        btn2.setOnClickListener(view -> InputButton("2"));
        btn3.setOnClickListener(view -> InputButton("3"));
        btn4.setOnClickListener(view -> InputButton("4"));
        btn5.setOnClickListener(view -> InputButton("5"));
        btn6.setOnClickListener(view -> InputButton("6"));
        btn7.setOnClickListener(view -> InputButton("7"));
        btn8.setOnClickListener(view -> InputButton("8"));
        btn9.setOnClickListener(view -> InputButton("9"));
        btndel.setOnClickListener(view -> {
            try{
                counter = decounter(counter);
                inputpw(counter).setText("");
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        });




    }
    private int counter(int count){
        if(count<3){
            count ++;
        }else {
            count =0;
        }
        return count;
    }

    private int decounter(int count){
        if(count>0){
            count--;
        }else{
            count = 0;
        }
        return count;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void InputButton(String number){
        inputpw(counter).setText(number);
        inputpw(counter).setBackground(null);
        counter = counter(counter);
        transactivity();
    }

    private EditText inputpw(int count){
        EditText[] result01 = {pwcode01,pwcode02,pwcode03,pwcode04};
        int result = 0;
        switch (count){
            case 0: result = 0;break;
            case 1: result = 1;break;
            case 2: result = 2;break;
            case 3: result = 3;break;
            default: break; }

        return result01[result];

    }


    private void transactivity(){
        if(counter==0) {
            String pwfull = pwcode01.getText().toString() + pwcode02.getText().toString() + pwcode03.getText().toString() + pwcode04.getText().toString();
            switch (state) {
                case "Intro":
                    Log.e("Password Intro", pwfull);
                    if (pw.equals(pwfull)) {
                        Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        pwcodeclear();
                        customSnackbar.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.",main);
                    }
                    break;
                case "resume":
                    Log.e("Password resume", pwfull);
                    if (pw.equals(pwfull)) {
                        finish();
                        spa.setItem("pwrock", "0");
                    } else {
                        pwcodeclear();
                        customSnackbar.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.",main);
                    }
                    break;
                case "Secure":
                    Log.e("Password secure", pwfull);
                    if (pw.equals(pwfull)) {
                        Intent intent = new Intent(PasswordActivity.this, PasswordSettingActivity.class);
                        intent.putExtra("theme", theme);
                        startActivity(intent);
                        finish();
                    } else {
                        pwcodeclear();
                        customSnackbar.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.",main);
                    }
                    break;
                case "Option2":
                    Log.e("Password option2", pwfull);
                    confirmpw = pwfull;
                    pwcodeclear();
                    pwTitle.setText("비밀번호 설정 확인");
                    pwsubTitle.setText("비밀번호를 확인해주세요.");
                    state = "Option3";
                    break;
                case "Option3":
                    Log.e("Password option3", pwfull);
                    if (confirmpw.equals(pwfull)) {
                        spa.setItem("pwsetting", "1");
                        spa.setItem("pw", confirmpw);
                        customSnackbar.toast("성공적으로 비밀번호가 설정되었습니다.",main);
                        Intent intent = new Intent(PasswordActivity.this, PasswordSettingActivity.class);
                        intent.putExtra("theme", theme);
                        startActivity(intent);
                        finish();
                    } else {
                        pwcodeclear();
                        customSnackbar.toast("비밀번호 확인이 틀렸습니다. 다시 입력해주세요.",main);
                    }
                    break;
                case "Option":
                    Log.e("Option", pwfull);
                    if (pw.equals(pwfull)) {
                        pwcodeclear();
                        pwTitle.setText("비밀번호 변경");
                        pwsubTitle.setText("변경 할 비밀번호 4자리를 입력해주세요.");
                        state = "Option4";
                    } else {
                        pwcodeclear();
                        customSnackbar.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.",main);
                    }
                    break;
                default:
                    Log.e("Password setting", pwfull);
                    spa.setItem("pw", pwfull);
                    customSnackbar.toast("성공적으로 비밀번호가 변경되었습니다.",main);
                    Intent intent = new Intent(PasswordActivity.this, PasswordSettingActivity.class);
                    intent.putExtra("theme", theme);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
//        if(state.equals("Intro")){
//            if(counter==0){
//                pwfull = pwcode01.getText().toString()+pwcode02.getText().toString()+pwcode03.getText().toString()+pwcode04.getText().toString();
//                Log.e("Password Intro",pwfull);
//                if(pw.equals(pwfull)){
//                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    pwcodeclear();
//                    customToast.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.");
//                }
//            }
//        }else if(state.equals("resume")){
//            if(counter==0) {
//                pwfull = pwcode01.getText().toString() + pwcode02.getText().toString() + pwcode03.getText().toString() + pwcode04.getText().toString();
//                Log.e("Password resume", pwfull);
//                if (pw.equals(pwfull)) {
//                    finish();
//                    spa.setItem("pwrock","0");
//                } else {
//                    pwcodeclear();
//                    customToast.toast("비밀번호가 틀렸습니다. 다시 입력해주세요.");
//                }
//            }
//        }else if(state.equals("Option2")){
//            if(counter==0) {
//                pwfull = pwcode01.getText().toString() + pwcode02.getText().toString() + pwcode03.getText().toString() + pwcode04.getText().toString();
//                Log.e("Password option2", pwfull);
//                confirmpw = pwfull;
//                pwcodeclear();
//                pwTitle.setText("비밀번호 설정 확인");
//                pwsubTitle.setText("비밀번호를 확인해주세요.");
//                state = "Option3";
//            }
//        }else if(state.equals("Option3")){
//            if(counter==0) {
//                pwfull = pwcode01.getText().toString() + pwcode02.getText().toString() + pwcode03.getText().toString() + pwcode04.getText().toString();
//                Log.e("Password option2", pwfull);
//                if(confirmpw.equals(pwfull)){
//                    spa.setItem("pwsetting","1");
//                    spa.setItem("pw",confirmpw);
//                    customToast.toast("성공적으로 비밀번호가 설정되었습니다.");
//                    Intent intent = new Intent(PasswordActivity.this,PasswordSettingActivity.class);
//                    intent.putExtra("theme",theme);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    pwcodeclear();
//                    customToast.toast("비밀번호 확인이 틀렸습니다. 다시 입력해주세요.");
//                }
//            }
//        }else{
//            if(counter==0){
//                pwfull = pwcode01.getText().toString()+pwcode02.getText().toString()+pwcode03.getText().toString()+pwcode04.getText().toString();
//                Log.e("Password setting",pwfull);
//                spa.setItem("pw",pwfull);
//                customToast.toast("성공적으로 비밀번호가 변경되었습니다.");
//                Intent intent = new Intent(PasswordActivity.this,PasswordSettingActivity.class);
//                intent.putExtra("theme",theme);
//                startActivity(intent);
//                finish();
//
//            }
//        }

    }
    private void pwcodeclear(){
        pwcode01.setText(null);
        pwcode02.setText(null);
        pwcode03.setText(null);
        pwcode04.setText(null);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
//        spa.setItem("pwsetting","0");
        spa.setItem("pwrock","0");
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
