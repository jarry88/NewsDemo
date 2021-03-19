package com.example.newsdemoapplication.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void Loge(String string){
        Log.e("TAG", "Log: "+string );
    }
    public static List<String> getData(int size){
        List<String> data=new ArrayList<>();
        for(int i=0;i<size;i++){
            data.add("内容"+i);
        }
        return data;
    }
}
