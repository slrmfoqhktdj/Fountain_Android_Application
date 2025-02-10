package com.example.swipeviewex01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ThirdFragment  extends Fragment {


    private String theme;
    protected ThemeAdapter ta;
    protected SharedPreferencesAdapter spa;

    private String pwsetting;

    protected AlertDialogAdapter ada;

    private Context mContext;

    //    언데드 서비스 컨넥터

    public static ThirdFragment newInstance(String theme) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString("theme",theme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        spa = SharedPreferencesAdapter.getInstance();
        //테마
        theme = getArguments().getString("theme");
        ta = new ThemeAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //테마적용
        View view = ta.FragmentThemeSelector(mContext,theme,inflater).inflate(R.layout.fragment_third, container, false);
        TextView option_alarm = view.findViewById(R.id.option_alarm);
        TextView option_logout = view.findViewById(R.id.option_logout);
        TextView option_password = view.findViewById(R.id.option_password);
        TextView option_theme = view.findViewById(R.id.option_theme);
        TextView option_update = view.findViewById(R.id.option_update);
        TextView option_helper = view.findViewById(R.id.option_helper);

        //테마
        ta.ThirdFragmentThemeSelector(mContext,theme, option_alarm, option_logout, option_password, option_theme, option_update, option_helper);

        //알림설정
        option_alarm.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext,AlarmSettingActivity.class);
            intent.putExtra("theme",theme);
            startActivity(intent);
        });

        //로그아웃
        option_logout.setOnClickListener(view12 -> {

            ada = new AlertDialogAdapter(mContext,theme);
            ada.ThirdFragmentLogout();
        });


        //비밀번호설정
        option_password.setOnClickListener(view13 -> {
            pwsetting = spa.getItem("pwsetting");
            if(pwsetting.equals("1")){
                Intent intent = new Intent(mContext,PasswordActivity.class);
                intent.putExtra("state","Secure");
                startActivity(intent);
            }else {
                Intent intent = new Intent(mContext, PasswordSettingActivity.class);
                intent.putExtra("theme",theme);
                startActivity(intent);
            }

        });

        //테마설정
        option_theme.setOnClickListener(view14 -> {
            Intent intent = new Intent(mContext, ThemeSettingActivity.class);
            intent.putExtra("theme",theme);
            startActivity(intent);
        });

        //업데이트 확인
        option_update.setOnClickListener(view15 -> {
            Intent intent = new Intent(mContext, UpdateCheckActivity.class);
            intent.putExtra("theme",theme);
            startActivity(intent);
        });

        //고객센터 및 도움말
        option_helper.setOnClickListener(view16 -> {
            Intent intent = new Intent(mContext,HelpActivity.class);
            intent.putExtra("theme",theme);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            mContext = null;
            ta = null;
            ada = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}