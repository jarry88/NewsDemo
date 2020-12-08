package com.gzp.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtils {

    public static final String packageName = "com.language";
    public static final String LANGUAGE = "language";
    public static final String LANGUAGE_EN_US = "en-US"; //英语
    public static final String LANGUAGE_HI_IN = "hi-IN"; //印度

    public static void putString(Context context, String key, String dValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(key, dValue);
        editor.commit();//提交修改
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getString(key, "en-US");
    }


    public static Context selectLanguage(Context context, String language) {
        Context updateContext;
        //设置语言类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateContext = createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            updateContext = context;
        }
        putString(context, LANGUAGE, language);
        return updateContext;
    }

    //    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Context mContext = context;
        //设置语言类型
        Resources resources = mContext.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        switch (language) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "en_in":
                locale = new Locale("en", "IN");
                break;
            case "zh":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                getSystemLocale(context);
                break;
        }

        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
            mContext = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return mContext;
    }

    private static Locale getSystemLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }


    private static void applyLanguage(Context context, String language) {
        //设置语言类型
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale = null;
        switch (language) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "zh":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                locale = Locale.getDefault();
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocale(locale);
        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }

    public static Context updateLanguage(Context context) {
        String curLanguage = getString(context, LANGUAGE);
        if (null == curLanguage || TextUtils.isEmpty(curLanguage)) {
            curLanguage = LANGUAGE_EN_US;
        }
        return selectLanguage(context, curLanguage);
    }

    public static boolean switchLanguage(Context context, String value) {
        String curLanguage = getString(context, LANGUAGE);
        if (value.equals(curLanguage) || TextUtils.isEmpty(value)) {
            return false;
        }
        if (TextUtils.equals(value, LANGUAGE_HI_IN)) {
            putString(context, LANGUAGE, LANGUAGE_HI_IN);
        } else {
            putString(context, LANGUAGE, LANGUAGE_EN_US);
        }
        selectLanguage(context, getString(context, LANGUAGE));
        return true;
    }

}
