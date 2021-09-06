package com.corporation.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.corporation.helloworld.Share.Application;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECEIVE_MMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Alert_perm extends AppCompatActivity {
    private Intent intent;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_perm);

        ImageButton alert_perm_btn = findViewById(R.id.alert_perm_btn);
        alert_perm_btn.setOnClickListener(v -> {
            // 권한 요청 및 체크
            checkRequestPermission();
        });


        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);

            boolean isWhiteListing = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
            }
            if (!isWhiteListing) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }

    }



    // 권한 요청
    public void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{READ_EXTERNAL_STORAGE,READ_CALL_LOG,READ_PHONE_STATE,SEND_SMS,RECEIVE_BOOT_COMPLETED,ACTIVITY_RECOGNITION,RECEIVE_SMS,READ_SMS,RECEIVE_MMS,WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    // 권한 거절시 체크 코드
    public void checkRequestPermission(){
        if(ContextCompat.checkSelfPermission(this, READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                ||   ContextCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        ){


            requestPermission();
        }else {

            permit_after_Setting();
        }

    }


    // 권한 승인 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {
                permit_after_Setting();
            }
            else {
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //권한이 승인된 이후에 뷰를 불러오는 메소드
    public void permit_after_Setting()
    {
     //   application = (Application) getApplication();
    //    application.setLogSetting();

    //     org.apache.log4j.Logger mLogger = org.apache.log4j.Logger.getLogger(Alert_perm.class);
  //      mLogger.debug("로그 테스트");

/*        intent = new Intent(this, CheckService.class);
        if(CheckService.serviceIntent == null) {
            application.restart_service();
        }else{
            Toast.makeText(this, "서비스 실행중 입니다", Toast.LENGTH_LONG).show();
        }*/
        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (telManager.getLine1Number() != null) {
            //    Intent intent = new Intent(getApplicationContext(), Alert_agree_1.class);
                Intent intent = new Intent(getApplicationContext(), Alert_agree_1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            else {
                //TODO: 미개통 스마트폰 처리
                Intent intent = new Intent(getApplicationContext(), Alert_cell.class);
                //Intent intent = new Intent(getApplicationContext(), Alert_agree_1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }
    }
}