package com.hl.indpark.uis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class ProtectActivity extends AppCompatActivity {


    public static WeakReference<ProtectActivity> weakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }
    protected void initView() {
        weakReference = new WeakReference<>(this);
        Log.e("save", "初始化");
        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        //起始坐标
        params.x = 0;
        params.y = 0;
        //宽高设计为1个像素
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    protected void initData() {
    }

    protected void initEvent() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.onTouchEvent(motionEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isScreenOn()) {
            finishSelf();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("save", "销毁保活页面");
        if (weakReference != null && weakReference.get() == this) {
            weakReference = null;
        }
    }

    /**
     * 关闭自己
     */
    public void finishSelf() {
        if (!isFinishing()) {
            finish();
        }
    }

    /**
     * 判断主屏幕是否点亮
     *
     * @return
     */
    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getApplicationContext()
                .getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }
}
