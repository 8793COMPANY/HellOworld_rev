package com.corporation.helloworld.Screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.corporation.helloworld.R;
import com.corporation.helloworld.Share.Application;

public class RecordFragment extends Fragment {
    private TextView textView;
    private ListView listView;
    private Application application;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        listView = view.findViewById(R.id.list_view);

        application=(Application)getActivity().getApplication();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,application.select_Record()) ;
        listView.setAdapter(adapter) ;

        return  view;
    }
}