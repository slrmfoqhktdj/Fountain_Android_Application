package com.example.swipeviewex01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {
    private FragmentStateAdapter adapterViewPager;
    private FragmentManager fragmentManager;
    private LinearLayout main;
    private ViewPager2 vpPager;

    protected MainService ms;
    protected boolean isService = false;
    private final String tag = "MainActivity";

    private Intent foregroundServiceIntent;

    private String theme;
    private SharedPreferencesAdapter spa;
    private ThemeAdapter ta;
//    CustomLoadingDialog customProgressDialog;

//    static Activity mainActivity;
    protected AlertDialogAdapter ada;

//    String APPSTATE;

    private CustomSnackbar ct;

//    boolean appstate = true;

//    int height;

    //    언데드 서비스 컨넥터
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainService.MyBinder mb = (MainService.MyBinder) iBinder;
            ms = mb.getService();
            isService = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
            Log.e(tag,"서비스 연결 끊김");
        }
    };

    //thirdfragment logout
    private final BroadcastReceiver sBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            spa.setItem("loginid","");
            Intent logoutintent = new Intent(context,LoginActivity.class);
            startActivity(logoutintent);
            finish();
        }
    };

    //메인 백프레스 다이어로그
    private final BroadcastReceiver sBroadcastReceiver02 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            finish();
        }
    };

    
//    //스타트 로딩
//    private final BroadcastReceiver sBroadcastReceiver03 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
////            customProgressDialog.dismiss();
//        }
//    };

    //서버와 연결 끊김
    private final BroadcastReceiver sBroadcastReceiver04 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(appstate){
////                ct.toast("서버와의 연결이 끊겼습니다. 인터넷 상태를 확인해주세요.");
//            }
            ct.toast("서버와의 연결이 끊겼습니다. 인터넷 상태를 확인해주세요.",main);
        }
    };

    //서버와 연결중
    private final BroadcastReceiver sBroadcastReceiver05 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(appstate) {
////                ct.toast("서버와 연결 중 입니다. 잠시만 기다려주세요.");
//            }
            ct.toast("서버와 연결 중 입니다. 잠시만 기다려주세요.",main);
        }
    };

    //서버와 연결됨
    private final BroadcastReceiver sBroadcastReceiver06 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(appstate) {
////                ct.toast("서버와 연결되었습니다.");
//            }
            ct.toast("서버와 연결되었습니다.",main);
        }
    };


//    private void CallAlarmToast(String title,String message){
//        //백그라운드에서 토스트 메시지 노출시, 시스템이 이를 차단할 수 있다고 합니다. 텍스트 토스트 메시지는 여전히 허용된다고 합니다.
//        //
//        //setView를 호출하는 토스트 메시지만 해당 되는것 같습니다.
////        ct.AlarmToast(title,message,height);
//    }





    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectNonSdkApiUsage().penaltyLog().build());
        super.onCreate(savedInstanceState);

//        mainActivity = MainActivity.this;

        //테마

        spa = SharedPreferencesAdapter.getInstance();
        theme = spa.getItem("theme");
        ta = new ThemeAdapter();
        ta.ActivityThemeSelector(getApplicationContext(),theme);
//        customProgressDialog = new CustomLoadingDialog(this);
//        customProgressDialog.show();

        setContentView(R.layout.activity_main);

        //처음 시작하면 가이드 보여주기
        if(spa.getItem("first_app").equals("")){
//            startActivity(new Intent(this,GuideActivity.class));
            spa.setItem("first_app","1");
        }


        //서비스 실행여부

        if (MainService.serviceIntent == null) {
            foregroundServiceIntent = new Intent(getApplicationContext(), MainService.class);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                startForegroundService(foregroundServiceIntent);
            }else {
                startService(foregroundServiceIntent);
            }
            bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);
        } else {
                foregroundServiceIntent = MainService.serviceIntent;
                bindService(foregroundServiceIntent, conn, Context.BIND_AUTO_CREATE);

        }

        //탭 레이아웃의 뷰
        View tab1view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_mainbarselector01, null);
        View tab2view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_mainbarselector02, null);
        View tab3view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_mainbarselector03, null);

        View[] views = {tab1view,tab2view,tab3view};

        TabLayout mtabLayout = findViewById(R.id.maintab);
        main = findViewById(R.id.mainLayout);


        //테마
        ta.ActivityThemeSelector(getApplicationContext(),theme, mtabLayout,main);


        //탭레이아웃 3개 만들기
        mtabLayout.addTab(mtabLayout.newTab());
        mtabLayout.addTab(mtabLayout.newTab());
        mtabLayout.addTab(mtabLayout.newTab());
        
        fragmentManager = getSupportFragmentManager();

        //뷰페이져2 등록
        vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(fragmentManager,getLifecycle(), mtabLayout.getTabCount(),theme);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(2);

        //화면상태 -켜짐
