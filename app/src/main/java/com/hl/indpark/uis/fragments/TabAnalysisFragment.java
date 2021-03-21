package com.hl.indpark.uis.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.uis.adapters.ViewPagerAdapter;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
  * Created by yjl on 2021/3/10 9:55
  * Function：报警分析
  * Desc：
  */
public class TabAnalysisFragment extends BaseFragment {
     private TabLayout tabLayout;
     private ViewPager mViewPager;
    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_analysis;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tabLayout = root.findViewById(R.id.layout_tab);
        mViewPager = root.findViewById(R.id.viewpager);
        setLayoutWidth();
        initView();
    }
    public void setLayoutWidth(){
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                // 拿到tabLayout的slidingTabIndicator属性
                try {
                    Field slidingTabIndicatorField = tabLayout.getClass().getDeclaredField("slidingTabIndicator");
                    slidingTabIndicatorField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) slidingTabIndicatorField.get(tabLayout);
                    Log.i("tabLayout", mTabStrip.getChildCount() +"   --");
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
//                        拿到tabview的textview属性
                        Field textViewField = tabView.getClass().getDeclaredField("textView");
                        textViewField.setAccessible(true);
                        TextView mTextView = (TextView) textViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);

//                        int width = mTextView.getWidth();
//                        if (width == 0) {
//                            mTextView.measure(0, 0);
//                            width = mTextView.getMeasuredWidth();
//                        }
//                        控制Item条目的宽度，这种设置的话就是等分
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        layoutParams.width = 250;
//                        layoutParams.weight = 1;
                        tabView.setLayoutParams(layoutParams);
                        tabView.invalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initView() {
        final String[] titles = {"环保", "危险源"};
        Fragment fragment1 = new TabAEPFragment();
        Fragment fragment2 = new TabAHSFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
//        onTab(tabLayout.getTabAt(0));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                onTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void onTab(TabLayout.Tab tab) {
        TextView textView = new TextView(getActivity());
        float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 18, getResources().getDisplayMetrics());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,selectedSize);
        textView.setTextColor(getResources().getColor(R.color.primary));
        textView.setText(tab.getText());
        tab.setCustomView(textView);
    }
}
