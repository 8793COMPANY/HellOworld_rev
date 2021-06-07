package com.corporation.helloworld.Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class VPAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> items = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    FragmentManager fm;

    public VPAdapter(FragmentManager fm) {
        super(fm);
        this.fm= fm;
    }

    public void addFragment(Fragment fragment, String title) {
        items.add(fragment);
        mFragmentTitleList.add(title);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
