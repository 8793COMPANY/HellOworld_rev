package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.corporation.helloworld.Share.Application;

public class UpdateReceiver extends BroadcastReceiver {
    private Application application;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);

                String versionName = info.versionName;
                int versionCode = info.versionCode;

                Toast.makeText(context, "versionName : " + versionName + " code : " + versionCode, Toast.LENGTH_SHORT).show();

                // 서비스 재시작 코드 추가
                if(application == null)
                {
                    application = ((Application) context.getApplicationContext());
                }
                application.restart_service();

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
