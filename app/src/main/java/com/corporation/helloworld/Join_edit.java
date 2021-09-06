package com.corporation.helloworld;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import java.util.ArrayList;
import java.util.Calendar;

public class Join_edit extends AppCompatActivity {
    ImageButton join_input_upload_btn;

    SharedPreferences localData;
    SharedPreferences.Editor editor;

    Boolean gender_m, gender_f;
    Boolean[] check_items;
    String[] data_items;

    LinearLayout join_input_area_layout, join_input_ymd_layout, show_back_btn;
    TextView join_input_ymd;
    Spinner join_input_area_spinner;

    LinearLayout show_admin_layout_1, show_admin_layout_2;
    EditText admin_code_edit;

    AlertDialog.Builder builder;
    DatePicker date_picker;

    int y, m, d;
    String msg;

    String [] disease = new String[4];
    String [] phoneNum = new String[3];

    private TelephonyManager telManager;

    String first_Msg="",second_Msg="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_input);


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

        // 수정시에만 취소버튼 노출
        show_back_btn = findViewById(R.id.show_back_btn);
        show_back_btn.setVisibility(View.VISIBLE);

        // 수정시에만 어드민코드 노출
        show_admin_layout_1.setVisibility(View.VISIBLE);
        show_admin_layout_2.setVisibility(View.VISIBLE);

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
        TextView phone_number = findViewById(R.id.phone_number);
        telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_date_picker, null);
        builder.setView(dialogView);

        // YMD 기본값 설정
        y = 1950;
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
                phone_number.setText(data);
            }
            else {
                phone_number.setText("-");
            }
        }

        // 데이터 가져오기
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

        // 세이브 체크
        if (localData.getBoolean("is_Save", false)) {
            // 로딩
            int index = 0;
            for (String di : data_items) {
                data_items[index] = localData.getString(Integer.toString(index), "");
                if (data_items[index] == null || data_items[index] == "") {
                    check_items[index] = false;
                } else {
                    check_items[index] = true;
                }
                index++;
            }

            // 어드민코드 셋-데이터
            admin_code_edit.setText(localData.getString("admin_code", ""));
            admin_code_edit.setBackground(getDrawable(R.drawable.join_input_double_on));

            // 셋-데이터
            join_input_name.setText(data_items[0]);
            join_input_name.setBackground(getDrawable(R.drawable.join_input_double_on));

            join_input_ymd.setText(data_items[2]);
            join_input_ymd_layout.setBackground(getDrawable(R.drawable.join_input_double_on));

            join_input_area_spinner.setSelection(locationAdapter.getPosition(data_items[3]));
            join_input_area_layout.setBackground(getDrawable(R.drawable.join_input_double_on));

            join_input_area_detail.setText(data_items[4]);
            join_input_area_detail.setBackground(getDrawable(R.drawable.join_input_double_on));

            join_input_disease_1.setText(data_items[5]);
            if (!(data_items[5].equals("")))
                join_input_disease_1.setBackground(getDrawable(R.drawable.join_input_double_on));
            join_input_disease_2.setText(data_items[6]);
            if (!(data_items[6].equals("")))
                join_input_disease_2.setBackground(getDrawable(R.drawable.join_input_double_on));
            join_input_disease_3.setText(data_items[7]);
            if (!(data_items[7].equals("")))
                join_input_disease_3.setBackground(getDrawable(R.drawable.join_input_double_on));
            join_input_disease_4.setText(data_items[8]);
            if (!(data_items[8].equals("")))
                join_input_disease_4.setBackground(getDrawable(R.drawable.join_input_double_on));

            join_input_nok_1_type.setText(data_items[9]);
            if (!(data_items[9].equals("")))
                join_input_nok_1_type.setBackground(getDrawable(R.drawable.join_input_int_on));
            join_input_nok_1_tel.setText(data_items[10]);
            if (!(data_items[10].equals("")))
                join_input_nok_1_tel.setBackground(getDrawable(R.drawable.join_input_double_on));
            join_input_nok_2_type.setText(data_items[11]);
            if (!(data_items[11].equals("")))
                join_input_nok_2_type.setBackground(getDrawable(R.drawable.join_input_int_on));
            join_input_nok_2_tel.setText(data_items[12]);
            if (!(data_items[12].equals("")))
                join_input_nok_2_tel.setBackground(getDrawable(R.drawable.join_input_double_on));
            join_input_nok_3_type.setText(data_items[13]);
            if (!(data_items[13].equals("")))
                join_input_nok_3_type.setBackground(getDrawable(R.drawable.join_input_int_on));
            join_input_nok_3_tel.setText(data_items[14]);
            if (!(data_items[14].equals("")))
                join_input_nok_3_tel.setBackground(getDrawable(R.drawable.join_input_double_on));

            if (data_items[1].equals("남")) {
                gender_m = true;
                gender_f = false;

                join_input_gender_m.setBackground(getDrawable(R.drawable.join_input_int_on));
                join_input_gender_m.setTextColor(Color.WHITE);
                join_input_gender_f.setBackground(getDrawable(R.drawable.join_input_int_off));
                join_input_gender_f.setTextColor(0xFFa7a7a7);
            } else {
                gender_m = false;
                gender_f = true;

                join_input_gender_f.setBackground(getDrawable(R.drawable.join_input_int_on));
                join_input_gender_f.setTextColor(Color.WHITE);
                join_input_gender_m.setBackground(getDrawable(R.drawable.join_input_int_off));
                join_input_gender_m.setTextColor(0xFFa7a7a7);
            }
        } else {

        }

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




            for(int i=0; i<5; i++){
                Log.e("data["+i+"] = ",data_items[i]);
                Log.e("i",i+"");
                if(data_items[i].equals("")){

                }else{
                    if (i==3){
                        first_Msg += data_items[i]+"_";
                    }else{
                        first_Msg += data_items[i]+"/";
                    }

                }

            }

            for (int i=0; i<disease.length; i++){
                Log.e("dis",disease[i]);
                if(disease[i].equals("")){

                }else{
                    second_Msg += disease[i]+"_";
                }

                if (i == disease.length-1){
                    second_Msg = second_Msg.substring(0, second_Msg.length()-1)+"/";
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

            // 세이브
            editor = localData.edit();

            int index = 0;
            for (String di : data_items) {
                editor.putString(Integer.toString(index), di);
                Log.i("저장", "SAVE : DATA => " + index + " | " + di);
                index++;
            }
            editor.putBoolean("is_Save", true);
            editor.putString("admin_code", admin_code_edit.getText().toString());
            editor.commit();


            Toast.makeText(this, "업로드되었습니다", Toast.LENGTH_SHORT).show();
            Log.i("저장", "SAVE : onClick and Saved! => " + localData.getBoolean("is_Save", false));
            Log.i("저장", localData.getString("admin_code","!")+"");
            Log.i("저장",msg);

            sendtoMessage("!"+first_Msg);
            sendtoMessage("@"+second_Msg);
            sendtoMessage("#"+msg);
            sendtoMessage("$"+localData.getString("admin_code","!")+"/"+ Build.MODEL);


          //  sendtoMessage(msg+"/"+localData.getString("admin_code","!")+"/"+ Build.MODEL);
           // sendMMS(msg+"/"+localData.getString("admin_code","!")+"/"+ Build.MODEL);
            // 화면 전환
            finish();
        });

        // 초기 업로드 버튼 활성화
        join_input_upload_btn.setClickable(false);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        // 어드민코드
        admin_code_edit.addTextChangedListener(new TextWatcher() {
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
                    admin_code_edit.setBackground(getDrawable(R.drawable.join_input_double_on));
                }
                else {
                    admin_code_edit.setBackground(getDrawable(R.drawable.join_input_double_off));
                }
            }
        });
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


        txt += "/모든 데이터는 암호화 되어 전송됩니다";
//            for (int i =0; i<91-txtByte; i++){
//                txt += "/";
//            }

        return txt;
    }


    public static void sendtoMessage(String sms) {

        String phoneNo = "16664594";
        Log.e("확인","lms");

        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList partMessage = smsManager.divideMessage(sms);
            smsManager.sendMultipartTextMessage(phoneNo, null, partMessage, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
