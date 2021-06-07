package com.corporation.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.Toast;

public class Alert_agree_1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_agree_1);

        ImageButton Alert_agree_no = findViewById(R.id.Alert_agree_no);
        ImageButton Alert_agree_yes = findViewById(R.id.Alert_agree_yes);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Alert_agree_yes.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Alert_agree_2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        });

        Alert_agree_no.setOnClickListener(v -> {
            Toast.makeText(Alert_agree_1.this, "약관에 동의해주세요", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(100); // 0.5초간 진동
        });
    }
}