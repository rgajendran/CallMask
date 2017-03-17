package com.gajendraprofile.callmask;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.DecimalFormat;
import java.util.Calendar;

import static com.gajendraprofile.callmask.Message.GetSP;
import static com.gajendraprofile.callmask.Message.GetSPBool;
import static com.gajendraprofile.callmask.Message.GetSPBool;
import static com.gajendraprofile.callmask.Message.SetInt;
import static com.gajendraprofile.callmask.Message.SetSP;


public class welcome_start extends ActionBarActivity {


    ContentObserver mContentObserver;
    Button activate, add_call, btn_noti;
    DatabaseHelper myDb;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);
        tf = Typeface.createFromAsset(getAssets(), "font1.ttf");
        activate = (Button)findViewById(R.id.activate);
        add_call = (Button)findViewById(R.id.btn_add_call);
        btn_noti = (Button)findViewById(R.id.btn_notification);
        activate.setTypeface(tf);
        add_call.setTypeface(tf);
        btn_noti.setTypeface(tf);
        AdView adView1 = (AdView)findViewById(R.id.adViewSub1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        myDb = new DatabaseHelper(this);
        String activate_text = com.gajendraprofile.callmask.Message.GetSP(getBaseContext(), "Pass", "login", "0s3242e2");
        checkForAddCall();
        checkNotification();
        if(activate_text.equals("0s3242e2")){
            activate.setText("Activate");

        }else{
            activate.setText("Open Settings");
        }

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(welcome_start.this)).inflate(R.layout.activity_login, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(welcome_start.this);
                alertBuilder.setView(view);
                final EditText editText = (EditText) view.findViewById(R.id.alertTextPass);
                final TextView alertTV = (TextView) view.findViewById(R.id.alertTextView);
                alertTV.setTypeface(tf);
                editText.setTypeface(tf);
                if (com.gajendraprofile.callmask.Message.GetSP(getBaseContext(), "Pass", "login", "6293462946396").equals("6293462946396")) {
                    alertTV.setText("Please enter the passcode.\nDefault Password : CallMask");
                }
                alertBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("Pass", Context.MODE_PRIVATE);
                        if (sp.getString("login", "001010010101010").equals(editText.getText().toString())) {
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        } else if (sp.getString("login", "6293462946396").equals("6293462946396")) {
                            if (editText.getText().toString().equals("CallMask")) {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), "Invalid Password, Try Again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Invalid Password, Try Again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Dialog dialog = alertBuilder.create();
                dialog.show();

            }
        });

        add_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(welcome_start.this)).inflate(R.layout.activity_login, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(welcome_start.this);
                alertBuilder.setView(view);
                final TextView alertTV = (TextView) view.findViewById(R.id.alertTextView);
                alertTV.setTypeface(tf);
                final EditText editText = (EditText) view.findViewById(R.id.alertTextPass);
                editText.setTypeface(tf);

                alertBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("Pass", Context.MODE_PRIVATE);
                        if (sp.getString("login", "001010010101010").equals(editText.getText().toString())) {
                            Intent intent = new Intent(getBaseContext(), FakeCaller.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(), "Invalid Password (or) Password not set", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

        if(!GetSPBool(getBaseContext(), "GCM", "GCMSet", false)) {
            if (com.gajendraprofile.callmask.Message.GetSP(getBaseContext(), "Text", "GooglePlay", "NO").equals("NO")) {
                if (checkPlayServices()) {
                    Intent intents = new Intent(getBaseContext(), RegistrationIntentService.class);
                    startService(intents);
                }
            }
        }

        btn_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage(welcome_start.this, "No Notification received");
                    return;
                } else {
                    startActivity(new Intent(getBaseContext(), com.gajendraprofile.callmask.Notification.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotification();
        checkForAddCall();
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getBaseContext());
        if(resultCode != ConnectionResult.SUCCESS){
            if(apiAvailability.isUserResolvableError(resultCode)){
                apiAvailability.getErrorDialog(this,resultCode,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                finish();
            }
            return false;
        }
        return true;
    }

    public static void showMessage(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Notification");
        builder.setMessage(message);
        builder.show();

    }

    public void checkForAddCall(){
        if (com.gajendraprofile.callmask.Message.GetSP(getBaseContext(), "Pass", "login", "6293462946396").equals("6293462946396")) {
            add_call.setEnabled(false);
        }else{
            add_call.setEnabled(true);
        }
    }

    public void checkNotification(){
        if(com.gajendraprofile.callmask.Message.GetSP(getBaseContext(), "GCM", "Token", "NOTSET").equals("NOTSET")){
            btn_noti.setEnabled(false);
        }else{
            add_call.setEnabled(true);
        }
    }
}
