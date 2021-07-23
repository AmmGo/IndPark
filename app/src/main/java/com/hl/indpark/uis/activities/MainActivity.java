package com.hl.indpark.uis.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListenerAdapter;
import com.azhon.appupdate.manager.DownloadManager;
import com.hl.indpark.App;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.videoactivities.BaseCallActivity;
import com.hl.indpark.uis.fragments.MainFragment;
import com.hl.indpark.utils.AliveJobService;
import com.hl.indpark.utils.JPushUtils;
import com.hl.indpark.utils.ScreenReceiver;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.utils.WeakHandler;

import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.impl.BgStart;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;

import static com.hl.indpark.entities.events.EventType.TYPE_BIND_ALIAS;

public class MainActivity extends BaseCallActivity implements WeakHandler.IHandle, OnButtonClickListener {
    private static final String TAG = "我的首页";
    private MainFragment mainFragment;
    private static final int MSG_QUIT = 0;
    private static final int TIME_QUIT = 2000;
    private WeakHandler handler;
    private boolean canQuit = false;
    protected int statusBarHeight;
    private String version;
    private UpdateVersion versionUpdate;
    private String versionNum;
    private ScreenReceiver screenReceiver;

    /**
     * 极光推送别名为UID
     * 极光推送Tag为UID和企业ID
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 保活，监控关屏幕
//        registerReceiver();
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
            versionNum = getVersionName();
            Log.e("版本号", "当前版本" + versionNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDataUpdate();
        AliveJobService.startJobScheduler(this);
        BgStart.getInstance().requestStartPermisstion(this, new PermissionLisenter() {
            @Override
            public void onGranted() {
                Log.e(TAG, "onGranted");
            }

            @Override
            public void cancel() {
                Log.e(TAG, "cancel");
            }

            @Override
            public void onDenied() {
                Log.e(TAG, "onDenied");
            }
        },"huawei", "oppo", "vivo","meizu");
    }
    private void registerReceiver() {
        screenReceiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, intentFilter);
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

    /**
     * status 1强制 2不强制
     * needed 1更新 2不更新
     */
    boolean isforce = false;

    public void getDataUpdate() {
        ArticlesRepo.getUpdateVersion(String.valueOf(version)).observe(this, new ApiObserver<UpdateVersion>() {
            @Override
            public void onSuccess(Response<UpdateVersion> response) {
                versionUpdate = response.getData();
                Log.e("当前版本", "onSuccess: ");
                Log.e("当前版本1更新2不更新", "onSuccess: " + response.getData().status);
                try {
//                    versionUpdate.status = 1;
//                    versionUpdate.needed = 2;
//                    versionUpdate.fileUrl ="https://www.pgyer.com/ZrcR";
                    if (versionUpdate != null) {
                        if (versionUpdate.needed == 1) {
                            if (versionUpdate.status != 1) {
                                if (versionUpdate.fileUrl.contains(".apk")) {
                                    isforce = false;
                                    startUpdate(isforce, versionUpdate.fileUrl);
                                } else {
                                    upWeb();
                                }
                            }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void upWeb() {
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage(versionUpdate.content)
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String webUrl = versionUpdate.fileUrl;
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(webUrl);//此处填链接
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }).create().show();

    }

    private DownloadManager manager;

    private void startUpdate(boolean isforce, String url) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                .setDialogImage(R.drawable.ic_dialog_default)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor("#3688FF"))
                //设置对话框强制更新时进度条和文字的颜色
                .setDialogProgressBarColor(Color.parseColor("#3688FF"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置是否上报数据
                .setUsePlatform(true)
                //设置强制更新
                .setForcedUpgrade(isforce)
                //设置对话框按钮的点击监听
                .setButtonClickListener(this)
                //设置下载过程的监听
                .setOnDownloadListener(listenerAdapter);

        manager = DownloadManager.getInstance(this);
        manager.setApkName("gongyexing.apk")
                .setApkUrl(url)
                .setSmallIcon(R.mipmap.logo)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(2)
                .setApkVersionName(versionUpdate.version)
                .setApkDescription(versionUpdate.content)
//                .setApkMD5("DC501F04BBAA458C9DC33008EFED5E7F")
                .download();
    }

    private OnDownloadListenerAdapter listenerAdapter = new OnDownloadListenerAdapter() {
        /**
         * 下载中
         *
         * @param max      总进度
         * @param progress 当前进度
         */
        @Override
        public void downloading(int max, int progress) {
//            int curr = (int) (progress / (double) max * 100.0);
        }
    };

    @Override
    public void onButtonClick(int id) {

    }

    @Override
    public void onDestroy() {
       super.onDestroy();
//        unregisterReceiver(screenReceiver);
    }
}
