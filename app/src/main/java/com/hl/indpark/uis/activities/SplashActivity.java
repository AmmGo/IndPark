package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hl.indpark.R;
import com.hl.indpark.uis.activities.videoactivities.utils.WindowUtil;
import com.hl.indpark.utils.SharePreferenceUtil;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.WeakHandler;

public class SplashActivity extends BaseActivity implements WeakHandler.IHandle {
    private ShimmerFrameLayout layoutShimmer;
    protected int statusBarHeight;
    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        WeakHandler handler = new WeakHandler(this);
        handler.sendEmptyMessageDelayed(0, 2000);
        layoutShimmer = findViewById(R.id.layout_shimmer);
        initStatusBarHeight();
        WindowUtil.hideWindowStatusBar(getWindow());

    }

    private void initStatusBarHeight() {
        statusBarHeight = WindowUtil.getSystemStatusBarHeight(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        layoutShimmer.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        layoutShimmer.stopShimmer();
    }

    @Override
    public void handleMessage(Message msg) {
        String token= SharePreferenceUtil.getKeyValue("token");

        if (token!=null&&!token.equals("")){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
