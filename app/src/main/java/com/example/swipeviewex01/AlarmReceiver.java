package com.example.swipeviewex01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("AlarmReceiver,","AlarmReceiver,");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Intent in = new Intent(context, MainService.class);
//            context.startForegroundService(in);
//            Log.e("AlarmReceiver,1","AlarmReceiver,1");
//        } else {
//            Intent in = new Intent(context, MainService.class);
//            context.startService(in);
//            Log.e("AlarmReceiver,2","AlarmReceiver,2");
//        }
        String action = intent.getAction();
        String TAG = "reboot_service";
        Log.e(TAG, "onReceive: " + action);
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Intent in = new Intent(context, MainService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(in);
                Log.e("AlarmReceiver,1","AlarmReceiver,1");
            } else {
                context.startService(in);
                Log.e("AlarmReceiver,2","AlarmReceiver,2");
            }
        }

    }

}