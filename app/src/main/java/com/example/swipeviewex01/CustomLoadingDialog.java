package com.example.swipeviewex01;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;

public class CustomLoadingDialog extends Dialog {
    Context context;

    protected CustomLoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        //로딩창을 투명하게
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
    }
    @Override
    public void dismiss(){
        super.dismiss();
        context = null;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        dismiss();
    }


}
