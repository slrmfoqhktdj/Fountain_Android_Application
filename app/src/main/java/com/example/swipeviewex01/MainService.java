
 package com.example.swipeviewex01;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainService extends Service {
    public static Intent serviceIntent = null;
    protected Socket socket;

    private AppDatabase db;

    private SharedPreferencesAdapter spa;
    String alarmexist;
    String alarmvibe;
    String alarmsound;
    String alarmshow;

    private String Myid;

    protected AES256Chiper aes256Chiper;

    private SimpleDateFormat sdf;
    private Date date;

    protected MainService ms;
    private IBinder mBinder = new MyBinder();

    long[] pattern = {0,500,200,500,200,1000};
    int[] amplitude = {0,100,0,100,0,150};

    private MediaPlayer player;

    protected class MyBinder extends Binder {
        MainService getService(){
            return MainService.this;
        }
    }

//    ArrayList<String> Devicelist = null;
//    String getDevicelist = null;
//    String getWaterlist = null;
//    String getOnofflist = null;
//    String getErrorlist = null;
//    String getErrorcodelist = null;
//    String getModelist = null;
//    String getSteplist = null;

//    ClearMachineThread cmt;

    private Context mContext;

    int count= 1;

//    Thread ConfirmThread;

    private NotificationManager manager;

    private final String CHANNEL_ID01 = "channel01";
    private final String CHANNEL_ID02 = "channel02";
//    String CHANNEL_ID03 = "channel03";
    private final String CHANNEL_ID04 = "channel04";
//    String CHANNEL_ID05 = "channel05";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            count = 1;
        }
    };

    private BroadcastReceiver mBroadcastReceiver02 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("o",5)){
                case 1:
                    NotificationOptions(1);
                    break;
                case 2:
                    NotificationOptions(2);
                    break;
                case 3:
                    NotificationOptions(3);
                    break;
                case 4:
                    NotificationOptions(4);
                    break;
                default:
                    NotificationOptions(5);
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, final int startld) {
        serviceIntent = intent;
//        initializeNotification();

        ms = new MainService();
        //데이터 베이스 생성 및 연결`

        db = AppDatabase.getInstance(this);


        spa = SharedPreferencesAdapter.getInstance();
        spa.init(getApplicationContext());

        //문자열 암호화
        aes256Chiper = new AES256Chiper();

        Myid = spa.getItem("loginid");


        //Notification channel 생성
        CreateNotificationChannel();

        //알람 설정
        NotificationOptions(5);

        mContext = getApplicationContext();

//        alarmexist = spa.getItem("alarmexist");
//        alarmsound = spa.getItem("alarmsound");
//        alarmvibe = spa.getItem("alarmvibe");


        new Thread(() -> {
            try {
//                    socket = IO.socket("http://yc7974.gabia.io");
                socket = IO.socket(getString(R.string.app_mainservice));
//                    socket = IO.socket("http://yc7974.cafe24.com:3000");
                socket.on("WaterState",onNewMessage15);
//                socket.on("confirmrecive",onNewMessage16);
                socket.on("picture",onNewMessage17);
                socket.on("ImOn",onNewMessage18);
                socket.on("ImOff",onNewMessage19);
                socket.on("deviceWakeUp",onNewMessage13);
                socket.on("mode",onNewMessage12);
                socket.on("step",onNewMessage11);

                socket.on("regist_Yes",onNewMessage1);
                socket.on("regist_No", onNewMessage2);
                socket.on("regist_reply_App_Yes",onNewMessage3);
                socket.on("regist_reply_App_No",onNewMessage4);
                socket.on("regist_reply2_App_Yes",onNewMessage5);
                socket.on("regist_reply2_App_No",onNewMessage6);

                socket.on(Socket.EVENT_DISCONNECT,onNewMessage20);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT,onNewMessage21);
                socket.on(Socket.EVENT_RECONNECT,onNewMessage22);
                socket.on(Socket.EVENT_RECONNECTING,onNewMessage23);
                socket.on(Socket.EVENT_CONNECT,onNewMessage24);
                socket.on(Socket.EVENT_RECONNECTING,onNewMessage25);

                socket.connect();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        



        return START_NOT_STICKY;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        initializeNotification();
        stopForeground(true);


        IntentFilter filter = new IntentFilter();
        filter.addAction("resetcount");
        registerReceiver(mBroadcastReceiver,filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("notialram");
        registerReceiver(mBroadcastReceiver02,filter2);


    }

    @Override
    public void onDestroy(){
        try{
            Log.e("MainService","onDestroy");
            socket.off("deviceWakeUp",onNewMessage13);
            socket.off("WaterState",onNewMessage15);
//            socket.off("confirmrecive",onNewMessage16);
            socket.off("picture",onNewMessage17);
            socket.off("ImOn",onNewMessage18);
            socket.off("ImOff",onNewMessage19);
            socket.off("regist_Yes",onNewMessage1);
            socket.off("regist_No", onNewMessage2);
            socket.off("regist_reply_App_Yes",onNewMessage3);
            socket.off("regist_reply_App_No",onNewMessage4);
            socket.off("regist_reply2_App_Yes",onNewMessage5);
            socket.off("regist_reply2_App_No",onNewMessage6);
            socket.off("mode",onNewMessage12);
            socket.off("step",onNewMessage11);
            socket.off(Socket.EVENT_DISCONNECT,onNewMessage20);
            socket.off(Socket.EVENT_CONNECT_TIMEOUT,onNewMessage21);
            socket.off(Socket.EVENT_RECONNECT,onNewMessage22);
            socket.off(Socket.EVENT_RECONNECTING,onNewMessage23);
            socket.off(Socket.EVENT_CONNECT,onNewMessage24);
            socket.off(Socket.EVENT_RECONNECTING,onNewMessage25);

            socket.disconnect();
            socket.close();
            socket = null;

            unregisterReceiver(mBroadcastReceiver);
            unregisterReceiver(mBroadcastReceiver02);
            mBroadcastReceiver = null;
            mBroadcastReceiver02 = null;

            db.close();
            AppDatabase.destoryInstance();
//            spa.onDestroy();
//            spa = null;
//            cmt = null;
//            ConfirmThread.interrupt();
            stopSelf();
            if(mContext!=null){
                mContext =null;
            }
            ms =null;
            serviceIntent = null;
            if(manager!=null){
                manager.cancel(2);
                manager = null;
            }
            player = null;
            mBinder = null;


        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void ServiceFinish() throws IOException {
//        Log.e("MainService","ServiceFinish");
//        stopSelf();
//    }

    private final Emitter.Listener onNewMessage1 = args -> {
        Intent intent = new Intent("regist_Yes");
        sendBroadcast(intent);
    };

    private final Emitter.Listener onNewMessage2 = args -> {
        Intent intent = new Intent("regist_No");
        sendBroadcast(intent);
    };

    private final Emitter.Listener onNewMessage3 = args -> {
        JSONObject data ;
        Intent intent = new Intent("regist_reply_App_Yes");
        try {
            data = new JSONObject(String.valueOf(args[0]));

            intent.putExtra("recivedata0",data.getString("water"));
            intent.putExtra("recivedata1",data.getString("onoff"));
            intent.putExtra("recivedata2",data.getString("error"));
            intent.putExtra("recivedata3",data.getString("step"));
            intent.putExtra("recivedata4",data.getString("mode"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        sendBroadcast(intent);
    };

    private final Emitter.Listener onNewMessage4 = args -> {
        Intent intent = new Intent("regist_reply_App_No");
        JSONObject data ;
        try {
            data = new JSONObject(String.valueOf(args[0]));

            intent.putExtra("user000",data.getString("user01"));
            intent.putExtra("user001",data.getString("user02"));
            intent.putExtra("user002",data.getString("user03"));
            intent.putExtra("user003",data.getString("user04"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        sendBroadcast(intent);
    };

    private final Emitter.Listener onNewMessage5 = args -> {
        Intent intent = new Intent("regist_reply2_App_Yes");
        JSONObject data ;
        try {
            data = new JSONObject(String.valueOf(args[0]));

            intent.putExtra("recivedata0",data.getString("water"));
            intent.putExtra("recivedata1",data.getString("onoff"));
            intent.putExtra("recivedata2",data.getString("error"));
            intent.putExtra("recivedata3",data.getString("step"));
            intent.putExtra("recivedata4",data.getString("mode"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        sendBroadcast(intent);

    };

    private final Emitter.Listener onNewMessage6 = args -> {
        Intent intent = new Intent("regist_reply2_App_No");
        sendBroadcast(intent);

    };

    private final Emitter.Listener onNewMessage11 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String devicename = null;
            String step = null;
            String redevicename;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                devicename = data.getString("device");
                step = data.getString("step");
            }catch (JSONException e){
                e.printStackTrace();
            }
            String time = PresentTime();



            redevicename = db.todoDao().redeviceselect(Myid,devicename);
            if(redevicename!=null){
                if(step!=null) {
                    db.todoDao().stepupdate(devicename, step);
//                db.todoDao().pinsert(new Picture(time, devicename, redevicename, "I-S/" + step, Myid, null));
                    db.todoDao().pinsert(time, devicename, redevicename, "I-S/" + step, Myid, null);
                    if(!(alarmexist.equals("0"))) {
                        showNoti(redevicename, "5", NotiChannelSelector());
                    }
                }
            }
        }
    };

    private final Emitter.Listener onNewMessage12 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String devicename = null;
            String mode = null;
            String redevicename;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                devicename = data.getString("device");
                mode = data.getString("mode");
            }catch (JSONException e){
                e.printStackTrace();
            }


            String time = PresentTime();

            redevicename = db.todoDao().redeviceselect(Myid,devicename);
            if(redevicename!=null) {
                if (mode != null) {
                    db.todoDao().modeupdate(devicename, mode);
//                db.todoDao().pinsert(new Picture(time,devicename,redevicename,"I-M/"+modename,Myid,null));
                    db.todoDao().pinsert(time, devicename, redevicename, "I-M/" + mode, Myid, null);
                    if (!(alarmexist.equals("0"))) {
                        showNoti(redevicename, "6", NotiChannelSelector());
                    }

                }
            }

        }
    };

    private final Emitter.Listener onNewMessage13 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));

                String device = data.getString("device");
                String water = data.getString("water");
                String onoff = data.getString("onoff");
                String error = data.getString("error");
                String mode = data.getString("mode");
                String step = data.getString("step");
                String redevicename;


                Log.e("deviceWakeUp","device:"+device +  "  water:"+water+  "  onoff:"+onoff+  "  error:"+error+  "  step:"+step+  "  mode:"+mode);
                if( db.todoDao().select(Myid,device)!=null){
                    db.todoDao().confirmupdate(device,water,onoff,error,step,mode);

                    String time = PresentTime();
                    redevicename = db.todoDao().redeviceselect(Myid,device);
//                    db.todoDao().pinsert(new Picture(time,device,redevicename,"I-U/",Myid,null));
//                    if(redevicename!=null) {
                        db.todoDao().pinsert(time, device, redevicename, "I-U/", Myid, null);
                        if (!(alarmexist.equals("0"))) {
                            showNoti(redevicename, "4", NotiChannelSelector());
                        }
//                    }

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };

    private final Emitter.Listener onNewMessage15 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String devicename = null;
            String water = null;
            String redevicename;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                devicename = data.getString("device");
                water = data.getString("water");
            }catch (JSONException e){
                e.printStackTrace();
            }


            String time = PresentTime();

            redevicename = db.todoDao().redeviceselect(Myid,devicename);
            if(redevicename!=null) {
//            db.todoDao().pinsert(new Picture(time,devicename,redevicename,"I-W/"+water,Myid,null));
                db.todoDao().pinsert(time, devicename, redevicename, "I-W/" + water, Myid, null);

                Log.d("WaterState", water);
                db.todoDao().waterupdate(devicename, water);

                if (water != null && water.equals("0")) {
                    //물부족하면 꺼짐
                    db.todoDao().onoffupdate(devicename,"0");
                    if (!(alarmexist.equals("0"))) {
                        showNoti(redevicename, "2", NotiChannelSelector());
                    }
                }
            }

//            headupnotiEx();

        }
    };

//    private final Emitter.Listener onNewMessage16 = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//
//            String[] datalist;
//            String[] waterlist;
//            String[] onofflist;
//            String[] errorlist;
//            String[] errorcodelist;
//            String[] steplist;
//            String[] modelist;
//
//            JSONObject data;
//            try {
//
//                data = new JSONObject(String.valueOf(args[0]));
//                getDevicelist = data.getString("device");
//                getWaterlist = data.getString("water");
//                getOnofflist = data.getString("onoff");
//                getErrorlist = data.getString("error");
//                getErrorcodelist = data.getString("errorcode");
//                getSteplist = data.getString("step");
//                getModelist = data.getString("mode");
//
////                Log.e("confirmreceive",getDevicelist+" "+getWaterlist+" "+getOnofflist+" "+getErrorlist+" "+getErrorcodelist+" ");
//
//                datalist = getDevicelist.split("/");
//                waterlist = getWaterlist.split("/");
//                onofflist = getOnofflist.split("/");
//                errorlist = getErrorlist.split("/");
//                errorcodelist = getErrorcodelist.split("/");
//                steplist = getSteplist.split("/");
//                modelist = getModelist.split("/");
//
//                for(int i = 0;i<datalist.length;i++){
//                    db.todoDao().confirmupdate(datalist[i],waterlist[i],onofflist[i],errorlist[i],errorcodelist[i],steplist[i],modelist[i]);
//                    Log.e("confirmreceive",datalist[i]+waterlist[i]+onofflist[i]+errorlist[i]+errorcodelist[i]+steplist[i]+modelist[i]);
//                }
//                Log.e("confirm","time : "+PresentTime());
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//
//        }
//
//    };

    private final Emitter.Listener onNewMessage17 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String devicename = null;
            String redevicename;
            String file = null;
            String time;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                devicename = data.getString("device");
                file = data.getString("file");
            }catch (JSONException e){
                e.printStackTrace();
            }

            time = PresentTime();
            Log.e("picture",devicename+time+Myid+file);

            redevicename = db.todoDao().redeviceselect(Myid,devicename);



            ////////////// 왓다갓다하는게 완료되면 풀고 고쳐야함
            
            
            //사진 캐시에 저장
//            Bitmap bitmap = null;
//            try {
//                bitmap = Glide.with(mContext).asBitmap().load("http://172.30.1.52:3003/images/"+time+".jpg").error(R.drawable.pictureerror).submit().get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ControlPicture cp = new ControlPicture(mContext);
//            cp.saveCacheImage(bitmap,time);

            if(redevicename!=null) {
                time = PresentTime();


                //디비에 저장
//            db.todoDao().pinsert(new Picture(time,devicename,redevicename,"P/",Myid,file));
                db.todoDao().pinsert(time, devicename, redevicename, "P/", Myid, file);

                if (!(alarmexist.equals("0"))) {
                    // 사진 미리보기를 사용하려면
//                showPicture(redevicename, NotiChannelSelector(),file);
                    showNoti(redevicename, "7", NotiChannelSelector());
                }
            }

        }
    };

    private final Emitter.Listener onNewMessage18 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String device = null;
            String water = null;
            String onoff = null;
            String error = null;
            String mode = null;
            String step = null;
            String redevicename;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                device = data.getString("device");
                water = data.getString("water");
                onoff = data.getString("onoff");
                error = data.getString("error");
                mode = data.getString("mode");
                step = data.getString("step");
            }catch (JSONException e){
                e.printStackTrace();
            }

            try{
                if(db.todoDao().select(Myid,device)!=null){
                    db.todoDao().confirmupdate(device,water,onoff,error,step,mode);
                    String time = PresentTime();
                    redevicename = db.todoDao().redeviceselect(Myid,device);
//                    if(redevicename!=null) {
                    if(error.equals("0")){
                        db.todoDao().pinsert(time, device, redevicename, "I-N/", Myid, null);
                        if (!(alarmexist.equals("0"))) {
                            showNoti(redevicename, "3", NotiChannelSelector());
                        }
                    }else{
                        db.todoDao().onoffupdate(device,"0");
                        db.todoDao().pinsert(time, device, redevicename, "I-N-E/", Myid, null);
                        if (!(alarmexist.equals("0"))) {
                            showNoti(redevicename, "8", NotiChannelSelector());
                        }
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private final Emitter.Listener onNewMessage19 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String device = null;
            String water = null;
            String onoff = null;
            String error = null;
            String mode = null;
            String step = null;
            String redevicename;
            JSONObject data;
            try {
                data = new JSONObject(String.valueOf(args[0]));
                device = data.getString("device");
                water = data.getString("water");
                onoff = data.getString("onoff");
                error = data.getString("error");
                mode = data.getString("mode");
                step = data.getString("step");
            }catch (JSONException e){
                e.printStackTrace();
            }


            try {

                if(db.todoDao().select(Myid,device)!=null) {
                    db.todoDao().confirmupdate(device, water, onoff, error, step, mode);
                    String time = PresentTime();
                    redevicename = db.todoDao().redeviceselect(Myid, device);
                    if(error.equals("0")){
                        db.todoDao().pinsert(time, device, redevicename, "I-F/", Myid, null);
                        if (!(alarmexist.equals("0"))) {
                            showNoti(redevicename, "1", NotiChannelSelector());
                        }
                    }else{
                        //에러가 나면 꺼짐
                        db.todoDao().onoffupdate(device,"0");
                        db.todoDao().pinsert(time, device, redevicename, "I-F-E/", Myid, null);
                        if (!(alarmexist.equals("0"))) {
                            showNoti(redevicename, "8", NotiChannelSelector());
                        }
                    }
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private final Emitter.Listener onNewMessage20 = args -> {Log.e("MainService","time : "+PresentTime() + " Event disconnect");
    try{
//        cmt = null; ConfirmThread.interrupt();
        Intent intent = new Intent("disconnect");
        sendBroadcast(intent);
    } catch (Exception e){ e.printStackTrace(); }
    };

    private final Emitter.Listener onNewMessage21 = args -> Log.e("MainService","time : "+PresentTime()+ " Event connect timeout");

    private final Emitter.Listener onNewMessage22 = args -> Log.e("MainService","time : "+PresentTime()+ " Event reconnect");

    private final Emitter.Listener onNewMessage23 = args -> Log.e("MainService","time : "+PresentTime() + " Event reconnecting");

    private final Emitter.Listener onNewMessage24 = args -> {
        try {
            Myid = spa.getItem("loginid");
            Log.e("MainService","time : "+PresentTime() + " Event connect "+ Myid );
            JSONObject obj = new JSONObject();
            obj.put("id",Myid);
            socket.emit("AppSocketId",obj );
        }catch (JSONException e){
            e.printStackTrace();
        }
//        socket.emit("AppSocketId",Myid);

//        cmt = new ClearMachineThread();
//        ConfirmThread = new Thread(cmt);
//        ConfirmThread.start();
        Intent intent = new Intent("connect");
        sendBroadcast(intent);
    };

    private final Emitter.Listener onNewMessage25 = args -> {Log.e("MainService","time : "+PresentTime() + " Event connecting"); Intent intent = new Intent("connecting"); sendBroadcast(intent);};




//
//    public void Confirm(){
//        Devicelist = (ArrayList<String>) db.todoDao().selects(Myid);
//        Log.e("Devicelist", String.valueOf(Devicelist));
//        if(!String.valueOf(Devicelist).equals("[]")){
//            try{
//                socket.emit("Confirm",Devicelist);
//
//            }catch (IndexOutOfBoundsException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
    
    private void CreateNotificationChannel(){
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //긴급 - 알림음이 울리며 헤드업 알림으로 표시
            //    String CHANNEL_ID05 = "channel05";
            String CHANEL_NAME = "Channel1";
            NotificationChannel channel01 = new NotificationChannel(CHANNEL_ID01, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel01.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel01.canShowBadge();
            channel01.setShowBadge(true);
            channel01.setSound(null,null);
            channel01.setVibrationPattern(null);
            manager.createNotificationChannel(channel01);

            //높음 - 알림음이 울림
            NotificationChannel channel02 = new NotificationChannel(CHANNEL_ID02, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel02.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel02.canShowBadge();
            channel02.setShowBadge(true);
            channel02.setSound(null,null);
            channel02.setVibrationPattern(null);
            manager.createNotificationChannel(channel02);

            //중간 - 알림음이 없음
//            NotificationChannel channel03 = new NotificationChannel(CHANNEL_ID03, CHANEL_NAME, NotificationManager.IMPORTANCE_LOW);
//            channel03.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            channel03.canShowBadge();
//            channel03.setShowBadge(true);
//            channel03.setSound(null,null);
//            channel03.setVibrationPattern(null);
//            manager.createNotificationChannel(channel03);
            
            //낮음 - 알림음이 없고 상태 표시줄에 표시되지 않음
            NotificationChannel channel04 = new NotificationChannel(CHANNEL_ID04, CHANEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel04.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel04.canShowBadge();
            channel04.setShowBadge(true);
            channel04.setSound(null,null);
            channel04.setVibrationPattern(null);
            manager.createNotificationChannel(channel04);

            //긴급 - 알람 없이 헤드업 알림으로 표시
//            NotificationChannel channel05 = new NotificationChannel(CHANNEL_ID05, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            channel05.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            channel05.canShowBadge();
//            channel05.setShowBadge(true);
//            channel05.setSound(null,null);
//            channel05.setVibrationPattern(null);
//            manager.createNotificationChannel(channel05);
        }
    }

//    public void showPicture(String name,String channel,String file){
//        NotificationCompat.Builder builder;
//
//        if(alarmsound.equals("1")){
//            try {
//                player = MediaPlayer.create(this, R.raw.alarmsound01);
//                player.setOnCompletionListener(mp -> {
//                    player.reset();
//                    player.release();
//                    player = null;
//                });
//                player.start();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        //버전 오레오 이상일 경우
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            builder = new NotificationCompat.Builder(this,channel);
//            //하위 버전일 경우
//        }else{
//            builder = new NotificationCompat.Builder(this);
//        }
//        Intent intent = new Intent(this, MainActivity.class);
//
//        //만약 다중으로 쌓여 클릭했을떄 쌓인 만큼 activity가 실행된다면 사용.
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
//        //알림창 제목
//        builder.setContentTitle("알림");
//
//        //헤드업 알림
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
//
//        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O){
//            builder.setSound(null);
//            builder.setVibrate(null);
//            builder.setDefaults(0);
//        }
//
//        //알림창 메시지
//        builder.setContentText(name+"에서 접근자를 확인했습니다.");
//        //알림창 아이콘
//        builder.setSmallIcon(R.drawable.app_main_notify);
//        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
//        builder.setAutoCancel(true);
//
//        // 시간표시
//        builder.setShowWhen(true);
//        //pendingIntent를 builder에 설정 해줍니다.
//        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
//        builder.setContentIntent(pendingIntent);
//
////        builder.setFullScreenIntent(pendingIntent,true);
//        try{
//            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(Glide.with(mContext).asBitmap().load("http://192.168.0.4:3010/uploads/"+file+".jpg").error(R.drawable.pictureerror).submit().get()));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
//
//        // 잠금화면 위로 알림창이뜨게한다.(상위버전)
//        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//        // 아이콘 뱃지 카운터
//        if(count > 1){
//            builder.setNumber(count);
//        }
//
//        Notification notification = builder.build();
//
//        //알림창 실행
//        manager.notify(2,notification);
//        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//
//        if(Build.VERSION.SDK_INT>=26){
//            switch (alarmvibe){
//                case "0": break;
//                case "2": vibrator.vibrate(VibrationEffect.createOneShot(500,100));break;
//                case "3": vibrator.vibrate(VibrationEffect.createOneShot(2000,100));break;
//                case "4": vibrator.vibrate(VibrationEffect.createWaveform(pattern,amplitude,-1));break;
//                default: vibrator.vibrate(VibrationEffect.createOneShot(1000,100));break;
//            }
//        }else {
//            switch (alarmvibe){
//                case "0": break;
//                case "2": vibrator.vibrate(500);break;
//                case "3": vibrator.vibrate(2000);break;
//                case "4": vibrator.vibrate(pattern,-1);break;
//                default: vibrator.vibrate(1000);break;
//            }
//        }
//
//
//        count++;
//    }



    private void showNoti(String name,String category,String channel){
//        String lockalarm_message;
        NotificationCompat.Builder builder;

        if(db.todoDao().redeviceselect(Myid,name)!=null) {

            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

            try {
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    if (alarmsound.equals("1")) {
                        try {
                            player = MediaPlayer.create(this, R.raw.alarmsound01);
                            player.setOnCompletionListener(mp -> {
                                try {
                                    player.reset();
                                    player.release();
                                    player = null;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            });
                            player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //버전 오레오 이상일 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new NotificationCompat.Builder(this, channel);
                //하위 버전일 경우
            } else {
                builder = new NotificationCompat.Builder(this);
            }
            Intent intent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //알림창 제목
            builder.setContentTitle("알림");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                if (alarmshow.equals("1")) {
                    //헤드업 알림
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                } else {
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                }
                builder.setDefaults(Notification.DEFAULT_LIGHTS);
//            builder.setSilent(true);
                builder.setVibrate(new long[]{0L});
            }
            //알람소리 설정
//        alarmsound = spa.getItem("alarmsound");

//        if(!alarmsound.equals("0")) {
//            builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarmsound01));
//        }
            //알림창 메시지
            switch (category) {
                case "1":
                    builder.setContentText(name + "가 전원이 꺼졌습니다.");
//                lockalarm_message = name + "가 전원이 꺼졌습니다.";
                    break;
                case "2":
                    builder.setContentText(name + "의 소독물이 떨어졌습니다.");
//                lockalarm_message = name + "의 소독물이 떨어졌습니다.";
                    break;
                case "3":
                    builder.setContentText(name + "가 전원이 켜졌습니다.");
//                lockalarm_message = name + "가 전원이 켜졌습니다.";
                    break;
                case "4":
                    builder.setContentText(name + "가 대기 상태입니다.");
//                lockalarm_message = name + "가 대기 상태입니다.";
                    break;
                case "5":
                    builder.setContentText(name + "의 강도가 변했습니다.");
//                lockalarm_message = name + "가 대기 상태입니다.";
                    break;
                case "6":
                    builder.setContentText(name + "의 모드가 변했습니다.");
//                lockalarm_message = name + "가 대기 상태입니다.";
                    break;
                case "7":
                    builder.setContentText(name + "에서 접근자를 확인했습니다.");
                    break;
                default:
                    builder.setContentText(name + "이 문제가 발생했습니다. 확인 부탁드립니다.");
//                lockalarm_message = name + "이 문제가 발생했습니다. 확인 부탁드립니다.";
                    break;
            }
            //알림창 아이콘
            builder.setSmallIcon(R.drawable.app_main_notify);
            //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
            builder.setAutoCancel(true);
            //pendingIntent를 builder에 설정 해줍니다.
            // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
            builder.setContentIntent(pendingIntent);

//        builder.setFullScreenIntent(pendingIntent,true);
            // 시간표시
            builder.setWhen(System.currentTimeMillis());

            builder.setShowWhen(true);

            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);


//            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);


            // 잠금화면 위로 알림창이뜨게한다.어느정도까지 보여줄지 결정(상위버전)
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            // 아이콘 뱃지 카운터
            if (count > 1) {
                builder.setNumber(count);
            }


            Notification notification = builder.build();
            //알림창 실행
            manager.notify(2, notification);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            //진동 설정
//        alarmvibe = spa.getItem("alarmvibe");
            try {
                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        switch (alarmvibe) {
                            case "0":
                                break;
                            case "2":
                                vibrator.vibrate(VibrationEffect.createOneShot(500, 100));
                                break;
                            case "3":
                                vibrator.vibrate(VibrationEffect.createOneShot(2000, 100));
                                break;
                            case "4":
                                vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitude, -1));
                                break;
                            default:
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, 100));
                                break;
                        }
                    } else {
                        switch (alarmvibe) {
                            case "0":
                                break;
                            case "2":
                                vibrator.vibrate(500);
                                break;
                            case "3":
                                vibrator.vibrate(2000);
                                break;
                            case "4":
                                vibrator.vibrate(pattern, -1);
                                break;
                            default:
                                vibrator.vibrate(1000);
                                break;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            count++;
        }

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        undeadworking();
        super.onTaskRemoved(rootIntent);
    }

    private void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_main);
//        builder.setLargeIcon(null);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
//        style.bigText("설정을 보려면 누르세요.");
//        style.setBigContentTitle(null);
//        style.setSummaryText("서비스 동작중");



        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
//        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        NotificationManager forgroundmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            forgroundmanager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_LOW));
        }
        Notification notification = builder.build();
        builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);

        startForeground(1, notification);
        Log.e("service"," initializeNotification()");
    }

    private void NotificationOptions(int o){
        try{
            switch (o){
                case 1:
                    //화면알람
                    alarmshow = spa.getItem("alarmshow");
                    break;
                case 2:
                    //알람소리 설정
                    alarmsound = spa.getItem("alarmsound");
                    break;
                case 3:
                    //진동 설정
                    alarmvibe = spa.getItem("alarmvibe");
                    break;
                case 4:
                    //알람 유무 설정
                    alarmexist = spa.getItem("alarmexist");
                    break;
                default:
                    //알람 유무 설정
                    alarmexist = spa.getItem("alarmexist");
                    //진동 설정
                    alarmvibe = spa.getItem("alarmvibe");
                    //화면알람
                    alarmshow = spa.getItem("alarmshow");
                    //알람소리 설정
                    alarmsound = spa.getItem("alarmsound");
                    break;
            }
//            //알람 유무 설정
//            alarmexist = spa.getItem("alarmexist");
//            //진동 설정
//            alarmvibe = spa.getItem("alarmvibe");
//            //화면알람
//            alarmshow = spa.getItem("alarmshow");
//            //알람소리 설정
//            alarmsound = spa.getItem("alarmsound");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String NotiChannelSelector(){
        String result;
        if(alarmexist.equals("0")){
            result = CHANNEL_ID04;
        }else{
//            if(alarmshow.equals("1")&&alarmsound.equals("1")){
//                result = CHANNEL_ID01;
//            }else{
//                if (alarmshow.equals("1") && alarmsound.equals("0")) {
//                    result = CHANNEL_ID05;
//                }else if(alarmshow.equals("0") && alarmsound.equals("1")){
//                    result = CHANNEL_ID02;
//                }else{
//                    result = CHANNEL_ID03;
//                }
//            }
            if(alarmshow.equals("1")){
                result = CHANNEL_ID01;
            }else {
                result = CHANNEL_ID02;
            }

        }

        Log.e("MainService"," exist = "+alarmexist+" sound = "+alarmsound+" show = "+alarmshow);
        return result;
    }



//    private class ClearMachineThread implements Runnable {
//
//        @Override
//        public void run(){
//            try {
//                while (!Thread.currentThread().isInterrupted()){
//                    //                Thread.sleep(600000);
//                    Confirm();
//                    Thread.sleep((60000)*20);
////                returnMessage rtm = new returnMessage();
//                    Log.e("ClearMachineThread","start");
////                rtm.run();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                Log.e("ClearMachineThread","dead");
//            }
//
//        }
//    }


//    public class returnMessage {
//        public void run()  {
////            ClearMachineThread cmt = new ClearMachineThread();
////            cmt.start();
//            Log.e("returnMessage","start");
//
//        }
//    }


    private String PresentTime(){
        sdf = new SimpleDateFormat("MM-dd HH:mm:ss",java.util.Locale.getDefault());
        date = new Date();
        return sdf.format(date);
    }

    private void undeadworking(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        Log.e("service","ontaskremoved");
    }

}
