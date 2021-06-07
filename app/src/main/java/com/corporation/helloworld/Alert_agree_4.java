package com.corporation.helloworld;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Alert_agree_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_agree_4);

        ImageButton Alert_agree_no = findViewById(R.id.Alert_agree_no);
        ImageButton Alert_agree_yes = findViewById(R.id.Alert_agree_yes);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Alert_agree_yes.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Alert_agree_5.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        Alert_agree_no.setOnClickListener(v -> {
            Toast.makeText(Alert_agree_4.this, "약관에 동의해주세요", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(100); // 0.5초간 진동
        });
    }
}