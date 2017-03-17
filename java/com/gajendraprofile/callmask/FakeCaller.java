package com.gajendraprofile.callmask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class FakeCaller extends ActionBarActivity {

    private int year;
    private int month;
    private int day, hour, min,sec;
    static final int DATE_PICKER_ID = 1111;
    static final int TIME_PICKER_ID = 1112;
    EditText et_date, et_time, et_phone, et_secTalk;
    String in_phone, in_date, in_time, in_duration;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_caller);
        tf = Typeface.createFromAsset(getAssets(), "font1.ttf");
        Button btn_add = (Button)findViewById(R.id.btn_add_fake_call);
        TextView tv = (TextView)findViewById(R.id.textView3);
        ImageButton btn_choosedate = (ImageButton)findViewById(R.id.btn_choose_date);
        ImageButton btn_choosetime = (ImageButton)findViewById(R.id.btn_choose_time);
        ImageButton btn_choosePhone = (ImageButton)findViewById(R.id.btn_choose_phone);
        AdView adView1 = (AdView)findViewById(R.id.adViewSub1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_secTalk = (EditText)findViewById(R.id.et_min);
        et_time = (EditText)findViewById(R.id.et_time);
        et_date = (EditText)findViewById(R.id.et_date);
        tv.setTypeface(tf);
        et_phone.setTypeface(tf);
        et_secTalk.setTypeface(tf);
        et_time.setTypeface(tf);
        et_date.setTypeface(tf);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        RadioButton btn1 = (RadioButton)findViewById(R.id.incoming);
        RadioButton btn2 = (RadioButton)findViewById(R.id.outgoing);
        btn1.setTypeface(tf);
        btn2.setTypeface(tf);
        btn_add.setTypeface(tf);

        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Show current date

        et_date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month+1).append("-")
                .append(year));

        et_time.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(hour).append(":").append(min));


        btn_choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        btn_choosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_PICKER_ID);
            }
        });

        btn_choosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1001);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // insertPlaceholderCall(getContentResolver(), pNum.getText().toString(), SecMin.getText().toString());

                int selectionId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)findViewById(selectionId);

                in_phone = et_phone.getText().toString();
                in_date = et_date.getText().toString();
                in_time = et_time.getText().toString();
                in_duration = et_secTalk.getText().toString();

                if(!in_phone.isEmpty() && !in_date.isEmpty() && !in_time.isEmpty() && !in_duration.isEmpty()){
                   int type = 0;
                    if(radioButton.getText().toString().equals("Incoming"))
                    {
                        type = CallLog.Calls.INCOMING_TYPE;
                    }else if(radioButton.getText().toString().equals("Outgoing"))
                    {
                        type = CallLog.Calls.OUTGOING_TYPE;
                    }

                    String dateSplit[] = in_date.split("-");
                    String timeSplit[] = in_time.split(":");

                    int dd = Integer.valueOf(dateSplit[0]);
                    int mm = Integer.valueOf(dateSplit[1]);
                    int yyyy = Integer.valueOf(dateSplit[2]);
                    int hh = Integer.valueOf(timeSplit[0]);
                    int min = Integer.valueOf(timeSplit[1]);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date date = null;
                    long timeMilliseconds;
                    try {
                        date = dateFormat.parse(String.valueOf(yyyy + "-" + mm + "-" + dd + " " + hh + ":" + min + ":" + "00"));
                        dateFormat.setTimeZone(TimeZone.getDefault());
                        timeMilliseconds = date.getTime();
                        insertPlaceholderCall(getBaseContext(),getContentResolver(), in_phone, in_duration, timeMilliseconds, type);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }else{
                    Toast.makeText(getBaseContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);

            case TIME_PICKER_ID:

                return new TimePickerDialog(this, timerListerner,hour, sec, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            et_date.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year));

        }
    };

    private TimePickerDialog.OnTimeSetListener timerListerner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           hour = hourOfDay;
           min = minute;

            et_time.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(hour).append(":").append(min));

        }
    };

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
                                et_phone.setText(number);
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

    public static void insertPlaceholderCall(Context context,ContentResolver contentResolver, String number, String duration, long date, int type){
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, date);
        values.put(CallLog.Calls.DURATION, duration);
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.NEW, 1);
        values.put(CallLog.Calls.CACHED_NAME, "");
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
        Log.d("TAG", "Number :" + number + "Duration :" + duration + "Date :" + date + "Type :" + String.valueOf(type));
        try {
            contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
            Message.toast(context, "Added :" + number + " to your call logs");
        }catch(Exception e){
            Message.toast(context, "Invalid Input, Please try again");
        }

    }

}
