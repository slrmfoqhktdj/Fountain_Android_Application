package com.example.swipeviewex01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AlertDialogAdapter {
    private Context mContext;

    protected String theme;
    protected SharedPreferencesAdapter spa;


    protected AlertDialogAdapter(Context context,String theme){
        this.mContext = context;
        spa = SharedPreferencesAdapter.getInstance();
        this.theme = theme;
    }
    protected void MainBackDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad = builder.create();
        builder.setTitle("종료 알림창");
        builder.setMessage("어플을 종료하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("예", (dialogInterface, i) -> {
//            MainActivity ma = (MainActivity) MainActivity.mainActivity;
//            spa.setItem("appstate","0");
//            ma.finish();
            Intent intent = new Intent("finish2");
            mContext.sendBroadcast(intent);
        });
        builder.setNegativeButton("아니요", (dialogInterface, i) -> ad.dismiss());
        builder.show();
    }

//    void SecondFragmentImageDownload(final SecondAdapter.MyWebViewClient myWebViewClient, final String url){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
//        builder.setMessage("이미지를 다운로드 하시겠습니까?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                myWebViewClient.shouldOverrideUrlLoading(url);
//            }
//        });
//        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                return;
//            }
//        });
//        builder.show();
//    }

    protected void ThirdFragmentLogout(){

        //글자 색
        CharSequence[] info = new CharSequence[] {"로그아웃","종료"};
        SpannableString sp1 = new SpannableString("로그아웃");
        sp1.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp2 = new SpannableString("종료");
        sp2.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence[] info2 = new CharSequence[]{sp1,sp2};


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad = builder.create();
        builder.setItems(theme.equals("1")?info2:info, (dialog, which) -> {
            switch(which)
            {
                case 0:
//                    MainActivity ma = (MainActivity) MainActivity.mainActivity;
//                    ma.finish();
                    Intent intent = new Intent("finish");
                    mContext.sendBroadcast(intent);

                    break;

                case 1:
                   MainBackDialog();
                   break;
            }
            ad.dismiss();
        });
        builder.show();

    }

    protected void sendOption(int o){
        Intent intent = new Intent("notialram");
        intent.putExtra("o",o);
        mContext.sendBroadcast(intent);
    }

    protected void AlarmSettingVibeOn(SwitchCompat vibeswitchBtn,TextView vibepatternTv,TextView vibepatternTvsm,CharSequence items){
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad2 = builder2.create();
        builder2.setTitle("알림");
        builder2.setCancelable(false);
        builder2.setMessage("알람시 진동 옵션이 꺼져있습니다. 켜시겠습니까?");
        builder2.setPositiveButton("확인", (dialogInterface, i) -> {
            vibeswitchBtn.setChecked(true);
            spa.setItem("alarmvibe","1");
            vibepatternTv.setTextColor(Color.parseColor("#35ACD6"));
            vibepatternTvsm.setText(items);
            sendOption(3);
            ad2.dismiss();
        });

        builder2.setNegativeButton("취소", (dialogInterface, i) -> ad2.dismiss());
        builder2.show();
    }

    protected void AlarmSettingVibePattern(final TextView vibepatternTvsm, final Vibrator vibrator, final CharSequence[] items, final long[] pattern,final int[] amplitude){
        final int[] SelectedItem = {1};
        int chose = 0;
        CharSequence chosepattern = vibepatternTvsm.getText();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad = builder.create();
        builder.setTitle("진동 패턴");
        builder.setCancelable(false);

        for(int i = 0;i<4;i++){
            if(items[i].equals(chosepattern)){
                chose = i;
                break;
            }
        }
        builder.setSingleChoiceItems(items,chose, (dialogInterface, i) -> {
            SelectedItem[0]=i+1;
            if(Build.VERSION.SDK_INT>=26){
                switch (SelectedItem[0]){
                    case 1: vibrator.vibrate(VibrationEffect.createOneShot(1000,100));break;
                    case 2: vibrator.vibrate(VibrationEffect.createOneShot(500,100));break;
                    case 3: vibrator.vibrate(VibrationEffect.createOneShot(2000,100));break;
                    case 4: vibrator.vibrate(VibrationEffect.createWaveform(pattern,amplitude,-1));break;
                }
            }else {
                switch (SelectedItem[0]){
                    case 1: vibrator.vibrate(1000);break;
                    case 2: vibrator.vibrate(500);break;
                    case 3: vibrator.vibrate(2000);break;
                    case 4: vibrator.vibrate(pattern,-1);break;
                }
            }
        });

        builder.setPositiveButton("확인", (dialogInterface, i) -> {
            spa.setItem("alarmvibe",String.valueOf(SelectedItem[0]));
            Log.e("AlertDialogAdapter","SelectedItem "+(SelectedItem[0]-1));
            vibepatternTvsm.setText(items[SelectedItem[0]-1]);
            sendOption(3);
        });
        builder.setNeutralButton("취소", (dialogInterface, i) -> ad.dismiss());
        builder.show();
    }


    protected void CustomAdapterLongClick(final TextView devicename, final List<Todo> mList, final String Myid, final AppDatabase db){
        CharSequence[] info = new CharSequence[] {"이름 수정","기기 삭제"};

        //글자 색
        SpannableString sp1 = new SpannableString("이름 수정");
        sp1.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp2 = new SpannableString("기기 삭제");
        sp2.setSpan(new ForegroundColorSpan(Color.WHITE),0,sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence[] info2 = new CharSequence[]{sp1,sp2};


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad = builder.create();
        builder.setItems(theme.equals("1")?info2:info, (dialog, which) -> {
            switch(which)
            {
                case 0:
                    final String fromname = devicename.getText().toString();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
                    final AlertDialog ad1 = builder1.create();
                    builder1.setTitle("이름을 변경해주세요");
                    builder1.setCancelable(false);
                    final EditText renameText = new EditText(mContext);
                    if(theme.equals("1")){
                        renameText.setTextColor(Color.WHITE);
                    }
                    builder1.setView(renameText);
                    builder1.setPositiveButton("변경하기", (dialogInterface, i) -> {
                        String rename = renameText.getText().toString();

                        //이름중복방지
                        ArrayList<String> list = new ArrayList<>();
                        for(int j = 0; j<mList.size();j++){
                            list.add(mList.get(j).getRedevice());
                        }

//                                if(list.indexOf(rename)==-1){                      <----- ?
                        if(!list.contains(rename)){
                            db.todoDao().devicenameupdate(Myid,fromname,rename).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

                            ///이름변경시 picture의 redevice 의 이름도 변경해야함
                            db.todoDao().pdevicenameupdate(Myid,fromname,rename).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                            ad1.dismiss();
                        }else{
                            final AlertDialog.Builder builder11 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
                            final AlertDialog ad11 = builder11.create();
                            builder11.setTitle("알림");
                            builder11.setCancelable(false);
                            builder11.setMessage("동일한 이름의 기기가 존재합니다. 다른 이름으로 정해주세요");
                            builder11.setPositiveButton("확인", (dialogInterface1, i1) -> ad11.dismiss());
                            builder11.show();
                        }

                    });
                    builder1.setNegativeButton("취소", (dialogInterface, i) -> ad1.dismiss());
                    builder1.show();
                    break;

                case 1:
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
                    final AlertDialog ad2 = builder2.create();
                    builder2.setTitle("삭제");
                    builder2.setCancelable(false);
                    builder2.setMessage("선택한 기기를 삭제하시겠습니까?");
                    builder2.setPositiveButton("확인", (dialogInterface, i) -> {
                        db.todoDao().devicedelete(Myid,devicename.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                        ad2.dismiss();
                    });

                    builder2.setNegativeButton("취소", (dialogInterface, i) -> ad2.dismiss());
                    builder2.show();

                    break;
            }
            ad.dismiss();
        });
        builder.show();
    }


    protected void SecondAdapterLongClick(AppDatabase db, String devicename, String time, ImageView imgview,TextView imgshow,String type){
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad2 = builder2.create();
        builder2.setTitle("삭제");
        builder2.setCancelable(false);
        builder2.setMessage("선택한 목록을 삭제하시겠습니까?");
        builder2.setPositiveButton("확인", (dialogInterface, i) -> {
            db.todoDao().pdelete(devicename,time).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
            if(type.equals("P")){
                imgview.setImageBitmap(null);
                imgshow.setTextColor(Color.BLACK);
            }
            ad2.dismiss();
        });

        builder2.setNegativeButton("취소", (dialogInterface, i) -> ad2.dismiss());
        builder2.show();
    }

    protected void PermissionCheckDialog(){
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        ad.setTitle("앱 권한");
        ad.setMessage("해당 앱의 원할한 기능을 이용하시려면 어플리케이션 정보 > 권한 > 에서 모든 권한을 허용해 주십시오");
        ad.setCancelable(false);
        ad.setPositiveButton("권한설정", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:"+mContext.getPackageName()));
            mContext.startActivity(intent);
            dialog.cancel();
        });
        ad.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        ad.show();
    }

//    void AlertDialogAdapterfinish(){
//        mContext = null;
//        spa = null;
//        theme = null;
//    }

    protected void onDestroy(){
        if(mContext!=null){
            mContext = null;
        }
//        if(spa!=null){
//            spa = null;
//        }
    }
    
    
}
