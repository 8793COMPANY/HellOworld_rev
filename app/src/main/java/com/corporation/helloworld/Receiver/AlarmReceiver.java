package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.corporation.helloworld.Share.Application;

public class AlarmReceiver extends BroadcastReceiver {
    private Application application;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(application == null)
        {
            application = ((Application) context.getApplicationContext());
        }
         application.restart_service();
    }
}
