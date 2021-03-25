package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hl.indpark.App;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
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
    private String version;

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
                Log.e("main", "rtm client login success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e("main", "rtm client login failed:" + errorInfo.getErrorDescription());
            }
        });
        try {
            String versionNum = getVersionName();
            Log.e("版本号", "当前版本"+versionNum );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        version = packInfo.versionName;
        return version;
    }
    public void getData() {
        ArticlesRepo.getUpdateVersion(String.valueOf(version)).observe(this, new ApiObserver<UpdateVersion>() {
            @Override
            public void onSuccess(Response<UpdateVersion> response) {
                Log.e("当前版本", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), MainActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
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
