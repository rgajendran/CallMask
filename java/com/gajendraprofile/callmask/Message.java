package com.gajendraprofile.callmask;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Message {

    public static void toast(Context c, String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }

    public static String GetSP(Context context,String lib,String key, String defaults){
        SharedPreferences sp = context.getSharedPreferences(lib, Context.MODE_PRIVATE);
        String s= sp.getString(key, defaults);
        return s;
    }

    public static void SetSP(Context context, String lib, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(lib, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).apply();
    }

    public  static void SetSPBool(Context context, String lib, String key, Boolean bool){
        SharedPreferences sp = context.getSharedPreferences(lib, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, bool).apply();
    }

    public static Boolean GetSPBool(Context context, String lib, String key, Boolean defaults){
        SharedPreferences sp = context.getSharedPreferences(lib, Context.MODE_PRIVATE);
        Boolean s = sp.getBoolean(key, defaults);
        return s;
    }

    public static void SetInt(Context context, String lib, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(lib,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value).apply();
    }

    public static int GetInt(Context context, String lib, String key, int defaults){
        SharedPreferences sp = context.getSharedPreferences(lib,Context.MODE_PRIVATE);
        return sp.getInt(key, defaults);
    }

}
