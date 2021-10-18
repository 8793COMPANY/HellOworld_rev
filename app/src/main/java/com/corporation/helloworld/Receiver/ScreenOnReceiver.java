package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenOnReceiver extends BroadcastReceiver {
    private Application application;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("등록","ㅎㅎ");
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
        String formatDate = sdfNow.format(date);

        if(application == null)
        {
            application = ((Application) context.getApplicationContext());
        }
        // 화면 ON-OFF - 서비스 갱신
     //   Log.e("화면 ON-OFF - 서비스 갱신","굿굿!!");

        Cursor cursor = application.getInformation_first_1();
        cursor.moveToFirst();
        boolean screen_First_Check = cursor.getInt(4) > 0 ;

        if(screen_First_Check)
        {
            Log.e("초기 핸드폰 열음","ㅎㅎ");
            int sum = cursor.getInt(3);
            sum+=1;
            Log.e("sum",sum+"");

            String sql = String.format("update information set firstcall='"+formatDate+"', lastcall='"+formatDate+"',screenopen="+ sum +",screencheck = 0 where _id = 1 ");
            application.dataBase_insert(sql);
        } else {
            String sendData = cursor.getString(1) + "/" + cursor.getString(2) + "/" + cursor.getInt(3) + "/" + android.os.Build.VERSION.SDK_INT + "/" + "0.90" +
                    "/" + CheckService.mStepDetector;
            Log.e("DB 데이터 체크 :: ", sendData);
            //Log.e("통화횟수", application.getCallLog() + "");
            //Log.e("핸드폰 열음","ㅎㅎ");
            int sum = cursor.getInt(3) ;
            sum +=1;
            Log.e("sum",sum+"");

            String sql = String.format("update information set  lastcall='" + formatDate + "' ,screenopen=" + sum + " where _id = 1 ");
            application.dataBase_insert(sql);
        }
    }
}
