package com.corporation.helloworld.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;

public class NoonDay6_Alarm extends BroadcastReceiver {
    private Application application;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(application == null) {
            application = ((Application) context.getApplicationContext());
        }


        Cursor cursor = application.get_All_Record();
        cursor.moveToFirst();
        if (!cursor.getString(1).equals("배터리 5% 이하입니다.")) {
            cursor = application.getInformation_first_1();
            cursor.moveToFirst();
            String sendData = cursor.getString(1) + "/" + cursor.getString(2) + "/" + cursor.getInt(3) + "/" + application.getCallLog() + "/" + android.os.Build.VERSION.SDK_INT + "/" + "0.90" +
                    "/" + CheckService.mStepDetector;

            application.dataBase_insert("INSERT or replace INTO record VALUES("+get_Count_DB(context)+",'" +application.encryption( application.current_Date()) + "','" + application.encryption(sendData) + "',1);");
            //  application.dataBase_insert("insert of replace into record(_id,date,senddata) values ((select _id from record where id ="+get_Count_DB(context)+" ))");

            application.dataBase_insert("update information set firstcall='없음', lastcall='없음',screenopen=0,screencheck = 1,activitycount=0 where _id = 1 ");
            application.sendtoMessage("A2/"+application.encryption(application.current_Date() + "/" + sendData));
//            application.setAlarm_Noon_6();
   //         application.setAlarm_Noon_test1();
        }
    }

    public int get_Count_DB(Context context){
        SharedPreferences preferences = context.getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int count  = preferences.getInt("checkCount",0);
        count +=1;
        if(count > 30){
            editor.putInt("checkCount",1);
            count = 1;
        }else{
            editor.putInt("checkCount",count);
        }
        editor.commit();
        return count;
    }
}
