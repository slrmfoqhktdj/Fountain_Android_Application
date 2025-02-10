package com.example.swipeviewex01;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesAdapter {
    private static final String PREFERENCE_NAME = "option";
    private static SharedPreferences sf;
    private static SharedPreferences.Editor editor;

    protected void init(Context context) {
        sf = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sf.edit();
    }
    // SingleTon 으로 생성
    private static class LazyHolder {
        public static final SharedPreferencesAdapter uniqueInstance = new SharedPreferencesAdapter();
    }

    protected static SharedPreferencesAdapter getInstance() {
        return LazyHolder.uniqueInstance;
    }
    

    protected String getItem(String calume){
        return  sf.getString(calume,"");
    }

    protected void setItem(String calume,String value){
        editor.putString(calume, value);
        editor.commit();

    }



}

//싱글톤 원본

//public class PrefsHelper {
//    public static final String PREFERENCE_NAME = "HELPER";
//    private static SharedPreferences prefs;
//    private static SharedPreferences.Editor prefsEditor;
//
//    // SingleTon 으로 생성
//    private static class LazyHolder {
//        public static final PrefsHelper uniqueInstance = new PrefsHelper();
//    }
//
//    public static PrefsHelper getInstance() {
//        return LazyHolder.uniqueInstance;
//    }
//
//    // init
//    public void init(Context context) {
//        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//        prefsEditor = prefs.edit();
//    }
//
//    //commit이나 apply를 해야 저장이 적용이 됨.
//    //defValue 는 만약 찾는값이 없을때 대체되어 나오는 값을 설정하는것
//
//    // String read, write
//    public String read(String key) {
//        return prefs.getString(key,"");
//    }
//
//    public void write(String key, String value) {
//        prefsEditor.putString(key, value).apply();
//    }

//    // Integer read, write
//    public Integer read(String key, int defValue) {
//        return prefs.getInt(key, defValue);
//    }
//
//    public void write(String key, Integer value) {
//        prefsEditor.putInt(key, value).apply();
//    }
//
//    // boolean read, write
//    public boolean read(String key, boolean defValue) {
//        return prefs.getBoolean(key, defValue);
//    }
//
//    public void write(String key, boolean value) {
//        prefsEditor.putBoolean(key, value).apply();
//    }

    // clear - 전체 모든 데이터값 삭제
//    public static void destroyPref() {
//        prefsEditor.clear().apply();
//    }

//}
