package org.androidtown.foodtruckgram.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.androidtown.foodtruckgram.Fragment.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 이예지 on 2018-09-06.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private MapFragment mapFragment = new MapFragment();
    private TruckListFragment truckListFragment = new TruckListFragment();
    private MyOrderFragment myOrderFragment = new MyOrderFragment();

    public List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    public void switchFragment(int position, Fragment fragment){
        mFragmentList.remove(position);
        mFragmentList.add(position, fragment);
    }
}

