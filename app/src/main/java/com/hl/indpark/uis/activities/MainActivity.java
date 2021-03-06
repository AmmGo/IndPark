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
    private static final String TAG = "ๆ็้ฆ้กต";
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
     * ๆๅๆจ้ๅซๅไธบUID
     * ๆๅๆจ้TagไธบUIDๅไผไธID
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ไฟๆดป๏ผ็ๆงๅณๅฑๅน
//        registerReceiver();
        JPushUtils.requestPermission(this);
        JPushUtils.getRegistrationID();
        JPushUtils.bindAlias(Util.getUserId());
        JPushUtils.addTags(Util.string2Array(Util.getUserId() + "," + Util.getEnterpriseId()));
        Log.e("Uid", Util.getUserId());
        Log.e("ไผไธid", Util.getEnterpriseId());
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
            Log.e("็ๆฌๅท", "ๅฝๅ็ๆฌ" + versionNum);
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
            ToastUtil.showToast(this, "ๅๆไธๆฌก่ฟๅ้ฎ้ๅบ๏ฝ");
            handler.sendEmptyMessageDelayed(MSG_QUIT, TIME_QUIT);
            return;
        }
        super.onBackPressed();
    }

    private String getVersionName() throws Exception {
        // ่ทๅpackagemanager็ๅฎไพ
        PackageManager packageManager = getPackageManager();
        // getPackageName()ๆฏไฝ?ๅฝๅ็ฑป็ๅๅ๏ผ0ไปฃ่กจๆฏ่ทๅ็ๆฌไฟกๆฏ
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        version = packInfo.versionName;
        return version;
    }

    /**
     * status 1ๅผบๅถ 2ไธๅผบๅถ
     * needed 1ๆดๆฐ 2ไธๆดๆฐ
     */
    boolean isforce = false;

    public void getDataUpdate() {
        ArticlesRepo.getUpdateVersion(String.valueOf(version)).observe(this, new ApiObserver<UpdateVersion>() {
            @Override
            public void onSuccess(Response<UpdateVersion> response) {
                versionUpdate = response.getData();
                Log.e("ๅฝๅ็ๆฌ", "onSuccess: ");
                Log.e("ๅฝๅ็ๆฌ1ๆดๆฐ2ไธๆดๆฐ", "onSuccess: " + response.getData().status);
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
                .setTitle("ๅ็ฐๆฐ็ๆฌ")
                .setMessage(versionUpdate.content)
                .setPositiveButton("ๅ็บง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String webUrl = versionUpdate.fileUrl;
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(webUrl);//ๆญคๅคๅกซ้พๆฅ
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }).create().show();

    }

    private DownloadManager manager;

    private void startUpdate(boolean isforce, String url) {
        /*
         * ๆดไธชๅบๅ่ฎธ้็ฝฎ็ๅๅฎน
         * ้ๅฟ้
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //่พๅบ้่ฏฏๆฅๅฟ
                .setEnableLog(true)
                //่ฎพ็ฝฎ่ชๅฎไน็ไธ่ฝฝ
                //.setHttpManager()
                //ไธ่ฝฝๅฎๆ่ชๅจ่ทณๅจๅฎ่ฃ้กต้ข
                .setJumpInstallPage(true)
                //่ฎพ็ฝฎๅฏน่ฏๆก่ๆฏๅพ็ (ๅพ็่ง่ๅ็งdemoไธญ็็คบไพๅพ)
                .setDialogImage(R.drawable.ic_dialog_default)
                //่ฎพ็ฝฎๆ้ฎ็้ข่ฒ
                .setDialogButtonColor(Color.parseColor("#3688FF"))
                //่ฎพ็ฝฎๅฏน่ฏๆกๅผบๅถๆดๆฐๆถ่ฟๅบฆๆกๅๆๅญ็้ข่ฒ
                .setDialogProgressBarColor(Color.parseColor("#3688FF"))
                //่ฎพ็ฝฎๆ้ฎ็ๆๅญ้ข่ฒ
                .setDialogButtonTextColor(Color.WHITE)
                //่ฎพ็ฝฎๆฏๅฆๆพ็คบ้็ฅๆ?่ฟๅบฆ
                .setShowNotification(true)
                //่ฎพ็ฝฎๆฏๅฆๆ็คบๅๅฐไธ่ฝฝtoast
                .setShowBgdToast(false)
                //่ฎพ็ฝฎๆฏๅฆไธๆฅๆฐๆฎ
                .setUsePlatform(true)
                //่ฎพ็ฝฎๅผบๅถๆดๆฐ
                .setForcedUpgrade(isforce)
                //่ฎพ็ฝฎๅฏน่ฏๆกๆ้ฎ็็นๅป็ๅฌ
                .setButtonClickListener(this)
                //่ฎพ็ฝฎไธ่ฝฝ่ฟ็จ็็ๅฌ
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
         * ไธ่ฝฝไธญ
         *
         * @param max      ๆป่ฟๅบฆ
         * @param progress ๅฝๅ่ฟๅบฆ
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
