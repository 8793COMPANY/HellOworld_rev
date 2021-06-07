package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.corporation.helloworld.Share.Application;

public class BatteryCheckReceiver extends BroadcastReceiver {
    private Application application;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(application == null)
        {
            application = ((Application) context.getApplicationContext());
        }
        Application.battery_one_more=true;
        Intent i = new Intent(context.getApplicationContext(),PowerConnectionReceiver.class);
        application.sendBroadcast(i);
    }
}
