package com.gajendraprofile.callmask;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;

public class MainActivity extends Activity{

    private EditText editPhone, et_hideShowNum;
    private Switch aSwitch, hSwitch;
    private TextView numdays;
    private Button refill, permission,privacy;
    private Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);
        tf = Typeface.createFromAsset(getAssets(), "font1.ttf");
        final EditText editText = (EditText)findViewById(R.id.sharedPassHolder);
        editPhone = (EditText)findViewById(R.id.sharedNumberHolder);
        et_hideShowNum = (EditText)findViewById(R.id.hideshowNumber);
        privacy = (Button)findViewById(R.id.privacy);
        et_hideShowNum.setEnabled(false);
        permission = (Button)findViewById(R.id.permission);
        AdView adView2 = (AdView)findViewById(R.id.adViewSub2);
        AdView adView3 = (AdView)findViewById(R.id.adViewSub3);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        AdRequest adRequest3 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);
        adView3.loadAd(adRequest3);

        ImageButton passBtn = (ImageButton)findViewById(R.id.sharedPassButton);
        ImageButton phoneBtn = (ImageButton)findViewById(R.id.sharedNumberButton);
        ImageButton contactBtn = (ImageButton)findViewById(R.id.chooseContact);
        ImageButton hideShowBtn = (ImageButton)findViewById(R.id.hideshowButton);
        hideShowBtn.setEnabled(false);
        setStatus();
        init();
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1001);
            }
        });

        aSwitch = (Switch)findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int value = Message.GetInt(getBaseContext(), "Sub", "Days", 0);

                if (isChecked) {
                    // if(value > 0) {
                    Message.SetSP(getBaseContext(), "Pass", "switch", "ON");
                    MainActivity.toast(getBaseContext(), "Auto-Delete is ON");
                    //}else{
                    //   Message.toast(getBaseContext(),"Please renew your subscriptions");
                    //  aSwitch.setChecked(false);
                    // }
                } else {
                    Message.SetSP(getBaseContext(), "Pass", "switch", "OFF");
                    MainActivity.toast(getBaseContext(), "Auto-Delete is OFF");
                }
                setStatus();

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("https://docs.google.com/document/d/1cUViC5vjwu2GfCOFzMA6eTT8bGSA0QhNblyYpg29P5c/pub"));
                startActivity(myWebLink);
            }
        });

        hSwitch = (Switch)findViewById(R.id.hideSwitch);
        hSwitch.setEnabled(false);
        hSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int value = Message.GetInt(getBaseContext(),"Sub","Days",0);

                if(isChecked){
                   // if(value > 0) {
                    Message.SetSP(getBaseContext(),"Pass","switchHide","ON");
                    MainActivity.toast(getBaseContext(), "HideApp is ON");
                  //  }else{
                 //       Message.toast(getBaseContext(),"Please renew your subscriptions");
                  //      hSwitch.setChecked(false);
                 //   }
                }else{
                    Message.SetSP(getBaseContext(), "Pass", "switchHide", "OFF");
                    MainActivity.toast(getBaseContext(), "HideApp is OFF");
                }
                setStatus();

            }
        });

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALL_LOG,Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
            }
        });

        String text = Message.GetSP(getBaseContext(), "Pass", "login", "");
        String phone = Message.GetSP(getBaseContext(), "Pass", "phone", "");
        String switchs = Message.GetSP(getBaseContext(), "Pass", "switch", "OFF");
        String switchHide = Message.GetSP(getBaseContext(), "Pass", "switchHide", "OFF");
        String hideShow = Message.GetSP(getBaseContext(), "Pass", "hide", "");
        editText.setText(text);
        editPhone.setText(phone);
        et_hideShowNum.setText(hideShow.replace("*",""));

        if(switchs.equals("ON")){
            aSwitch.setChecked(true);
        }
        if(switchHide.equals("ON")){
            hSwitch.setChecked(true);
        }
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().trim().equals("") && editText.getText().length() >= 4) {
                    Message.SetSP(getBaseContext(), "Pass", "login", editText.getText().toString());
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Message.toast(getBaseContext(),"Password should be more than 4 characters");
                }
                setStatus();
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.SetSP(getBaseContext(), "Pass", "phone", editPhone.getText().toString());
                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                setStatus();

            }
        });

        hideShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_hideShowNum.getText().toString().trim().equals("") && et_hideShowNum.getText().length() == 4){
                    Message.SetSP(getBaseContext(), "Pass", "hide", "*"+et_hideShowNum.getText().toString());
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Message.SetSP(getBaseContext(),"Pass","hide","");
                    Toast.makeText(getBaseContext(), "Invalid Code", Toast.LENGTH_SHORT).show();
                }

                setStatus();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
       // setNumdays();
        setStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1001:

                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        Cursor c = null;
                        try {
                            c = getContentResolver().query(uri, new String[]{
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.TYPE },
                                    null, null, null);

                            if (c != null && c.moveToFirst()) {
                                String number = c.getString(0);
                                int type = c.getInt(1);
                                String remo = number.replace(" ", "");
                                editPhone.setText(remo);
                            }
                        } finally {
                            if (c != null) {
                                c.close();
                            }
                        }
                    }
                }

                break;

        }

    }

    public static void toast(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }


    public void init(){
        int[] word = {R.id.heading1,R.id.textView4,R.id.textView11,R.id.textView3,R.id.textView1,R.id.textView22,
                R.id.textView44,R.id.textView5,R.id.textViewD,R.id.textView2,R.id.textViewPhone,R.id.heading2,
                R.id.textViewHideText,R.id.hideshowNumber,R.id.textView,R.id.textViewLogin,R.id.textViewHide, R.id.permission, R.id.privacy};
        for(int i =0; i<word.length; i++){
            TextView tv = (TextView)findViewById(word[i]);
            tv.setTypeface(tf);
        }
        int[] editT = {R.id.sharedPassHolder,R.id.sharedNumberHolder};
        for(int i =0; i<editT.length; i++){
            EditText et = (EditText)findViewById(editT[i]);
            et.setTypeface(tf);
        }
    }

    public void setStatus(){
        TextView app_password = (TextView)findViewById(R.id.tv_app_password);
        TextView auto_delete_number = (TextView)findViewById(R.id.tv_auto_delete);
        TextView auto_delete_enabled = (TextView)findViewById(R.id.tv_auto_delete_enabled);
        TextView hide_app_enabled = (TextView)findViewById(R.id.hide_app_enabled);
        TextView hide_number_set = (TextView)findViewById(R.id.hide_number_set);
        TextView developer_service = (TextView)findViewById(R.id.developer_service);
        Switch sw = (Switch)findViewById(R.id.switch1);
        sw.setTypeface(tf);
        app_password.setTypeface(tf);
        auto_delete_number.setTypeface(tf);
        auto_delete_enabled.setTypeface(tf);
        hide_app_enabled.setTypeface(tf);
        hide_number_set.setTypeface(tf);
        developer_service.setTypeface(tf);

        String text = Message.GetSP(getBaseContext(), "Pass", "login", "");
        String phone = Message.GetSP(getBaseContext(), "Pass", "phone", "");
        String switchs = Message.GetSP(getBaseContext(), "Pass", "switch", "OFF");
        String switchHide = Message.GetSP(getBaseContext(), "Pass", "switchHide", "OFF");
        String hideShow = Message.GetSP(getBaseContext(), "Pass", "hide", "");
        String gcm = Message.GetSP(getBaseContext(), "GCM", "Token", "NOTSET");

        if(!text.equals("")){
            app_password.setText("ON");
            app_password.setBackgroundResource(R.color.light_green);
        }else{
            app_password.setText("OFF");
            app_password.setBackgroundResource(R.color.light_red);
        }

        if(!phone.equals("")){
            auto_delete_number.setText("ON");
            auto_delete_number.setBackgroundResource(R.color.light_green);
        }else{
            auto_delete_number.setText("OFF");
            auto_delete_number.setBackgroundResource(R.color.light_red);
        }

        if(!switchs.equals("OFF")){
            auto_delete_enabled.setText("ON");
            auto_delete_enabled.setBackgroundResource(R.color.light_green);
        }else{
            auto_delete_enabled.setText("OFF");
            auto_delete_enabled.setBackgroundResource(R.color.light_red);
        }

        if(!switchHide.equals("OFF")){
            hide_app_enabled.setText("ON");
            hide_app_enabled.setBackgroundResource(R.color.light_green);
        }else{
            hide_app_enabled.setText("OFF");
            hide_app_enabled.setBackgroundResource(R.color.light_red);
        }

        if(!hideShow.equals("")){
            hide_number_set.setText("ON");
            hide_number_set.setBackgroundResource(R.color.light_green);
        }else{
            hide_number_set.setText("OFF");
            hide_number_set.setBackgroundResource(R.color.light_red);
        }

        if(!gcm.equals("NOTSET")){
            developer_service.setText("ON");
            developer_service.setBackgroundResource(R.color.light_green);
        }else{
            developer_service.setText("Click to Activate");
            developer_service.setBackgroundResource(R.color.light_red);
            developer_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message.toast(getBaseContext(), "Check your internet and restart CallMask");
                    Intent intents = new Intent(getBaseContext(), RegistrationIntentService.class);
                    startService(intents);
                }
            });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
