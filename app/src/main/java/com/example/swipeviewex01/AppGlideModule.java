package com.example.swipeviewex01;

import com.bumptech.glide.annotation.GlideModule;

//glide 경고 오류 잡기위해
@GlideModule
public class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled(){
        return false;
    }
}
