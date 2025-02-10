package com.example.swipeviewex01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class LoginService extends Service {
    public static Intent serviceIntent = null;
    protected Socket socket;

    protected LoginService ls;
    private IBinder mBinder = new MyBinder();

    protected class MyBinder extends Binder {
        LoginService getService(){
            return LoginService.this;
        }
    }

    @Override
    public  IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startld) {
        serviceIntent = intent;
        ls = new LoginService();
        new Thread(() -> {
            try {
//                    socket = IO.socket("http://yc7974.gabia.io");
                socket = IO.socket(getString(R.string.app_mainservice));
//                    socket = IO.socket("http://yc7974.cafe24.com:3000");
//                    socket = IO.socket("http://211.223.84.233:46604");

                socket.on("loginSucess", onNewMessage0);
                socket.on("loginFail", onNewMessage1);
                socket.on("CreateSucess", onNewMessage2);
                socket.on("CreateFail", onNewMessage3);
                socket.on("IdOverlapSucess",onNewMessage4);
                socket.on("IdOverlapFail",onNewMessage5);
                socket.on("FindPwSucess",onNewMessage6);
                socket.on("FindPwFail",onNewMessage7);
                socket.on("FindIdFail",onNewMessage8);

                socket.on(Socket.EVENT_DISCONNECT,onNewMessage20);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT,onNewMessage21);
                socket.on(Socket.EVENT_RECONNECT,onNewMessage22);
                socket.on(Socket.EVENT_RECONNECTING,onNewMessage23);

                socket.connect();
                Log.e("Loginservice","로그인 서비스 시작");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();





        return START_NOT_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String strId = "1";
            final String strTitle = "login_service";
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(strId);
            if (channel == null) {
                channel = new NotificationChannel(strId, strTitle, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, strId).build();
            startForeground(1, notification);
        }
    }


    Emitter.Listener onNewMessage0 = args -> {
        Intent intent = new Intent("loginSucess");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage1 = args -> {
        Intent intent = new Intent("loginFail");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage2 = args -> {
        Intent intent = new Intent("CreateSucess");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage3 = args -> {
        Intent intent = new Intent("CreateFail");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage4 = args -> {
        Intent intent = new Intent("IdOverlapSucess");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage5 = args -> {
        Intent intent = new Intent("IdOverlapFail");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage6 = args -> {
        Intent intent = new Intent("FindPwSucess");
        sendBroadcast(intent);
    };

    Emitter.Listener onNewMessage7 = args -> {
        Intent intent = new Intent("FindPwFail");
        sendBroadcast(intent);
    };
    Emitter.Listener onNewMessage8 = args -> {
        Intent intent = new Intent("FindIdFail");
        sendBroadcast(intent);
        Log.e("LoginService","FindIdFail");
    };

    Emitter.Listener onNewMessage20 = args -> Log.e("LoginService","EVENT_DISCONNECT");
    Emitter.Listener onNewMessage21 = args -> Log.e("LoginService","EVENT_CONNECT_TIMEOUT");
    Emitter.Listener onNewMessage22 = args -> Log.e("LoginService","EVENT_RECONNECT");
    Emitter.Listener onNewMessage23 = args -> Log.e("LoginService","EVENT_RECONNECTING");


//
//    public void ServiceFinish() throws IOException {
//        socket.close();
//        try{
//            mBinder = null;
//            la = null;
//            stopService(serviceIntent);
//            stopSelf();
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
//        serviceIntent = null;
//    }


    @Override
    public void onDestroy(){
        try{
            socket.off("loginSucess", onNewMessage0);
            socket.off("loginFail", onNewMessage1);
            socket.off("CreateSucess", onNewMessage2);
            socket.off("CreateFail", onNewMessage3);
            socket.off("IdOverlapSucess",onNewMessage4);
            socket.off("IdOverlapFail",onNewMessage5);
            socket.off("FindPwSucess",onNewMessage6);
            socket.off("FindPwFail",onNewMessage7);
            socket.off("FindIdFail",onNewMessage8);
            socket.disconnect();
            socket.close();
            socket = null;
            Log.e("LoginService","onDestroy");
            stopSelf();
            ls =null;
            serviceIntent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            }
            mBinder = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
