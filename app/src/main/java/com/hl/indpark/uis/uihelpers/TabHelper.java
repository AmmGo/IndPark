package com.hl.indpark.uis.uihelpers;


import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.uis.adapters.TabPageAdapter;


 /**
  * Created by yjl on 2021/3/30 15:39
  * Function：
  * Desc：
  */
public class TabHelper {
    private static final int MAX_CACHE_SIZE = 4;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPageAdapter adapter;

    public TabHelper(TabLayout tabLayout, ViewPager viewPager, TabPageAdapter adapter) {
        this.tabLayout = tabLayout;
        this.viewPager = viewPager;
        this.adapter = adapter;

        if (adapter.getCount() <= MAX_CACHE_SIZE) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(MAX_CACHE_SIZE);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setCurrentPosition(int position) {
        viewPager.setCurrentItem(position);
    }

    public void setCurrentPosition(int position, boolean smoothScroll) {
        viewPager.setCurrentItem(position, smoothScroll);
    }

    public TabPageAdapter getAdapter() {
        return adapter;
    }

}
