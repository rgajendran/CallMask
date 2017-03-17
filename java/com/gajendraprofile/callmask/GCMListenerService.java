package com.gajendraprofile.callmask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GCMListenerService extends GcmListenerService {

    DatabaseHelper MyDb;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        if(message.equals("RESET CALLMASK")){
            startService(new Intent(getBaseContext(),AlarmSetter.class));
        }else{
            sendNotification(getBaseContext(),message,Notification.class);
            MyDb = new DatabaseHelper(this);
            AddData(message);
        }
    }

    public static void sendNotification(Context context,String Message, Class clas){
        Intent intent = new Intent(context, clas);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Call Mask")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText(Message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }

    public void AddData(String message){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM HH:mm");
        String currentDateandTime = sdf.format(new Date());
        MyDb.insertData(currentDateandTime, message);
    }



}
