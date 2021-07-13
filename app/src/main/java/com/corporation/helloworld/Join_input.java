package com.corporation.helloworld;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class Join_input extends AppCompatActivity {
    ImageButton join_input_upload_btn;

    SharedPreferences localData;
    SharedPreferences.Editor editor;

    Boolean gender_m, gender_f;
    Boolean[] check_items;
    String[] data_items;

    LinearLayout join_input_area_layout, join_input_ymd_layout;
    TextView join_input_ymd;
    Spinner join_input_area_spinner;

    LinearLayout show_admin_layout_1, show_admin_layout_2;
    EditText admin_code_edit;

    AlertDialog.Builder builder;
    DatePicker date_picker;

    int y, m, d;

    private TelephonyManager telManager;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    String myphone,msg,first_Msg="",second_Msg="";

    String [] disease = new String[4];
    String [] phoneNum = new String[3];



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_input);


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Calendar cal = Calendar.getInstance();

        // 레이아웃 변수 초기화
        show_admin_layout_1 = findViewById(R.id.show_admin_layout_1);
        show_admin_layout_2 = findViewById(R.id.show_admin_layout_2);
        admin_code_edit = findViewById(R.id.admin_code_edit);

        join_input_upload_btn = findViewById(R.id.join_input_upload_btn);
        ImageButton join_input_back_btn = findViewById(R.id.join_input_back_btn);

        EditText join_input_name = findViewById(R.id.join_input_name);

        Button join_input_gender_m = findViewById(R.id.join_input_gender_m);
        Button join_input_gender_f = findViewById(R.id.join_input_gender_f);
        gender_m = false;
        gender_f = false;

        join_input_ymd_layout = findViewById(R.id.join_input_ymd_layout);
        join_input_ymd = findViewById(R.id.join_input_ymd);

        join_input_area_layout = findViewById(R.id.join_input_area_layout);
        join_input_area_spinner = findViewById(R.id.join_input_area_spinner);

        ArrayAdapter locationAdapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_dropdown_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        join_input_area_spinner.setAdapter(locationAdapter);

        EditText join_input_area_detail = findViewById(R.id.join_input_area_detail);
        EditText join_input_disease_1 = findViewById(R.id.join_input_disease_1);
        EditText join_input_disease_2 = findViewById(R.id.join_input_disease_2);
        EditText join_input_disease_3 = findViewById(R.id.join_input_disease_3);
        EditText join_input_disease_4 = findViewById(R.id.join_input_disease_4);
        EditText join_input_nok_1_type = findViewById(R.id.join_input_nok_1_type);
        EditText join_input_nok_1_tel = findViewById(R.id.join_input_nok_1_tel);
        EditText join_input_nok_2_type = findViewById(R.id.join_input_nok_2_type);
        EditText join_input_nok_2_tel = findViewById(R.id.join_input_nok_2_tel);
        EditText join_input_nok_3_type = findViewById(R.id.join_input_nok_3_type);
        EditText join_input_nok_3_tel = findViewById(R.id.join_input_nok_3_tel);
        AtomicReference<TextView> phone_number = new AtomicReference<>(findViewById(R.id.phone_number));
        telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_date_picker, null);
        builder.setView(dialogView);

        y = cal.get(cal.YEAR);
        m = cal.get(cal.MONTH);
        d = cal.get(cal.DAY_OF_MONTH);

        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickPos(y, m + 1, d);
            }
        });

        date_picker = dialogView.findViewById(R.id.date_picker);
        date_picker.init(y, m, d, (view, year, monthOfYear, dayOfMonth) -> {
            y = year;
            m = monthOfYear;
            d = dayOfMonth;
        });

        AlertDialog dialog = builder.create();



        // 데이터 가져오기
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (telManager.getLine1Number() != null) {
                String data = telManager.getLine1Number().replace("+82", "0");

                data = data.substring(0, 3) + "-" + data.substring(3, 7) + "-" + data.substring(7);
                phone_number.get().setText(data);
            }
            else {
                phone_number.get().setText("-");
            }
        }

        localData = getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        editor = localData.edit();


        check_items = new Boolean[] {
                false, false, false, false,
                false, false, false, false,
                false, false, false, false,
                false, false, false
        };

        data_items = new String[] {
                "", "", "", "",
                "", "", "", "",
                "", "", "", "",
                "", "", ""
        };

        // 업로드 버튼 눌렀을때
        join_input_upload_btn.setOnClickListener(v -> {
            // 데이터 가져오기
            data_items[0] = join_input_name.getText().toString();
            data_items[2] = join_input_ymd.getText().toString();
            data_items[3] = (String)join_input_area_spinner.getSelectedItem();
            data_items[4] = join_input_area_detail.getText().toString();
            data_items[5] = join_input_disease_1.getText().toString();
            data_items[6] = join_input_disease_2.getText().toString();
            data_items[7] = join_input_disease_3.getText().toString();
            data_items[8] = join_input_disease_4.getText().toString();
            data_items[9] = join_input_nok_1_type.getText().toString();
            data_items[10] = join_input_nok_1_tel.getText().toString();
            data_items[11] = join_input_nok_2_type.getText().toString();
            data_items[12] = join_input_nok_2_tel.getText().toString();
            data_items[13] = join_input_nok_3_type.getText().toString();
            data_items[14] = join_input_nok_3_tel.getText().toString();


            data_items[2] = data_items[2].replace(" ","").replace("년","").replace("월","").replace("일","");
            msg ="";

            int count=0;
            String total_word="";
            boolean check = true;
            //나머지
            for(int i=5; i<15; i++){
                Log.e("data_items[i]",data_items[i]);
                Log.e("count",count+"");
                Log.e("zzzzzzzzzzzzzzzzzz","????????");
                if (i < 9){
                    disease[count] = data_items[i];
                    count++;
                    if (count ==4)
                        count=0;
                }else if (i>8 && i %2 != 0){
                    if (!data_items[i].equals("")){
                        total_word = data_items[i]+"-";
                    }

                }else if (i>8 && i %2 == 0){
                    total_word += data_items[i];
                    phoneNum[count] = total_word;
                    count++;
                    total_word ="";
                }
            }

//           이름 성별 날짜 주소
            for(int i=0; i<5; i++){
                Log.e("data["+i+"] = ",data_items[i]);
                Log.e("i",i+"");
              if(data_items[i].equals("")){

                }else{
                  if (i==3){
                      first_Msg += data_items[i]+"_";
                  }else if (i==4){
                      // 21-07-13 개선사항 요청
                      // 첫번째 메시지 - 마지막 슬래쉬 문자 제거
                      first_Msg += data_items[i];
                  } else {
                      first_Msg += data_items[i]+"/";
                  }
                }
            }

            //

            for (int i=0; i<disease.length; i++){
                Log.e("dis",disease[i]);
                if(disease[i].equals("")){

                }else{
                        second_Msg += disease[i]+"_";
//                    second_Msg += disease[i];
                }

                if (i == disease.length-1){
                    // 21-07-13 개선사항 요청
                    // 마지막 메시지 - 마지막 슬래쉬 문자 제거
                    second_Msg = second_Msg.substring(0, second_Msg.length()-1);
                }

            }

            for (int i=0; i<phoneNum.length; i++){
                Log.e("phone["+i+"]",phoneNum[i]);
                if(phoneNum[i].equals("")){

                }else{
                        msg += phoneNum[i]+"_";
                }

                if (i == phoneNum.length-1)
                    msg = msg.substring(0, msg.length()-1);

            }

            Log.e("msg",msg);

            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);


            // 세이브
            editor = localData.edit();

            int index = 0;
            String total ="";
            for (String di : data_items) {
                editor.putString(Integer.toString(index), di);
                Log.i("저장", "SAVE : DATA => " + index + " | " + di);

                index++;
            }
            editor.putBoolean("is_Save", true);
            editor.commit();

            Toast.makeText(this, "업로드되었습니다", Toast.LENGTH_SHORT).show();
            Log.i("저장", "SAVE : onClick and Saved! => " + localData.getBoolean("is_Save", false));

            // 화면 전환
            Intent intent = new Intent(getApplicationContext(), Admin_code.class);
            intent.putExtra("msg", msg);
            intent.putExtra("first_Msg",first_Msg);
            intent.putExtra("second_Msg",second_Msg);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });

        // 초기 업로드 버튼 비 활성화
        join_input_upload_btn.setClickable(false);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canUpload()) {
                    join_input_upload_btn.setBackground(getDrawable(R.drawable.join_input_upload_btn));
                } else {
                    join_input_upload_btn.setBackground(getDrawable(R.drawable.join_input_upload_btn_2));
                    join_input_upload_btn.setClickable(false);
                }

                mHandler.postDelayed(this, 1000);
            }
        }, 1000);

        // 성별 : 남
        join_input_gender_m.setOnClickListener(v -> {
            gender_m = true;
            gender_f = false;
            check_items[1] = true;
            data_items[1] = "남";

            if (gender_m) {
                join_input_gender_m.setBackground(getDrawable(R.drawable.join_input_int_on));
                join_input_gender_m.setTextColor(Color.WHITE);
                join_input_gender_f.setBackground(getDrawable(R.drawable.join_input_int_off));
                join_input_gender_f.setTextColor(0xFFa7a7a7);
            }
        });

        // 성별 : 여
        join_input_gender_f.setOnClickListener(v -> {
            gender_m = false;
            gender_f = true;
            check_items[1] = true;
            data_items[1] = "여";

            if (gender_f) {
                join_input_gender_f.setBackground(getDrawable(R.drawable.join_input_int_on));
                join_input_gender_f.setTextColor(Color.WHITE);
                join_input_gender_m.setBackground(getDrawable(R.drawable.join_input_int_off));
                join_input_gender_m.setTextColor(0xFFa7a7a7);
            }
        });

        // 한글자라도 입력하면 인풋창 활성화
        // 사용자 이름
        join_input_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_name.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[0] = true;
                }
                else {
                    join_input_name.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[0] = false;
                }
            }
        });

        // 생년월일
        join_input_ymd_layout.setOnClickListener(v -> {
            dialog.show();
        });

        // 거주지역
        join_input_area_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    join_input_area_layout.setBackground(getDrawable(R.drawable.join_input_double_on));
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    check_items[3] = true;
                } else {
                    join_input_area_layout.setBackground(getDrawable(R.drawable.join_input_double_off));
                    ((TextView) parent.getChildAt(0)).setTextColor(0xFFa7a7a7);
                    ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    check_items[3] = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                join_input_area_spinner.setSelection(0);
            }
        });
        join_input_area_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_area_detail.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[4] = true;
                }
                else {
                    join_input_area_detail.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[4] = false;
                }
            }
        });

        // 병력기록 (직접입력)
        join_input_disease_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_disease_1.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[5] = true;
                }
                else {
                    join_input_disease_1.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[5] = false;
                }
            }
        });
        join_input_disease_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_disease_2.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[6] = true;
                }
                else {
                    join_input_disease_2.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[6] = false;
                }
            }
        });
        join_input_disease_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_disease_3.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[7] = true;
                }
                else {
                    join_input_disease_3.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[7] = false;
                }
            }
        });
        join_input_disease_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_disease_4.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[8] = true;
                }
                else {
                    join_input_disease_4.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[8] = false;
                }
            }
        });

        // 보호자 연락처
        join_input_nok_1_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_1_type.setBackground(getDrawable(R.drawable.join_input_int_on));
                    check_items[9] = true;
                }
                else {
                    join_input_nok_1_type.setBackground(getDrawable(R.drawable.join_input_int_off));
                    check_items[9] = false;
                }
            }
        });
        join_input_nok_1_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_1_tel.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[10] = true;
                }
                else {
                    join_input_nok_1_tel.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[10] = false;
                }
            }
        });
        join_input_nok_2_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_2_type.setBackground(getDrawable(R.drawable.join_input_int_on));
                    check_items[11] = true;
                }
                else {
                    join_input_nok_2_type.setBackground(getDrawable(R.drawable.join_input_int_off));
                    check_items[11] = false;
                }
            }
        });
        join_input_nok_2_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_2_tel.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[12] = true;
                }
                else {
                    join_input_nok_2_tel.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[12] = false;
                }
            }
        });
        join_input_nok_3_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_3_type.setBackground(getDrawable(R.drawable.join_input_int_on));
                    check_items[13] = true;
                }
                else {
                    join_input_nok_3_type.setBackground(getDrawable(R.drawable.join_input_int_off));
                    check_items[13] = false;
                }
            }
        });
        join_input_nok_3_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    join_input_nok_3_tel.setBackground(getDrawable(R.drawable.join_input_double_on));
                    check_items[14] = true;
                }
                else {
                    join_input_nok_3_tel.setBackground(getDrawable(R.drawable.join_input_double_off));
                    check_items[14] = false;
                }
            }
        });

        // 뒤로가기 버튼
        join_input_back_btn.setOnClickListener(v -> finish());

    }

    // 업로드 버튼 활성화
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean canUpload() {
        Log.i("로그", "canUpload: RUN !");
        int i = 0;
        int necessary = 0;
        int optional1 = 0;
        int optional2 = 0;
        for (boolean b : check_items) {
            if (b == true && i < 5) {
                necessary++;
            } else if (b == true && ((4 < i) && (i < 9)) ) {
                optional1++;
            } else if (b == true && ((8 < i) && (i < 15)) ) {
                optional2++;
            }
            i++;
        }

        if (necessary == 5
        && optional1 > 0
        && optional2 > 1) {
            // 업로드 버튼 활성화
            join_input_upload_btn.setClickable(true);
            Log.i("로그", "canUpload: BTN ENABLE !");
            return true;
        } else {
            Log.i("로그", "canUpload: No condition ! nec : " + necessary + " / opt1 : " + optional1 + " / opt2 : " + optional2);
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickPos(int y, int m, int d) {
        join_input_ymd.setText(y + "년 " + m + "월 " + d + "일 ");
        join_input_ymd_layout.setBackground(getDrawable(R.drawable.join_input_double_on));
        check_items[2] = true;
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