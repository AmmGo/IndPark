package com.hl.indpark.uis.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListenerAdapter;
import com.azhon.appupdate.manager.DownloadManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

public class AboutActivity extends BaseActivity implements OnButtonClickListener {


    private RelativeLayout rlVersion;
    private TextView jchat_version;
    private String versionNum;
    private UpdateVersion versionUpdate;
    private ShimmerFrameLayout layoutShimmer;
    @Override
    protected int getContentView() {
        return R.layout.activity_about;
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
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        rlVersion = findViewById(R.id.rl_version);
        jchat_version = findViewById(R.id.jchat_version);
        layoutShimmer = findViewById(R.id.layout_shimmer);
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            jchat_version.setText("V"+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        rlVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        titleBar.getCenterTextView().setText("????????????");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            versionNum = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getVersionName() throws Exception {
        // ??????packagemanager?????????
        PackageManager packageManager = getPackageManager();
        // getPackageName()???????????????????????????0???????????????????????????
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
     String   version = packInfo.versionName;
        return version;
    }
    /**
     * status 1?????? 2?????????
     * needed 1?????? 2?????????
     */
    boolean isforce = false;
    public void getData() {
        ArticlesRepo.getUpdateVersion(String.valueOf(versionNum)).observe(this, new ApiObserver<UpdateVersion>() {
            @Override
            public void onSuccess(Response<UpdateVersion> response) {
                versionUpdate = response.getData();
                Log.e("????????????", "onSuccess: ");
                Log.e("????????????1??????2?????????", "onSuccess: " + response.getData().status);
                try {
                    if (versionUpdate != null) {
                        if (versionUpdate.needed == 1) {
                            if (versionUpdate.status == 1) {
                                isforce = true;
                            }else{
                                isforce = false;

                            }
                            if (versionUpdate.fileUrl.contains(".apk")){
                                startUpdate(isforce, versionUpdate.fileUrl);
                            }else{
                                upWeb();
                            }
                        }else{
                            ToastUtil.showToast(AboutActivity.this,"????????????????????????");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), AboutActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    public void upWeb() {
        new AlertDialog.Builder(this)
                .setTitle("???????????????")
                .setMessage(versionUpdate.content)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String webUrl = versionUpdate.fileUrl;
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(webUrl);//???????????????
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }).create().show();

    }
    private DownloadManager manager;
    private void startUpdate(boolean isforce,String url) {
        /*
         * ??????????????????????????????
         * ?????????
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //??????????????????
                .setEnableLog(true)
                //????????????????????????
                //.setHttpManager()
                //????????????????????????????????????
                .setJumpInstallPage(true)
                //??????????????????????????? (??????????????????demo???????????????)
                .setDialogImage(R.drawable.ic_dialog_default)
                //?????????????????????
                .setDialogButtonColor(Color.parseColor("#3688FF"))
                //?????????????????????????????????????????????????????????
                .setDialogProgressBarColor(Color.parseColor("#3688FF"))
                //???????????????????????????
                .setDialogButtonTextColor(Color.WHITE)
                //?????????????????????????????????
                .setShowNotification(true)
                //??????????????????????????????toast
                .setShowBgdToast(false)
                //????????????????????????
                .setUsePlatform(true)
                //??????????????????
                .setForcedUpgrade(isforce)
                //????????????????????????????????????
                .setButtonClickListener(this)
                //???????????????????????????
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
         * ?????????
         *
         * @param max      ?????????
         * @param progress ????????????
         */
        @Override
        public void downloading(int max, int progress) {
//            int curr = (int) (progress / (double) max * 100.0);
        }
    };

    @Override
    public void onButtonClick(int id) {

    }
}
