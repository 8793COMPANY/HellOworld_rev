package com.corporation.helloworld.Service;

import static com.squareup.seismic.ShakeDetector.SENSITIVITY_LIGHT;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.corporation.helloworld.Interface.BatteryResultCallback;
import com.corporation.helloworld.MainActivity;
import com.corporation.helloworld.R;
import com.corporation.helloworld.Receiver.AlarmReceiver;
import com.corporation.helloworld.Receiver.PowerConnectionReceiver;
import com.corporation.helloworld.Receiver.ScreenOnReceiver;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.corporation.helloworld.Share.Application;
import com.squareup.seismic.ShakeDetector;

public class CheckService extends Service implements SensorEventListener, BatteryResultCallback, ShakeDetector.Listener {

    public static Intent serviceIntent;
    public static int mStepDetector;
    // public static boolean battery_one_more = true;

    private ScreenOnReceiver screenOnReceiver;
    //리시버 등록
    private IntentFilter scrOnFilter;

    //정보 불러오기 위한 cursor
    private Cursor cursor;

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private Sensor stepDetectorSensor;

    //application class 호출
    private Application application;


    //배터리 체크 필터
    private IntentFilter intentFilter;
    private PowerConnectionReceiver battary;

    //시간 체크
    int time_Count = 0;

    private boolean batterycheck = false;


    SharedPreferences localData;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        initializeNotification();
        init();


        //  register_Manbogi();

       // return START_REDELIVER_INTENT;
         return START_NOT_STICKY;
    }

    //서비스 초기화 설정
    public void init() {
        application = (Application) getApplication();
        mStepDetector = application.get_Mambogi();
        cursor = application.getInformation_first_1();

        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        battary = new PowerConnectionReceiver(this);
        this.registerReceiver(battary, intentFilter);


        scrOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenOnReceiver = new ScreenOnReceiver();
        registerReceiver(screenOnReceiver, scrOnFilter);

        // targetSdkVersion 29 초과하면 에러 발생합니다 !!
        application.setLogSetting();


        if (application.get_shared_Alaram() == false) {
            Log.e("check", "check");
            application.setAlarm_Noon();
            application.setAlarm_Noon_test1();
            application.setAlarm_Noon_test2();
            application.setAlarm();

            application.put_Shared_Alaram(true);
        }
        register_Manbogi();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    // 서비스 초기화 부분
    public void initializeNotification() {

        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.bigText("실행중입니다.");
            style.setBigContentTitle(null);
            style.setSummaryText("서비스 동작중");
            builder.setContentText(null);
            builder.setContentTitle(null);
            builder.setOngoing(true);
            builder.setStyle(style);
            builder.setWhen(0);
            builder.setShowWhen(false);
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(new NotificationChannel("1", "service_hello", NotificationManager.IMPORTANCE_DEFAULT));
            }
            Notification notification = builder.build();
            startForeground(1, notification);
        } else {
            // 낮은 OS 에서 노티-알림 (ex : 폴더폰 등등..)
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
            builder.setSmallIcon(R.mipmap.ic_launcher);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.bigText("실행중입니다.");
            style.setBigContentTitle("안녕하세요");
            style.setSummaryText("서비스 동작중");
            builder.setContentText(null);
            builder.setContentTitle(null);
            builder.setOngoing(true);
            builder.setStyle(style);
            builder.setWhen(0);
            builder.setShowWhen(false);
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(new NotificationChannel("1", "service_hello", NotificationManager.IMPORTANCE_DEFAULT));
            }
            Notification notification = builder.build();
            startForeground(1, notification);
        }
    }


    //만보기 기능의 프리시져
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                mStepDetector += event.values[0];
                // mStepDetector = (int)event.values[0];
                Log.e("스텝 디텍터", "" + mStepDetector);
                application.set_Mambogi(mStepDetector);
                Intent myFilteredResponse = new Intent("hello_service_mambo");
                myFilteredResponse.putExtra("stepService", mStepDetector);
                sendBroadcast(myFilteredResponse);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    //만보기 기능
    public void register_Manbogi() {
        Log.e("만보기", "만보기능");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor == null) {
            // 대체 만보기 가동
            /*
            ShakeDetector sd = new ShakeDetector(this);
            sd.setSensitivity(SENSITIVITY_LIGHT);
            sd.start(sensorManager);
             */
        } else {
            // 오리지널 만보기 가동
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_GAME);
            // 대체 만보기 가동
            /*
            ShakeDetector sd = new ShakeDetector(this);
            sd.setSensitivity(SENSITIVITY_LIGHT);
            sd.start(sensorManager);
             */
        }
    }


    // 서비스 종료시 재호출 코드
    @Override
    public void onTaskRemoved(Intent rootIntent) {
//        Log.e("서비스 실행 aaaa", "서비스 실행aaaa");
//        super.onTaskRemoved(rootIntent);
//
//        Log.e("서비스 종료", "서비스 종료");
//        Toast.makeText(this, "안녕하세요가 작동중입니다.", Toast.LENGTH_SHORT).show();
//        final Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 3);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), sender), sender);
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//            }
//        }
    }

    //알람 등로기능
    @Override
    public void callDelegate(int batteryLevel) {
    }


    @Override
    public void onDestroy() {
        Log.e("서비스 실행 el", "서비스 실행");
        super.onDestroy();
        Log.e("서비스 실행", "서비스 실행");
        serviceIntent = null;

        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(battary);
        setAlarmTimer();
   //     onTaskRemoved(serviceIntent);
    }



    protected void setAlarmTimer() {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), sender), sender);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
            } else {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
            }
        }
    }

    // 대체 만보기 프리시져
    @Override
    public void hearShake() {
        mStepDetector = application.get_Mambogi() + 1;
        Log.e("대체 스텝 디텍터", "" + mStepDetector);
        application.set_Mambogi(mStepDetector);
    }
}
