package com.design_phantom.iwlldotime;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Common {


    public static String getSecond(long millitime){
        String second = "";
        long ans = millitime/1000;
        //1分以上ある場合は余りを秒数にセットする
        if( (ans / 60) >= 1 ){
            long sec = (ans % 60);
            second = (String.format("%02d", sec));
        }else{
            second = (String.format("%02d", ans));
        }

        return second;
    }

    public static String getMinutes(long millitime){
        String minute = "";
        long ans = millitime/1000;
        //1分以上ある場合は余りを秒数にセットする
        if( (ans / 60) >= 1 ){
            minute = (String.format("%02d", (int)Math.floor(ans/60)));
        }else{
            minute = ("00");
        }

        return minute;
    }

    public static void log(String comment){
        Log.i("INFO", comment);
    }

    public static void toast(Context context, String comment){
        Toast.makeText(context,comment,Toast.LENGTH_SHORT).show();
    }

}
