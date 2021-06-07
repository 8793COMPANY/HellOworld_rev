package com.corporation.helloworld.Screen;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;

import com.corporation.helloworld.Service.CheckService;
import com.corporation.helloworld.Share.Application;

public class ViewFragment extends Fragment {

    //변수 선언
    private TextView phone_number,
            open_screen
            ,os_version,
            app_version,
            smart_phone_array,
            first_call,
            calling_phone,
            activitycounter;
    private Button send_message;

    private Application application;

    private TelephonyManager telManager;

    private Cursor cursor;
    private IntentFilter scrOnFilter;

    private int serviceData;

    private Mambogi mambogi;

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view,container,false);
        //초기화
        application =(Application)getActivity().getApplication();
        mambogi = new Mambogi();
        send_message = view.findViewById(R.id.send_message);
        phone_number = view.findViewById(R.id.phone_number);
        open_screen = view.findViewById(R.id.open_screen);
        os_version=view.findViewById(R.id.os_version);
        app_version=view.findViewById(R.id.app_version);
        smart_phone_array =view.findViewById(R.id.smart_phone_array);
        first_call = view.findViewById(R.id.first_call);
        calling_phone = view.findViewById(R.id.calling_phone);
        activitycounter = view.findViewById(R.id.activitycounter);
        telManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);

        //초기 값 설정
        init();

        //보내기 버튼 활성화
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor=application.getInformation_first_1();
                cursor.moveToFirst();
                //통화 횟수 하루전으로 등록
                String sendData = cursor.getString(1)+"/"+cursor.getString(2)+"/" +cursor.getInt(3)+"/"+application.getCallLog()+"/"+android.os.Build.VERSION.SDK_INT+"/"+"1.0"+
                        "/"+CheckService.mStepDetector;

              int count=  get_Count_DB(getActivity());

                Log.e("test",count+"");
                String sql = String.format("INSERT or replace INTO record VALUES("+count+",'"+application.current_Date()+"','" + sendData+"',3);");
                application.dataBase_insert(sql);
                application.sendtoMessage(application.current_Date()+"\n"+sendData);
            }
        });
        return view;
    }*/

    private void init(){
        app_version.setText("1.0");
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (telManager.getLine1Number() != null)
                phone_number.setText(telManager.getLine1Number().replace("+82", "0"));
        }
        smart_phone_array.setText(application.getDeviceName());
        os_version.setText(android.os.Build.VERSION.SDK_INT+"");
        calling_phone.setText(application.getCallLog()+"");
        cursor=application.getInformation_first_1();
        cursor.moveToFirst();
        first_call.setText(cursor.getString(1)+"/"+cursor.getString(2));
        open_screen.setText(cursor.getString(3));
    //    activitycounter.setText(cursor.getInt(5)+"");
      //  activitycounter.setText("0");
        activitycounter.setText(CheckService.mStepDetector+"");

        IntentFilter mainFilter = new IntentFilter("hello_service_mambo");

        getActivity().registerReceiver(mambogi, mainFilter);

    }

    //만보기 실시간 기능 업데이트
    class Mambogi extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceData = intent.getIntExtra("stepService",0);
            activitycounter.setText(serviceData+"");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mambogi);
    }

    public int get_Count_DB(Context context){
        SharedPreferences preferences = context.getSharedPreferences("checkCount", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int count  = preferences.getInt("checkCount",0);
        count +=1;
        if(count > 30){
            editor.putInt("checkCount",1);
            count = 1;
        }else{
            editor.putInt("checkCount",count);
        }
        editor.commit();

        return count;
    }
}