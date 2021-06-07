package com.corporation.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class Alert_cell extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_cell);

        ImageButton alert_cell_btn = findViewById(R.id.alert_cell_btn);

        alert_cell_btn.setOnClickListener(v -> {
/*            Intent intent = new Intent(getApplicationContext(), Alert_perm.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*/
            Toast.makeText(this, "어플을 종료합니다", Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }
}