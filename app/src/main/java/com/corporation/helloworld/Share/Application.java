package com.corporation.helloworld.Share;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import com.corporation.helloworld.DB.RecordDB;
import com.corporation.helloworld.ExceptionHandler;
import com.corporation.helloworld.Receiver.NoonDay6_Alarm;
import com.corporation.helloworld.Receiver.NoonDay_Alarm;
import com.corporation.helloworld.Receiver.OneDay6_Alarm;
import com.corporation.helloworld.Receiver.OneDay_Alarm;
import com.corporation.helloworld.Service.CheckService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Application extends android.app.Application {
    //db관련 정보
    final String dbName = "record_hello_2";
    final int dbVersion=1;
    public RecordDB recordDB;
    public SQLiteDatabase db;
    public static boolean battery_one_more = true;
    public static boolean first_Battery_Check = true;


   public SharedPreferences localData;
   public SharedPreferences.Editor editor;


    private  SharedPreferences pref;

    //쿼리 관련 sql
        public String selse_Information_1 = "SELECT * FROM information where _id = 1;";
        private String select_Record_All = "SELECT * FROM information ;";


        private ExceptionHandler exceptionHandler;

        @Override
        public void onCreate() {
            super.onCreate();
            init();

        }

    public void init(){
            recordDB = new RecordDB(this,dbName,null,dbVersion);
            pref = getApplicationContext().getSharedPreferences("service_hello", MODE_PRIVATE);
    }

    public void setLogSetting(){
        final LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()+ "/logfile.log");
        //   logConfigurator.setFilePattern(pattern);
        logConfigurator.configure();

        exceptionHandler = new ExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }


    //커서로 디비 호출
    public Cursor getInformation_first_1(){
        db = recordDB.getReadableDatabase();
        Cursor cursor = db.rawQuery(selse_Information_1, null);

        return cursor;
    }

    //db데이터 입력
    public void dataBase_insert(String sql){
        db = recordDB.getWritableDatabase();
        db.execSQL(sql);
    }

    //db 기록 데이터 불러오기
    public  List<String> select_Record(){
        db = recordDB.getReadableDatabase();
        String select_sql = "SELECT * FROM record order by _id desc;";
        final Cursor cursor = db.rawQuery(select_sql, null);
        String [] LIST_MENU = new String[cursor.getCount()];
        List<String> menu = new ArrayList<>();
        while (cursor.moveToNext()){
            menu.add(cursor.getInt(0)+" "+cursor.getString(1)+"\n "+cursor.getString(2));
        }
        cursor.close();
        Log.e("DB 로그", "select_Record: " + menu);

        return menu;
    }

    public Cursor get_All_Record(){

        db = recordDB.getReadableDatabase();
        Cursor cursor = db.rawQuery(select_Record_All, null);

        return cursor;
    }

    public Cursor get_Track_Record_Db(String identify,String date){
        Cursor cursor = null;
        db = recordDB.getReadableDatabase();
        if (identify.equals("A")){
            cursor = db.rawQuery("select * from record where identify ="+2+" and date ='"+date+"';", null);
        }else if (identify.equals("P")){
            cursor = db.rawQuery("select * from record where identify ="+3+" and date ='"+date+"';", null);
        }

        return cursor;
    }

    //디바이스 이름 불러오기
    public  String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }
    // 날짜 불러오기
    public String current_Date(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);

        return  year+"-"+month+"-"+day;
    }


    //하루에 한번씩 보내는 전송 저녘 23:50
    public void setAlarm( )
    {
        Log.e("등록","등록");
        Intent intent = new Intent(this, OneDay_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.HOUR_OF_DAY)>23){
                calendar.add(Calendar.DATE, 1);
        } else if(calendar.get(Calendar.HOUR_OF_DAY) ==23){
            if(calendar.get(Calendar.MINUTE)>=50) {
                 calendar.add(Calendar.DATE, 1);
             }
        }
      //  calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
          //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY, alarmIntent), alarmIntent);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent), alarmIntent);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
              //  alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);

            }else{
               // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+AlarmManager.INTERVAL_DAY,alarmIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }
        }
    }




    //하루에 한번씩 보내는 전송 오후 5:50
    public void setAlarm_Noon( )
    {

        Intent intent = new Intent(this, NoonDay_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)>5){
            calendar.add(Calendar.DATE, 1);
        } else if(calendar.get(Calendar.HOUR_OF_DAY) ==5){
            if(calendar.get(Calendar.MINUTE)>=50) {
                calendar.add(Calendar.DATE, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent), alarmIntent);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }
        }
    }

    //하루에 한번씩 보내는 전송 오후 11:50
    public void setAlarm_Noon_test1( )
    {

        Intent intent = new Intent(this, NoonDay6_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)>11){
            calendar.add(Calendar.DATE, 1);
        } else if(calendar.get(Calendar.HOUR_OF_DAY) ==11){
            if(calendar.get(Calendar.MINUTE)>=50) {
                calendar.add(Calendar.DATE, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent), alarmIntent);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }
        }
    }


    //하루에 한번씩 보내는 전송 오후 17:50
    public void setAlarm_Noon_test2( )
    {

        Intent intent = new Intent(this, OneDay6_Alarm.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)>17){
            calendar.add(Calendar.DATE, 1);
        } else if(calendar.get(Calendar.HOUR_OF_DAY) ==17){
            if(calendar.get(Calendar.MINUTE)>=50) {
                calendar.add(Calendar.DATE, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent), alarmIntent);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            }
        }
    }



    // CallLog를 반환합니다. (오늘의 전화 횟수를 카운터 합니다)
    public int getCallLog() {
            int miss_call =0;
        int call_Count=0;
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    miss_call++;
                    break;
            }
            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
            String [] arrays = format1.format(callDayTime).split(" ");

            if (arrays[0].trim().equals(current_Date().trim())){
                call_Count++;
//                long seconds=Long.parseLong(callDate);
//                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//                String dateString = formatter.format(new Date(seconds));
            }else{
                managedCursor.moveToLast();
            }
        }
        call_Count = call_Count - miss_call;

        if(call_Count < 0){
            call_Count = 0;
        }
        return call_Count;
    }




    //메세지 전송 기능
    public static void sendtoMessage(String sms) {
        //    String phoneNo ="01035472357";
        //String phoneNo ="01052505443";
        String phoneNo = "16664594";
            try {
                Log.e("hi","daily");
                //전송
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public void set_Mambogi(int count){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("mambo",count);
        editor.apply();
    }

    public int get_Mambogi(){
     return pref.getInt("mambo",0);
    }

    public void restart_service() {
        Log.e("재시작","호호");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            Intent in = new Intent(this, CheckService.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(!isMyServiceRunning(CheckService.class)) {
                startForegroundService(in);
            }

        } else {
            Intent in = new Intent(this, CheckService.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(!isMyServiceRunning(CheckService.class)) {
                startService(in);
            }
        }
    }


    public void put_Shared_Alaram(boolean check){
        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        editor = localData.edit();
        editor.putBoolean("alarm",check);
        editor.commit();

    }


    public boolean get_shared_Alaram(){

        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        return localData.getBoolean("alarm",false);
    }


    public void service_Cancle(){
                stopService(CheckService.serviceIntent);

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public  String encryption(String content){
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<content.length(); i++)
        {
            String code = content.substring(i,i+1);

            if(code.equals(":")||code.equals("-")||code.equals(".")||code.equals("/"))
            {
                builder.append(code);
            }else{

                switch (code) {
                    case "1":
                        builder.append("B");
                        break;
                    case "2":
                        builder.append("A");
                        break;
                    case "3":
                        builder.append("D");
                        break;
                    case "4":
                        builder.append("C");
                        break;
                    case "5":
                        builder.append("F");
                        break;
                    case "6":
                        builder.append("E");
                        break;
                    case "7":
                        builder.append("H");
                        break;
                    case "8":
                        builder.append("G");
                        break;
                    case "9":
                        builder.append("I");
                        break;
                    case "0":
                        builder.append("J");
                        break;
                    case "없":
                        builder.append("없음");
                        break;
                }
            }
        }
        return builder.toString();
    }



}
