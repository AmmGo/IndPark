package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hl.indpark.R;
import com.hl.indpark.uis.adapters.ViewPagerAdapter;
import com.hl.indpark.uis.fragments.MyExchangeRecordFragment;
import com.hl.indpark.uis.fragments.MyScoresFragment;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MyScoresActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    @Override
    protected int getContentView() {
        return R.layout.activity_my_scores;
    }
    @OnClick({R.id.rl_join})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.rl_join:
                startActivityForResult(new Intent(this,CommodityActivity.class),1);
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            initView();
        }

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的积分");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyScoresActivity.this, HelpDetailsActivity.class);
                intent.putExtra("HELPID", 1);
                startActivity(intent);
            }
        });
        tabLayout = findViewById(R.id.layout_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager = findViewById(R.id.viewpager);
        initView();
    }
    private void initView() {
        final String[] titles = {"我的积分", "积分兑换"};
        Fragment fragment1 = new MyScoresFragment();
        Fragment fragment2 = new MyExchangeRecordFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
