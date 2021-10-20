package com.corporation.helloworld;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.corporation.helloworld.Adapter.VPAdapter;
import com.corporation.helloworld.Screen.RecordFragment;
import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;

public class Log_data extends AppCompatActivity {

    //초기 설정 변수
    private ViewPager pager;
    private VPAdapter vpAdapter;
    private Intent intent;
    private FragmentTransaction ft;
    private Fragment recordFragment;
    private TextView current_log;
    LinearLayout current_log_for_tester, layout_for_tester, log_data_title_back_2;
    Button btn_for_tester;

    // TODO : 테스터용 현재 로그 노출
    // 사용하려면 true 로 변경해주세요
    Boolean admin_mode = true;

    private Application application;

    private String phone_number,
            open_screen
            ,os_version,
            app_version,
            smart_phone_array,
            first_call,
            calling_phone,
            activitycounter;

    private TelephonyManager telManager;
    private Cursor cursor;
    private IntentFilter scrOnFilter;
    private int serviceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_data);

        Button log_data_close = findViewById(R.id.log_data_close);
        current_log_for_tester = findViewById(R.id.current_log_for_tester);
        log_data_title_back_2 = findViewById(R.id.log_data_title_back_2);
        layout_for_tester = findViewById(R.id.layout_for_tester);
        btn_for_tester = findViewById(R.id.btn_for_tester);

        application =(Application)getApplication();
        pager = findViewById(R.id.main_viewPager);
        current_log = findViewById(R.id.current_log);
        pager.setOffscreenPageLimit(0);

        //새로고침
        vpAdapter = new VPAdapter(getSupportFragmentManager());
        init();

        permit_after_Setting();

        if (admin_mode) {
            layout_for_tester.setVisibility(View.VISIBLE);
        } else {
            layout_for_tester.setVisibility(View.GONE);
        }
        log_data_title_back_2.setVisibility(View.VISIBLE);
        current_log_for_tester.setVisibility(View.GONE);

        log_data_close.setOnClickListener(v -> {
            finish();
        });

        btn_for_tester.setOnClickListener(v -> {
            admin_mode = !admin_mode;

            if (!admin_mode) {
                current_log_for_tester.setVisibility(View.VISIBLE);
                log_data_title_back_2.setVisibility(View.GONE);
            } else {
                current_log_for_tester.setVisibility(View.GONE);
                log_data_title_back_2.setVisibility(View.VISIBLE);
            }
        });
    }

    public void permit_after_Setting()
    {
//        intent = new Intent(this, CheckService.class);
//        if(CheckService.serviceIntent == null) {
//            application.restart_service();
//        }else{
//            Toast.makeText(this, "서비스 실행중 입니다", Toast.LENGTH_LONG).show();
//        }
        recordFragment = new RecordFragment();
        vpAdapter.addFragment(recordFragment,"기록 보관소");
        pager.setAdapter(vpAdapter);
    }

    //휴대폰 화면이 꺼졌을대 화면 현재 화면 갱신
    @Override
    protected void onRestart() {
        super.onRestart();
        ft = getSupportFragmentManager().beginTransaction();
        ft.detach(recordFragment).attach(recordFragment).commitAllowingStateLoss();
        init();
    }

    public Log_data() {
        super();
    }

    public void init() {
        app_version = ("1.0");
        smart_phone_array = (application.getDeviceName());
        os_version = (android.os.Build.VERSION.SDK_INT+"");
        //calling_phone = (application.getCallLog()+"");
        cursor=application.getInformation_first_1();
        cursor.moveToFirst();
        first_call = (cursor.getString(1)+"/"+cursor.getString(2));
        open_screen = (cursor.getString(3));
        activitycounter = (CheckService.mStepDetector+"");

        current_log.setText(
                        "첫 : " + cursor.getString(1) + "\n" +
                        "마 : " + cursor.getString(2) + "\n" +
                        "횟 : " + open_screen + "\n" +
                        "걸 : " + activitycounter + "\n" +
                        "OS 버젼 : " + os_version + "\n" +
                        "APP 버젼 : " + BuildConfig.VERSION_CODE);
    }
}