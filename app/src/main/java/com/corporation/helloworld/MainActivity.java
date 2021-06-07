package com.corporation.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import com.corporation.helloworld.Share.Application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences localData;
    private Application application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(application == null) {
//            application = ((Application) getApplicationContext());
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);

        //세이브 체크
        if (localData.getBoolean("is_Save", false)) {
            startLoading2();
        } else {
            startLoading();
        }

//        String sendData = "18:00" + "/" + "11:48" + "/" + 614 + "/" + application.getCallLog() + "/" + android.os.Build.VERSION.SDK_INT + "/" + "1.0" +
//                "/" + CheckService.mStepDetector;
//
//        application.dataBase_insert("INSERT or replace INTO record VALUES("+0+",'" + "2020-01-02" + "','" + sendData + "',2);");
//
//        sendData = "17:00" + "/" + "10:00" + "/" + 500 + "/" + application.getCallLog() + "/" + android.os.Build.VERSION.SDK_INT + "/" + "1.0" +
//                "/" + CheckService.mStepDetector;
//
//        application.dataBase_insert("INSERT or replace INTO record VALUES("+1+",'" + "2020-01-04" + "','" + sendData + "',3);");
//
//        sendData = "15:00" + "/" + "09:48" + "/" + 432 + "/" + application.getCallLog() + "/" + android.os.Build.VERSION.SDK_INT + "/" + "1.0" +
//                "/" + CheckService.mStepDetector;
//
//        application.dataBase_insert("INSERT or replace INTO record VALUES("+2+",'" + "2020-01-05" + "','" + sendData + "',2);");
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Alert_perm.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void startLoading2() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Toast.makeText(MainActivity.this, "데이터를 불러왔습니다", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), Join_logOn.class);
            intent.putExtra("name", localData.getString("0", ""));

            startActivity(intent);
            finish();
        }, 2000);
    }
}