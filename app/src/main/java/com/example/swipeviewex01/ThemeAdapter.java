package com.example.swipeviewex01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;

public class ThemeAdapter {

    protected ThemeAdapter(){
    }

    protected void ActivityThemeSelector(Context context,String themeonoff){
        if(themeonoff.equals("1")){
            context.setTheme(R.style.DarkTheme);
        }else{
            context.setTheme(R.style.AppTheme);
        }
    }


//비밀번호 설정
    protected void ActivityThemeSelector(Context context, String themeonoff,LinearLayout linearLayout){
        if(themeonoff.equals("1")){
            linearLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.mainmenubar_dark));
        }else{
            linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.mainmenubar));
        }
    }
//메인
    protected void ActivityThemeSelector(Context context, String themeonoff, TabLayout tabLayout,LinearLayout linearLayout){
        if(themeonoff.equals("1")){
            tabLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.mainmenubar_dark));
            linearLayout.setBackgroundColor(Color.BLACK);
        }else{
            tabLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.mainmenubar));
            linearLayout.setBackgroundColor(Color.WHITE);
        }
    }
//도움말
    protected void ActivityThemeSelector(String themeonoff, ScrollView scrollView){
        if(themeonoff.equals("1")){
            scrollView.setBackgroundColor(Color.BLACK);
        }
    }





    protected LayoutInflater FragmentThemeSelector(Context context,String themeonoff,LayoutInflater inflater){
        Context mcontext;
        if(themeonoff.equals("1")){
            mcontext = new ContextThemeWrapper(context, R.style.DarkTheme);
        }else{
            mcontext = new ContextThemeWrapper(context, R.style.AppTheme);
        }
        LayoutInflater layoutInflater = inflater.cloneInContext(mcontext);

        return layoutInflater;
    }

    protected void FirstFragmentThemeSelector(Context context, String themeonoff,ScrollView scrollView){
        if(themeonoff.equals("1")){
            scrollView.setBackgroundColor(Color.BLACK);
        }else{
            scrollView.setBackgroundColor(Color.WHITE);
        }
    }

    protected void ControlCustomThemeSelector(Context context, String themeonoff, LinearLayout layout01, LinearLayout layout02, LinearLayout layout03, LinearLayout layout04, LinearLayout layout05, LinearLayout layout06){
        if(themeonoff.equals("1")){
            layout01.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            layout02.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            layout03.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            layout04.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            layout05.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            layout06.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
        }else{
            layout01.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            layout02.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            layout03.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            layout04.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            layout05.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            layout06.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
        }
    }

    protected void ThirdFragmentThemeSelector(Context context, String themeonoff, TextView textView01, TextView textView02,TextView textView03,TextView textView04, TextView textView05,TextView textView06){
        if(themeonoff.equals("1")){
            textView01.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            textView02.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            textView03.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            textView04.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            textView05.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
            textView06.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
        }else{
            textView01.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            textView02.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            textView03.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            textView04.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            textView05.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
            textView06.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg));
        }
    }



    protected void CustomAdapterThemeSelector(Context context, String themeonoff, CustomAdapter.CustomViewHolder viewholder){
        if(themeonoff.equals("1")){
            viewholder.main.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
        }else{

            viewholder.main.setBackground(ContextCompat.getDrawable(context, R.drawable.mainrecyclelistbg));
        }
    }

//    void SecondAdapterThemeSelector(Context context, String themeonoff, SecondAdapter.SecondViewHolder viewholder){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            if(themeonoff.equals("1")){
//                viewholder.main.setBackground(ContextCompat.getDrawable(context,R.drawable.mainrecyclelistbg_dark));
//            }else{
//                viewholder.main.setBackground(ContextCompat.getDrawable(context, R.drawable.mainrecyclelistbg));
//            }
//        } else {
//            if(themeonoff.equals("1")){
//                viewholder.main.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.mainrecyclelistbg_dark));
//            }else{
//                viewholder.main.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.mainrecyclelistbg));
//            }
//        }
//    }

    //idcreate
    protected void CustomDialogThemeSelector(Context context,String themeonoff){
        if(themeonoff.equals("1")){
            context.setTheme(R.style.DarkAlertDialog);
            GradientDrawable bgShape = (GradientDrawable) ContextCompat.getDrawable(context,R.drawable.idcreatpagebg);
            bgShape.setColor(Color.parseColor("#212121"));
        }else{
            context.setTheme(R.style.AlertDialog);
            GradientDrawable bgShape = (GradientDrawable) ContextCompat.getDrawable(context,R.drawable.idcreatpagebg);
            bgShape.setColor(Color.WHITE);
        }
    }

    protected void TextViewThemeColor(String themeonoff,TextView textView){
        if(themeonoff.equals("1")){
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            textView.setTextColor(Color.parseColor("#000000"));
        }
    }

    protected void SpinnerTextThemeColor(String themeonoff, AdapterView<?> adapterView){
        if(themeonoff.equals("1")){
            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
        }else{
            ((TextView)adapterView.getChildAt(0)).setTextColor(Color.BLACK);
        }
    }

    protected void SpinnerlistbgThemeColor(String themeonoff,Spinner spinner){
        if(themeonoff.equals("1")){
            spinner.setPopupBackgroundResource(R.drawable.spinnerlistbgcolor_black);
        }else{
            spinner.setPopupBackgroundResource(R.drawable.spinnerlistbgcolor_white);
        }
    }

    protected void CheckBoxTextThemeColor(String themeonoff, CheckBox checkBox){
        if(themeonoff.equals("1")){
            checkBox.setTextColor(Color.WHITE);
        }else{
            checkBox.setTextColor(Color.BLACK);
        }
    }


}
