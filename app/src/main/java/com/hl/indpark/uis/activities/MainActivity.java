package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hl.indpark.App;
import com.hl.indpark.R;
import com.hl.indpark.uis.fragments.MainFragment;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.utils.WeakHandler;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;

public class MainActivity extends BaseActivity implements WeakHandler.IHandle {
    private MainFragment mainFragment;
    private static final int MSG_QUIT = 0;
    private static final int TIME_QUIT = 2000;
    private WeakHandler handler;
    private boolean canQuit = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        handler = new WeakHandler(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.layout_main, mainFragment);
        fragmentTransaction.commit();
        App.getInstance().rtmClient().login(null, Util.getUserId(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("main", "rtm client login success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i("main", "rtm client login failed:" + errorInfo.getErrorDescription());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!canQuit) {
            canQuit = true;
            ToastUtil.showToast(this, "再按一次返回键退出～");
            handler.sendEmptyMessageDelayed(MSG_QUIT, TIME_QUIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 0) {
            canQuit = false;
        }
    }

}
