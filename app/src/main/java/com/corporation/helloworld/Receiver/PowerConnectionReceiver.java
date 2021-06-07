package com.corporation.helloworld.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

import com.corporation.helloworld.Interface.BatteryResultCallback;
import com.corporation.helloworld.Share.Application;

public class PowerConnectionReceiver extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Intent intent;
    private Application application;


    public BatteryResultCallback batteryResultCallback;
    public  PowerConnectionReceiver(BatteryResultCallback batteryResultCallback){

        this.batteryResultCallback =  batteryResultCallback;
    }

    public  PowerConnectionReceiver(){ }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(application == null) {
            application = ((Application) context.getApplicationContext());
                }
                try {
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int batteryLevel = (int) (((float) level / (float) scale) * 100.0f);
                    if(batteryLevel<5) {
                        if (application.battery_one_more) {
                            Log.e("알람등록2", "알람등록2");
                    application.sendtoMessage("배터리 5%로 이하입니다");
                    application.battery_one_more = false;
                    alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(context, BatteryCheckReceiver.class);
                    alarmIntent = PendingIntent.getBroadcast(context, 2, intent, 0);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                alarmMgr.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 60 * 1000*60, alarmIntent), alarmIntent);
                            }else{

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60*1000*60,alarmIntent);
                                }else{
                                    alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60*1000*60,alarmIntent);
                                }
                            }



//                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            SystemClock.elapsedRealtime() +
//                                    (60*1000*5), alarmIntent);
                }
            }
//            }else if(batteryLevel < 99)
//            {
//                if(Application.first_Battery_Check) {
//                    Application.send("배터리 5%로 이하입니다");
//                    Application.first_Battery_Check = false;
//                }
//                if(Application.battery_one_more){
//                    Log.e("알람등록2","알람등록2");
//                    Application.send("배터리 5%로 이하입니다");
//                    Application.battery_one_more = false;
//                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                    intent = new Intent(context, AlarmReceiver.class);
//                    intent.putExtra("id",2);
//                    alarmIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
//                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            SystemClock.elapsedRealtime() +
//                                    (60 * 1000), alarmIntent);
//                }
//            }else{
//                Application.first_Battery_Check = true;
//            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
