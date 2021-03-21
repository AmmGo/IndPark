package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hl.indpark.App;
import com.hl.indpark.R;
import com.hl.indpark.uis.activities.videoactivities.BaseCallActivity;
import com.hl.indpark.uis.fragments.MainFragment;
import com.hl.indpark.utils.JPushUtils;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.utils.WeakHandler;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;

import static com.hl.indpark.entities.events.EventType.TYPE_BIND_ALIAS;

public class MainActivity extends BaseCallActivity implements WeakHandler.IHandle {
    private MainFragment mainFragment;
    private static final int MSG_QUIT = 0;
    private static final int TIME_QUIT = 2000;
    private WeakHandler handler;
    private boolean canQuit = false;
    protected int statusBarHeight;

    /**
     * 极光推送别名为UID
     * 极光推送Tag为UID和企业ID
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushUtils.requestPermission(this);
        JPushUtils.getRegistrationID();
        JPushUtils.bindAlias(Util.getUserId());
        JPushUtils.addTags(Util.string2Array(Util.getUserId() + "," + Util.getEnterpriseId()));
        Log.e("Uid", Util.getUserId());
        Log.e("企业id", Util.getEnterpriseId());
        handler = new WeakHandler(this);
//        new Thread() {
//            @Override
//            public void run() {
//                handler.sendMessage(handler.obtainMessage(TYPE_BIND_ALIAS, Util.getUserId()));
//            }
//        }.start();

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
        switch (msg.what) {
            case 0:
                canQuit = false;
                break;
            case TYPE_BIND_ALIAS:
                JPushUtils.bindAlias(Util.getUserId());
                break;

        }

    }

}
