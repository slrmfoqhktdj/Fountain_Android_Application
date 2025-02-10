package com.example.swipeviewex01;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CustomSnackbar {
//    private Context mContext;


//    CustomToast(Context context){
//        this.mContext = context;
//    }

    protected void toast(String message,View view){
        Snackbar snackbar = Snackbar.make(view,message, BaseTransientBottomBar.LENGTH_LONG);
        View snackbarview = snackbar.getView();
        snackbarview.setBackgroundColor(Color.WHITE);
        TextView snackbarText = (TextView)snackbarview.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextColor(Color.parseColor("#6A82FE"));
        snackbarText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();




//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View toastDesign = inflater.inflate(R.layout.toast_layout,((Activity)mContext).findViewById(R.id.toast_root));
//        TextView toast_msg = toastDesign.findViewById(R.id.toast);
//
//        Toast toast = new Toast(mContext);
//        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, 250);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast_msg.setText(message);
//        toast.setView(toastDesign);
//        toast.show();


//        Toast myToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
//        View toastLayout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        myToast.setView(toastLayout);
//        myToast.show();
    }


//    public void AlarmToast(String title,String message,int Y){
//
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View toastDesign = inflater.inflate(R.layout.alarmtoast_layout,((Activity)mContext).findViewById(R.id.alarmtoast_root));
//        TextView toast_msg = toastDesign.findViewById(R.id.alarmtoast);
//        TextView toast_tilte = toastDesign.findViewById(R.id.alarmroast_title);
//
//        //안됨
////        toastDesign.setOnClickListener(v -> {
////            Log.e("CustomToast","AlarmToast");
////        });
//
//        Toast toast = new Toast(mContext);
//        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, Y);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast_tilte.setText(title);
//        toast_msg.setText(message);
//        toast.setView(toastDesign);
//        toast.show();
//
//    }
//    public void onDestroy(){
//        if(mContext!=null){
//            mContext = null;
//        }
//    }
}
