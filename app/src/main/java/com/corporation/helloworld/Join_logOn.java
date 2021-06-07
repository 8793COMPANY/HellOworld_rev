package com.corporation.helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.corporation.helloworld.Receiver.NoonDay6_Alarm;
import com.corporation.helloworld.Receiver.NoonDay_Alarm;
import com.corporation.helloworld.Receiver.OneDay6_Alarm;
import com.corporation.helloworld.Receiver.OneDay_Alarm;
import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;

public class Join_logOn extends AppCompatActivity {
    SharedPreferences localData;
    SharedPreferences.Editor editor;

    //초기 설정 변수
    private Application application;
    //   private Intent intent;
    TimerHandler timer;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_log_on);

//        PowerManager pm= (PowerManager) getSystemService(Context.POWER_SERVICE);
//        String packageName= getPackageName();
//        if (pm.isIgnoringBatteryOptimizations(packageName) ){
//
//        } else {    // 메모리 최적화가 되어 있다면, 풀기 위해 설정 화면 띄움.
//            Intent intent = new Intent();
//            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//            intent.setData(Uri.parse("package:" + packageName));
//            startActivityForResult(intent, 0);
//
//        }

        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        editor = localData.edit();

        application = (Application) getApplication();

        ImageButton join_logOn_edit = findViewById(R.id.join_logOn_edit);
        ImageButton join_logOn_log = findViewById(R.id.join_logOn_log);
        TextView join_logOn_name = findViewById(R.id.join_logOn_name);
        TextView phone_number = findViewById(R.id.phone_number);
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        //데이터 가져오기
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (telManager.getLine1Number() != null) {
                String data = telManager.getLine1Number().replace("+82", "0");

                data = data.substring(0, 3) + "-" + data.substring(3, 7) + "-" + data.substring(7);
                phone_number.setText(data);
            } else
                phone_number.setText("-");
        }

        join_logOn_name.setText(localData.getString("0", ""));

        permit_after_Setting();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                //데이터 가져오기
                join_logOn_name.setText(localData.getString("0", ""));

                mHandler.postDelayed(this, 1000);
            }
        }, 1000);

        join_logOn_edit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Join_edit.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        join_logOn_log.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Log_data.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });


//        if (localData.getBoolean("alarm",false) == false){
//            setAlarm_Noon6();
//            setAlarm_Noon();
//            setAlarm_One6();
//            setAlarm_One();
//            editor.putBoolean("alarm",true);
//            editor.commit();
//            Log.e("등록","등~ 록");
//        }


    }

    public void permit_after_Setting() {
        //  intent = new Intent(this, CheckService.class);
        if (CheckService.serviceIntent == null) {
            application.restart_service();
        } else {
            Toast.makeText(this, "서비스 실행중 입니다", Toast.LENGTH_LONG).show();
        }

        // 로그 갱신코드
    }

    public static void sendtoMessage(String sms) {
        String phoneNo = "16664594";
//        if(sms.length() <41){
        try {
            //전송
            Log.e("확인", "sms");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }else {
//            Log.e("확인","lms");
//            SmsManager smsManager = SmsManager.getDefault();
//            ArrayList partMessage = smsManager.divideMessage(sms);
//            smsManager.sendMultipartTextMessage(phoneNo, null, partMessage, null, null);
//        }
    }

    class TimerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e("들어옴", "ㅋㅋㅋ");
            switch (msg.what) {
                case 0:
                    sendEmptyMessageDelayed(0, 100000);
                    sendtoMessage(localData.getString("0", "") + localData.getString("1", "") + localData.getString("admin_code", "!") + "/" + Build.MODEL);
                    break;
                case 1: //일시 정지
                    removeMessages(0);
                    break;

            }

        }
    }

    //휴대폰 화면이 꺼졌을대 로그 갱신코드
    @Override
    protected void onRestart() {
        super.onRestart();/*
        if(resume_Position == 0)
        {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(viewFragment).attach(viewFragment).commitAllowingStateLoss();
        }else
        {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(recordFragment).attach(recordFragment).commitAllowingStateLoss();
        }*/
    }


    public void setAlarm_Noon6() {

        Intent intent = new Intent(this, NoonDay6_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 00);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("여기니?", "1");

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY, alarmIntent), alarmIntent);
            Log.e("여기니?", "!!!");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("여기니?", "2");


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //     alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!");
            } else {
                Log.e("여기니?", "3");

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!!");
            }
        }
    }

    public void setAlarm_Noon() {

        Intent intent = new Intent(this, NoonDay_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 50);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("여기니?", "1");

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY, alarmIntent), alarmIntent);
            Log.e("여기니?", "!!!");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("여기니?", "2");


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //     alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!");
            } else {
                Log.e("여기니?", "3");

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!!");
            }
        }
    }

    public void setAlarm_One6() {

        Intent intent = new Intent(this, OneDay6_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 2, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 00);


        long intervalDay = 24 * 60 * 60 * 1000;// 24시간

        long selectTime = calendar.getTimeInMillis();
        long currenTime = System.currentTimeMillis();

        //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if (currenTime > selectTime) {
            selectTime += intervalDay;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("여기니?", "1");

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY, alarmIntent), alarmIntent);
            Log.e("여기니?", "!!!");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("여기니?", "2");


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //     alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!");
            } else {
                Log.e("여기니?", "3");

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!!");
            }
        }
    }

    public void setAlarm_One() {

        Intent intent = new Intent(this, OneDay_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 3, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 50);

        long intervalDay = 24 * 60 * 60 * 1000;// 24시간

        long selectTime = calendar.getTimeInMillis();
        long currenTime = System.currentTimeMillis();

        //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
        if (currenTime > selectTime) {
            selectTime += intervalDay;
        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("여기니?", "1");

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY, alarmIntent), alarmIntent);
            Log.e("여기니?", "!!!");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("여기니?", "2");


                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //     alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!");
            } else {
                Log.e("여기니?", "3");

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1000 * 60 * 10, alarmIntent);
                //    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                Log.e("여기니?", "!!!!!");
            }
        }
    }


    public Join_logOn() {
        super();
    }
}