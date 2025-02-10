package com.example.swipeviewex01;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import org.json.JSONException;
import org.json.JSONObject;

public class ControlCustom extends Activity implements LifecycleOwner {

    private String names;
    private String borndevicename;
    private String water;
    private String onoff;
    private String error;
    private String mode;
    private String step;

    private boolean modeboolean;


    private MainService ms;
    protected boolean isService = false;

    private Intent foregroundServiceIntent;

    private AppDatabase db;
    private int count = 0;

    private String Myid;


    private TextView showname_tv;
    private TextView waterstate_tv;
    private ImageView waterstate_imgv;
    private ImageView start_imgv;
    private ImageView error_imgv;
    private TextView error_tv;

    private String restep;
    private String remode;


//    AES256Chiper aes256Chiper;

    LifecycleRegistry lifecycleRegistry;

    //    언데드 서비스 컨넥터
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainService.MyBinder mb = (MainService.MyBinder) iBinder;
            ms = mb.getService();
            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
            String tag = "system";
            Log.e(tag, "서비스 연결 끊김");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        String theme = spa.getItem("theme");
        ThemeAdapter ta = new ThemeAdapter();
        ta.CustomDialogThemeSelector(this, theme);

        setContentView(R.layout.activity_controlcustom);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        foregroundServiceIntent = new Intent(this, MainService.class);
        bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);

        //데이터 베이스 생성 및 연결
        db = AppDatabase.getInstance(this);

        showname_tv = findViewById(R.id.second_showname_tv);
        waterstate_tv = findViewById(R.id.second_waterstate_tv);
        waterstate_imgv = findViewById(R.id.second_waterstate_imgv);
        start_imgv = findViewById(R.id.second_start_imgv);
        error_imgv = findViewById(R.id.second_error_imgv);
        error_tv = findViewById(R.id.second_error_tv);
        LinearLayout showname_ll = findViewById(R.id.second_showname_ll);
        LinearLayout waterstate_ll = findViewById(R.id.second_waterstate_ll);
        LinearLayout start_ll = findViewById(R.id.second_start_ll);
        LinearLayout error_ll = findViewById(R.id.second_error_ll);
        LinearLayout step_ll = findViewById(R.id.second_step_ll);
        LinearLayout mode_ll = findViewById(R.id.second_mode_ll);

        TextView step1 = findViewById(R.id.step1);
        TextView step2 = findViewById(R.id.step2);
        TextView step3 = findViewById(R.id.step3);
        TextView step4 = findViewById(R.id.step4);
        TextView step5 = findViewById(R.id.step5);
        TextView automode = findViewById(R.id.automode);
        TextView custommode = findViewById(R.id.custommode);








        ta.ControlCustomThemeSelector(this, theme, showname_ll, waterstate_ll, start_ll, error_ll,step_ll,mode_ll);

        Myid = spa.getItem("loginid");
        Intent intent = getIntent();

        borndevicename =intent.getStringExtra("devicename");

        //인위적으로 lifecycleowner을 만듦
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        db.todoDao().dataselect(Myid,borndevicename).observe( this, todos -> {
            Log.e("todo" ,"asdasd"+todos.get(0).getDevice()+todos.get(0).getWater()+todos.get(0).getOnoff()+todos.get(0).getError()+todos.get(0).getMode()+todos.get(0).getStep());
            names = todos.get(0).getRedevice();
            water = todos.get(0).getWater();
            onoff = todos.get(0).getOnoff();
            error = todos.get(0).getError();
            step = todos.get(0).getStep();
            mode = todos.get(0).getMode();
            restep = step;
            remode = mode;
            Log.e("ControlCustom","restep : "+restep+" remode : "+remode);

            waterstate_tv.setText(water);


            if (water != null) {
                switch (water) {
                    case "100":
                        waterstate_imgv.setImageResource(R.drawable.water_05);
                        break;
                    case "75":
                        waterstate_imgv.setImageResource(R.drawable.water_04);
                        break;
                    case "50":
                        waterstate_imgv.setImageResource(R.drawable.water_03);
                        break;
                    case "25":
                        waterstate_imgv.setImageResource(R.drawable.water_02);
                        break;
                    case "0":
                        waterstate_imgv.setImageResource(R.drawable.water_01);
                        break;
                }
            }


            if (names != null) {
                showname_tv.setText(names);
            } else {
                showname_tv.setText("최근 사용 기기 없음");
            }

            if (onoff != null && error != null) {

                if (onoff.equals("1")) {
                    start_imgv.setImageResource(R.drawable.onbtn);
                } else {
                    start_imgv.setImageResource(R.drawable.offbtn);
                }

                if (error.equals("0")) {
                    error_tv.setText("");
                    error_imgv.setImageResource(R.drawable.offbtn);
                } else {
                    error_imgv.setImageResource(R.drawable.onbtn);
                    switch (error){
                        case "1":
                            error_tv.setText("소독약이 부족합니다. 확인해주세요.");
                            break;
                        case "2":
                            error_tv.setText("에러코드2");
                            break;
                        case "3":
                            error_tv.setText("에러코드3");
                            break;
                        default:
                            error_tv.setText("기기 전원이 불안정하거나 연결이 불안정합니다. 잠시 후 시도해주세요.");
                            break;
                    }
                }

            }

            if(mode!=null){
                switch (mode){
                    case "0" :
                        automode.setBackgroundColor(Color.parseColor("#35ACD6"));
                        custommode.setBackgroundColor(Color.WHITE);
                        modeboolean = true;
                        break;
                    case "1" :
                        custommode.setBackgroundColor(Color.parseColor("#35ACD6"));
                        automode.setBackgroundColor(Color.WHITE);
                        modeboolean = true;
                        break;
                    default:
                        custommode.setBackgroundColor(Color.WHITE);
                        automode.setBackgroundColor(Color.WHITE);
                        modeboolean = false;
                        break;
                }
            }

//
            if(step !=null){
                switch (step){
                    case "1" :
                        step1.setBackgroundColor(Color.parseColor("#35ACD6"));
                        step2.setBackgroundColor(Color.WHITE);
                        step3.setBackgroundColor(Color.WHITE);
                        step4.setBackgroundColor(Color.WHITE);
                        step5.setBackgroundColor(Color.WHITE);
                        break;
                    case "2" :
                        step1.setBackgroundColor(Color.WHITE);
                        step2.setBackgroundColor(Color.parseColor("#35ACD6"));
                        step3.setBackgroundColor(Color.WHITE);
                        step4.setBackgroundColor(Color.WHITE);
                        step5.setBackgroundColor(Color.WHITE);
                        break;
                    case "3" :
                        step1.setBackgroundColor(Color.WHITE);
                        step2.setBackgroundColor(Color.WHITE);
                        step3.setBackgroundColor(Color.parseColor("#35ACD6"));
                        step4.setBackgroundColor(Color.WHITE);
                        step5.setBackgroundColor(Color.WHITE);
                        break;
                    case "4" :
                        step1.setBackgroundColor(Color.WHITE);
                        step2.setBackgroundColor(Color.WHITE);
                        step3.setBackgroundColor(Color.WHITE);
                        step4.setBackgroundColor(Color.parseColor("#35ACD6"));
                        step5.setBackgroundColor(Color.WHITE);
                        break;
                    case "5" :
                        step1.setBackgroundColor(Color.WHITE);
                        step2.setBackgroundColor(Color.WHITE);
                        step3.setBackgroundColor(Color.WHITE);
                        step4.setBackgroundColor(Color.WHITE);
                        step5.setBackgroundColor(Color.parseColor("#35ACD6"));
                        break;
                }
            }


        });


        showname_tv.setOnClickListener(view -> {
            if (count == 0) {
                showname_tv.setText(borndevicename);
                count = 1;
            } else {
                showname_tv.setText(names);
                count = 0;
            }
        });

        start_imgv.setOnClickListener(view -> {
            try{
                if(modeboolean){
                    JSONObject obj = new JSONObject();
                    if (onoff.equals("0")) {
                        try {
                            obj.put("id",Myid);
                            obj.put("dv",borndevicename);
                            ms.socket.emit("PleaseOn",obj );
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
//                        ms.socket.emit("PleaseOn", Myid + "##" + borndevicename);
                        Log.e("start", "pleaseon");
                    } else {
                        try {
                            obj.put("id",Myid);
                            obj.put("dv",borndevicename);
                            ms.socket.emit("PleaseOff",obj );
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
//                        ms.socket.emit("PleaseOff", Myid + "##" + borndevicename);
                        Log.e("start", "pleaseoff");
                    }
                }else{
                    Toast.makeText(this,"먼저 모드를 설정해주세요",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        step1.setOnClickListener(view->{
            try {
                if(!(restep.equals("1"))){
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id",Myid);
                        obj.put("dv",borndevicename);
                        obj.put("st","1");
                        ms.socket.emit("PleaseStep",obj );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    ms.socket.emit("PleaseStep", Myid + "##" + borndevicename+"##1");
                    Log.e("step", "1");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        step2.setOnClickListener(view->{
            try {
                if(!(restep.equals("2"))) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id",Myid);
                        obj.put("dv",borndevicename);
                        obj.put("st","2");
                        ms.socket.emit("PleaseStep",obj );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    ms.socket.emit("PleaseStep", Myid + "##" + borndevicename + "##2");
                    Log.e("step", "2");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        step3.setOnClickListener(view->{
            try {
                if(!(restep.equals("3"))) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id",Myid);
                        obj.put("dv",borndevicename);
                        obj.put("st","3");
                        ms.socket.emit("PleaseStep",obj );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    ms.socket.emit("PleaseStep", Myid + "##" + borndevicename + "##3");
                    Log.e("step", "3");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        step4.setOnClickListener(view->{
            try {
                if(!(restep.equals("4"))) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id",Myid);
                        obj.put("dv",borndevicename);
                        obj.put("st","4");
                        ms.socket.emit("PleaseStep",obj );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    ms.socket.emit("PleaseStep", Myid + "##" + borndevicename + "##4");
                    Log.e("step", "4");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        step5.setOnClickListener(view->{
            try {
                if(!(restep.equals("5"))) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id",Myid);
                        obj.put("dv",borndevicename);
                        obj.put("st","5");
                        ms.socket.emit("PleaseStep",obj );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    ms.socket.emit("PleaseStep", Myid + "##" + borndevicename + "##5");
                    Log.e("step", "5");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        automode.setOnClickListener(view->{
            try {


                if(!(remode.equals("0"))) {
                    JSONObject obj1 = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    try {
                        obj2.put("id",Myid);
                        obj2.put("dv",borndevicename);
                        ms.socket.emit("PleaseOff",obj2);
                        obj1.put("id",Myid);
                        obj1.put("dv",borndevicename);
                        obj1.put("md","0");
                        ms.socket.emit("PleaseMode",obj1);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.e("mode", "0,auto");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        custommode.setOnClickListener(view->{
            try {
                if (!(remode.equals("1"))) {
                    JSONObject obj1 = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    try {
                        obj2.put("id",Myid);
                        obj2.put("dv",borndevicename);
                        ms.socket.emit("PleaseOff",obj2);
                        obj1.put("id",Myid);
                        obj1.put("dv",borndevicename);
                        obj1.put("md","1");
                        ms.socket.emit("PleaseMode",obj1);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Log.e("mode", "1,custom");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ControlCustom","onDestroy");
        unbindService(conn);
        ms = null;
        foregroundServiceIntent = null;
        db = null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
    }

}
