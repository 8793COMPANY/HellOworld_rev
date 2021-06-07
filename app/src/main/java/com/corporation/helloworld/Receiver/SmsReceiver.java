package com.corporation.helloworld.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.corporation.helloworld.Share.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private Application application;
    public void onReceive(Context context, Intent intent) {
        // 수신되었을 때 호출되는 콜백 메서드
        // 매개변수 intent의 액션에 방송의 '종류'가 들어있고
        //         필드에는 '추가정보' 가 들어 있습니다.
        Cursor cursor;
        if(application == null) {
            application = ((Application) context.getApplicationContext());
        }


        // SMS 메시지를 파싱합니다.
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장
        if (bundle != null) { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨

            Object[] pdus = (Object[])bundle.get("pdus");

            SmsMessage[] msgs
                    = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage
                        .createFromPdu((byte[]) pdus[i]);

                        if( msgs[i].getOriginatingAddress().equals("01082673577")) {
//                            01059150020
                            Log.e("equals","-------------");

                            str +=
                                    msgs[i].getMessageBody().toString()
                                    + "\n";

                            JSONObject json = null;
                            try {
                                json  = new JSONObject(str);
                                String identify = json.getString("identify");
                                String date = json.getString("date");
                                cursor = application.get_Track_Record_Db(identify,date);
                                cursor.moveToFirst();

                                long now = System.currentTimeMillis();
                                Date time = new Date(now);
                                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH :mm:ss");
                                String formatDate = sdfNow.format(time).split(" ")[1];
                                int ampm = Integer.parseInt(formatDate);

                                if (identify.equals("A")){
                                    application.sendtoMessage("A/"+cursor.getString(1)+"\n"+cursor.getString(2));
                                }else{
                                    application.sendtoMessage("P/"+cursor.getString(1)+""+cursor.getString(2));
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
            }
            Log.e("testt",str);
        }
    } // end of onReceive
} // end of class

