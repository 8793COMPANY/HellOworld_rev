package com.corporation.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

public class Join extends AppCompatActivity {
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ImageButton join_btn = findViewById(R.id.join_btn);

        join_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Join_input.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });


    }

//11


}