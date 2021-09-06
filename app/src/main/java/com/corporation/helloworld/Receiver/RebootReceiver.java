package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.corporation.helloworld.Share.Application;

public class RebootReceiver extends BroadcastReceiver {
    private Application application;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(application == null) {
            application = ((Application) context.getApplicationContext());
        }
        Log.e("bootreceiver","boot");

        if (intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            application.restart_service();
            application.setAlarm_Noon();
            application.setAlarm_Noon_test1();
            application.setAlarm_Noon_test2();
            application.setAlarm();

            application.sendtoMessage("재설치 완료");
        }

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Log.e("bootreceiv11111er","boot");

            application.restart_service();

            application.setAlarm_Noon();
            application.setAlarm_Noon_test1();
            application.setAlarm_Noon_test2();
            application.setAlarm();


            application.sendtoMessage("재부팅 완료");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Intent in = new Intent(context, CheckService.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startForegroundService(in);
//            } else {
//                Intent in = new Intent(context, CheckService.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startService(in);
//            }


        }
    }
}
