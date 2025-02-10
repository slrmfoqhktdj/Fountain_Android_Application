package com.example.swipeviewex01;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkConnectingStatus {
    public boolean NetworkStatus(Context context){
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(cm != null){
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if(capabilities != null){
                    result = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            }
        }else{
            if(cm != null){
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if(networkInfo != null){
                    result = (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)||(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
                }
            }
        }

        return result;
    }


//    public static boolean getConnectivityStatus(Context context){
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
//        boolean result = false;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            result = connectivityManager.isDefaultNetworkActive();
//        }else{
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if(networkInfo != null){
//                result = (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)||(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
//            }
//        }
//        return result;
//    }
}
