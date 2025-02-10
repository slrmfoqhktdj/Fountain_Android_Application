package com.example.swipeviewex01;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private String idtext;
    private String pwtext;

    private LoginService ls;
    protected boolean isService = false;
    private Intent foregroundServiceIntent;


    protected AES256Chiper aes256Chiper = null;

    private SharedPreferencesAdapter spa;
    private ThemeAdapter ta;

    protected CustomLoadingDialog customProgressDialog;

    private NetworkConnectingStatus ncs;

    private Context mContext;

    private CustomSnackbar customSnackbar;

    private LoginService.MyBinder mb;

    private LinearLayout main;
//    private final int REQUEST_STORYGE = 1001;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mb = (LoginService.MyBinder) iBinder;
            ls = mb.getService();
            isService = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
            Log.e("Service","서비스 연결 끊김");
        }
    };

    private final BroadcastReceiver lBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loginSucess();
        }
    };

    private final BroadcastReceiver lBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loginFail();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spa = SharedPreferencesAdapter.getInstance();
        String theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.ActivityThemeSelector(this, theme);

        setContentView(R.layout.activity_login);

        mContext = this;

//        checkSelfPermission();


        final EditText id = findViewById(R.id.LoginID);
        final EditText pw = findViewById(R.id.LoginPW);
        final Button loginBT = findViewById(R.id.LoginBT);
        final TextView CreateBT = findViewById(R.id.CreateID);
        final ImageView idimg = findViewById(R.id.IdImg);
        final ImageView pwimg = findViewById(R.id.PwImg);
        final TextView findpw = findViewById(R.id.FindPw);
        main = findViewById(R.id.login_main);

        if (LoginService.serviceIntent == null) {
            foregroundServiceIntent = new Intent(getApplicationContext(), LoginService.class);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                startForegroundService(foregroundServiceIntent);
            }else{
                startService(foregroundServiceIntent);
            }
            bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);
        } else {
            foregroundServiceIntent = LoginService.serviceIntent;
            bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }

        aes256Chiper = new AES256Chiper();


        //데이터 베이스 생성 및 연결


        spa.setItem("loginid","");

        //로딩창 객체 생성
        customProgressDialog = new CustomLoadingDialog(getApplicationContext());
        ncs = new NetworkConnectingStatus();
        
        //커스텀 토스트메시지
        customSnackbar = new CustomSnackbar();

        //인터넷 연결 유무
        if(!ncs.NetworkStatus(mContext)){
            customSnackbar.toast("인터넷 연결이 되어있지 않습니다. 연결 후 다시 시도해주세요",main);
        }


        IntentFilter rfilter1 = new IntentFilter();
        rfilter1.addAction("loginSucess");
        registerReceiver(lBroadcastReceiver1,rfilter1);

        IntentFilter rfilter2 = new IntentFilter();
        rfilter2.addAction("loginFail");
        registerReceiver(lBroadcastReceiver2,rfilter2);


        id.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        id.setOnFocusChangeListener((view, b) -> {
            if(b){
                idimg.setImageResource(R.drawable.id2);
            }else{
                idimg.setImageResource(R.drawable.id);
            }
        });

        pw.setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_ENTER);

        pw.setOnFocusChangeListener((view, b) -> {
            if(b){
                pwimg.setImageResource(R.drawable.lock);
            }else{
                pwimg.setImageResource(R.drawable.lock2);
            }
        });

        findpw.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, FindPassword.class);
            startActivity(intent);
        });



        loginBT.setOnClickListener(view -> {
            if(ncs.NetworkStatus(mContext)) {
                idtext = id.getText().toString().trim();
                pwtext = pw.getText().toString().trim();
                if (idtext.equals("") || pwtext.equals("")) {
                    customSnackbar.toast("아이디와 비밀번호를 모두 입력해주세요.",main);
                } else {
                    try {


                        if (!ncs.NetworkStatus(mContext)) {
                            customSnackbar.toast("인터넷 연결이 되어있지 않습니다. 연결 후 다시 시도해주세요",main);
                        } else {
//                            ls.socket.emit("loginP", idtext + "//" + pwtext);
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("id",idtext);
                                obj.put("pw",pwtext);
                                ls.socket.emit("loginP",obj );
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }

//                    ls.socket.emit(aes256Chiper.encrypt("loginP"),aes256Chiper.encrypt(idtext+"//"+pwtext));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                customSnackbar.toast("인터넷 연결이 되어있지 않습니다. 연결 후 다시 시도해주세요",main);
            }
        });


        CreateBT.setOnClickListener(view -> {
            if(!ncs.NetworkStatus(mContext)){
                customSnackbar.toast("인터넷 연결이 되어있지 않습니다. 연결 후 다시 시도해주세요",main);
            }else{
                Intent intent = new Intent(LoginActivity.this, CreateIDCheck.class);
                startActivity(intent);
            }
        });

    }


    public void loginSucess(){
        spa.setItem("loginid",idtext);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginFail(){
        customSnackbar.toast("로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요",main);
//        Toast.makeText(getApplicationContext())
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            unregisterReceiver(lBroadcastReceiver1);
            unregisterReceiver(lBroadcastReceiver2);
            unbindService(conn);
            isService = false;
            stopService(foregroundServiceIntent);
            foregroundServiceIntent =null;
            ls.stopSelf();
            ls = null;
            ta = null;
            main = null;
            customSnackbar = null;
            mContext = null;
            customProgressDialog = null;
            ncs = null;
            mb = null;
            conn = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
