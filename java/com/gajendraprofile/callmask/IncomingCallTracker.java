package com.gajendraprofile.callmask;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;


public class IncomingCallTracker extends BroadcastReceiver{

    String num = "";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("TAG", "Broadcast Start");

        String mode_hide = Message.GetSP(context, "Pass", "switchHide", "OFF");
        if (mode_hide.equals("ON")) {
            num = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (num != null) {
                String secret = Message.GetSP(context, "Pass", "hide", "nil");


                if (secret.equals(num)) {

                    if(Message.GetSP(context,"Enable","status","OFF").equals("OFF")) {
                        PackageManager p = context.getPackageManager();
                        ComponentName componentName = new ComponentName(context.getApplicationContext(), welcome_start.class);
                        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        killCall(context);
                        Message.SetSP(context,"Enable","status","ON");
                        Message.toast(context, "Please wait....");
                    }else{
                        PackageManager p = context.getPackageManager();
                        ComponentName componentName = new ComponentName(context.getApplicationContext(), welcome_start.class);
                        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                        killCall(context);
                        Message.SetSP(context, "Enable", "status", "OFF");
                        Message.toast(context, "Please wait....");
                    }
                }
            }
        }else{

        }




        String mode = Message.GetSP(context, "Pass", "switch", "OFF");
        if (mode.equals("ON")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(state != null) {
                if (state.equals("IDLE")) {
                    Log.i("TAG", "IDLE");

                    try {
                        CallDelete(context);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }else{

        }
    }

    public void CallDelete(Context context) throws InterruptedException {

        Thread.sleep(3000);
        Uri UriCall = Uri.parse("content://call_log/calls");
        Cursor c = context.getContentResolver().query(UriCall, null, null, null, null);

        SharedPreferences sp = context.getSharedPreferences("Pass", Context.MODE_PRIVATE);
        String strNumber = sp.getString("phone", "00000");

        String queryStr = "NUMBER='" + strNumber + "'";

        Log.i("TAG", "Query : " + queryStr);
        int i = context.getContentResolver().delete(UriCall, queryStr, null);

        if (i >= 1) {
            Log.i("TAG", "Deleted : " + queryStr);
        }
    }

    public boolean killCall(Context context) {
        try {
            Thread.sleep(3000);
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            methodGetITelephony.setAccessible(true);

            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
