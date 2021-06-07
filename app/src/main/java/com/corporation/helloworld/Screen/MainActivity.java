package com.corporation.helloworld.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.corporation.helloworld.Adapter.VPAdapter;
import com.corporation.helloworld.R;
import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;
import com.google.android.material.tabs.TabLayout;

import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {

    //초기 설정 변수
    private TabLayout mTabLayout;
    private ViewPager pager;
    private VPAdapter vpAdapter;
    private Intent intent;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FragmentTransaction ft;
    private Fragment viewFragment, recordFragment;
    private int resume_Position =0;

    private Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application =(Application)getApplication();

        //새로고침
        vpAdapter = new VPAdapter(getSupportFragmentManager());

        // 권한 요청 및 체크
        checkRequestPermission();
        //

        inital_start_app();
    }

 // 권한 요청
    public void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{READ_CALL_LOG,READ_PHONE_STATE,SEND_SMS,RECEIVE_BOOT_COMPLETED,ACTIVITY_RECOGNITION,RECEIVE_SMS,READ_SMS},
                PERMISSION_REQUEST_CODE);
    }
//권한 거절시 체크 코드
    public void checkRequestPermission(){
        if(ContextCompat.checkSelfPermission(this, READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(this, ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                ||   ContextCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED
        ){
            requestPermission();
        }else {
            permit_after_Setting();
        }

    }


//권한 승인 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {
                permit_after_Setting();
            }
            else {
                Toast.makeText(this, "전화 로그를 불러 올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

        //권한이 승인된 이후에 뷰를 불러오는 메소드
    public void permit_after_Setting()
    {
        intent = new Intent(this, CheckService.class);
        if(CheckService.serviceIntent == null) {
            application.restart_service();
        }else{
            Toast.makeText(this, "서비스 실행중 입니다", Toast.LENGTH_LONG).show();
        }

        viewFragment = new ViewFragment();
        recordFragment = new RecordFragment();
        vpAdapter.addFragment(viewFragment,"오늘의 정보");
        vpAdapter.addFragment(recordFragment,"기록 보관소");
        pager.setAdapter(vpAdapter);
        mTabLayout.setupWithViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Log.e("asdas",position+"");
                resume_Position = position;
                if(position == 0){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(viewFragment).attach(viewFragment).commit();
                }else{
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(recordFragment).attach(recordFragment).commit();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    //휴대폰 화면이 꺼졌을대 화면 현재 화면 갱신
    @Override
    protected void onRestart() {
        super.onRestart();
        if(resume_Position == 0)
        {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(viewFragment).attach(viewFragment).commitAllowingStateLoss();
        }else
        {
            ft = getSupportFragmentManager().beginTransaction();
            ft.detach(recordFragment).attach(recordFragment).commitAllowingStateLoss();
        }
    }


    public MainActivity() {
        super();
    }


    public void inital_start_app()
    {
        SharedPreferences preferences = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean checkFirst = preferences.getBoolean("checkFirst",false);

        if(!checkFirst){
            Log.e("최초 실행","ㅁㄴㅇ");
            application.sendtoMessage("처음 문자입니다.");
            editor.putBoolean("checkFirst",true);
            editor.commit();
        }else {


        }
    }


}
