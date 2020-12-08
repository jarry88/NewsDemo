package com.gzp.baselib.utils;

import android.content.SharedPreferences;


import com.gzp.baselib.BaseApplication;

import java.util.Set;

/**
 * 共享参数管理类
 */
public class SharedPreferencesManager {

    private SharedPreferences sp;
    private static SharedPreferencesManager INSTANCE;

    public static String LONGITUDE_KEY="longitude";
    public static String LATITUDE_KEY="latitude";
    public static String PUSH_TOKEN="push_token";
    public static String PASSWORD="password";
    public static String ISOPENVOICECREDIT_KEY="isopenvoicecredit";
    private SharedPreferencesManager() {
        if (sp == null) {
            this.sp = BaseApplication.instance.getSharedPreferences("com.gzp.newsapp", 0);
        }
    }

    public static SharedPreferencesManager getInstance(){
        if(INSTANCE == null){
            synchronized (SharedPreferencesManager.class){
                if(INSTANCE == null){
                    INSTANCE = new SharedPreferencesManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 保存一个boolean值到SharedPreferences中
     */
    public void saveBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharedPreferences取出一个boolean类型 Value值
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 从SharedPreferences取出一个字符串Value值
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 保存一个字符串值到SharedPreferences中
     */
    public void saveString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
    public void clearString(){
        sp.edit().clear().commit();
    }

    public void saveInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void saveLong(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public void saveSet(String key, Set<String> value) {
        sp.edit().putStringSet(key, value).commit();
    }

    public Set<String> getSet(String key , Set<String> defValue) {
        return sp.getStringSet(key,defValue);
    }


}

