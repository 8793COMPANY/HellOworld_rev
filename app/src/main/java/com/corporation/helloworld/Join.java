package com.corporation.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Join extends AppCompatActivity {
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ImageButton join_btn = findViewById(R.id.join_btn);
        ImageView join_call_me_2 = findViewById(R.id.join_call_me_2);

        join_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Join_input.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        join_call_me_2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sayhello.co.kr/privacy"));
            startActivity(intent);
        });
    }

//11


}