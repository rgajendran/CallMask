package com.gajendraprofile.callmask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class AlarmSetter extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        updateSub();
    }


    private void updateSub(){
        int value = Message.GetInt(getBaseContext(),"Sub","Days",0);
        if(value > 0) {
            Message.SetInt(getBaseContext(), "Sub", "Days", value - 1);
            stopSelf();
        }else if(value == 0){
            GCMListenerService.sendNotification(getBaseContext(),"Your subscription has expired! Please click on the ads twice to renew",
                    Subscriptions.class);
            Message.SetSP(getBaseContext(),"Pass","switch","OFF");
            Message.SetSP(getBaseContext(),"Pass","switchHide","OFF");
            stopSelf();
        }else{
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