//        spa.setItem("appstate","1");

        //탭 레이아웃과 뷰페이져2 연동
        new TabLayoutMediator(mtabLayout,vpPager,((tab, position) -> tab.setCustomView(views[position]))).attach();

        
        
        //중간에 나갓다 들어오면 뜨게하는 패스워드 초기 설정
        spa.setItem("pwrock","0");

        //커스텀 토스트, 앱이 꺼질때 context를 회수하기위해 선언함
//        ct = new CustomToast(this);
        ct = new CustomSnackbar();

        //브로드캐스트
        IntentFilter sfilter = new IntentFilter();
        sfilter.addAction("finish");
        registerReceiver(sBroadcastReceiver,sfilter);

        IntentFilter sfilter2 = new IntentFilter();
        sfilter2.addAction("finish2");
        registerReceiver(sBroadcastReceiver02,sfilter2);

//        IntentFilter sfilter3 = new IntentFilter();
//        sfilter3.addAction("Loading");
//        registerReceiver(sBroadcastReceiver03,sfilter3);

        IntentFilter sfilter4 = new IntentFilter();
        sfilter4.addAction("disconnect");
        registerReceiver(sBroadcastReceiver04,sfilter4);

        IntentFilter sfilter5 = new IntentFilter();
        sfilter5.addAction("connecting");
        registerReceiver(sBroadcastReceiver05,sfilter5);

        IntentFilter sfilter6 = new IntentFilter();
        sfilter6.addAction("connect");
        registerReceiver(sBroadcastReceiver06,sfilter6);

//        Display display = getWindowManager().getDefaultDisplay();  // in Activity
//        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
//        Point size = new Point();
//        display.getRealSize(size); // or getSize(size)
//        height = (size.y/4)*3;


//        PrefsHelper prefsHelper = PrefsHelper.getInstance();
//        prefsHelper.init(this);
//        prefsHelper.write("ex","ex");


        //쌓인 메시지 번호 초기화
        ResetNotifycationCount();





    }


    public static class MyPagerAdapter extends FragmentStateAdapter {
        private final int NUM_ITEMS;
        private String theme;

        public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,int tabCount,String theme) {
            super(fragmentManager, lifecycle);
            this.NUM_ITEMS = tabCount;
            this.theme = theme;
        }
        // Returns total number of pages
        @Override
        public int getItemCount() {
            return NUM_ITEMS;
        }
        // Returns the fragment to display for that page
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return FirstFragment.newInstance(theme);
                case 1:
                    return SecondFragment.newInstance(theme);
                case 2:
                    return ThirdFragment.newInstance(theme);
                default:
                    return null;
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e(tag,"onDestroy");
        try {
            unregisterReceiver(sBroadcastReceiver);
            unregisterReceiver(sBroadcastReceiver02);
//            unregisterReceiver(sBroadcastReceiver03);
            unregisterReceiver(sBroadcastReceiver04);
            unregisterReceiver(sBroadcastReceiver05);
            unregisterReceiver(sBroadcastReceiver06);
            if(isService){
                unbindService(conn);
                isService = false;
            }
            //서비스
            stopService(foregroundServiceIntent);
            foregroundServiceIntent = null;
            ms = null;


            vpPager.setAdapter(null);
            adapterViewPager = null;
            fragmentManager = null;
            ta = null;
            if(ada!=null){
                ada.onDestroy();
                ada = null;
            }
//            ct.onDestroy();
            ct = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e(tag,"onstart");

        //비밀번호 on 일시 잠시 중간에 나갔다 왔을때 passwordactivity 띄우기
        String pwsetting = spa.getItem("pwsetting");
        if(pwsetting.equals("1")&&spa.getItem("pwrock").equals("1")){
            Intent intent = new Intent(this,PasswordActivity.class);
            intent.putExtra("state","resume");
            startActivity(intent);
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        //화면상태 -꺼짐
//        spa.setItem("appstate","0");

        //비밀번호 설정이 on이면 화면 나갈떄 설정 on
        spa.setItem("pwrock","1");
//        appstate = false;
        Log.e(tag,"pause");
    }
    @Override
    public void onResume(){
        super.onResume();
        //화면상태 -켜짐

//        spa.setItem("appstate","1");
        //쌓인 메시지 번호 초기화
        ResetNotifycationCount();
//        appstate = true;
        Log.e(tag,"resume");
    }

    @Override
    public void onStop(){
        super.onStop();
        //화면상태 -켜짐

        Log.e(tag,"stop");
    }

    @Override
    public void onBackPressed(){

        ada = new AlertDialogAdapter(this,theme);
        ada.MainBackDialog();
    }

    public void ResetNotifycationCount(){
        //쌓인 메세지 초기화
        Intent intent2 = new Intent("resetcount");
        sendBroadcast(intent2);
    }




}