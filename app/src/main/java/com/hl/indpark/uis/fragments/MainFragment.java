package com.hl.indpark.uis.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListenerAdapter;
import com.azhon.appupdate.manager.DownloadManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UpdateVersion;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.MainActivity;
import com.hl.indpark.uis.activities.MyMsgActivity;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yjl on 2021/3/8 10:51
 * Function：
 * Desc：主体
 */
public class MainFragment extends BaseFragment implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, OnButtonClickListener {
    private IDrawerToggle drawerToggle;

    private TextView titleBar;
    private TextView tv_hot_god_msg;
    private RelativeLayout rl_msg;
    private BottomNavigationView bottomNavigationView;
    //    R.id.tab_monitor
    private List<Integer> tabIds = Arrays.asList(R.id.tab_home, R.id.tab_maillist, R.id.tab_self);
    private SparseArray<BaseFragment> fragments = new SparseArray<>();
    private SparseArray<Class<? extends BaseFragment>> fragmentClasses = new SparseArray<>();
    private SparseIntArray fragmentTitles = new SparseIntArray();
    private String enterpriseName;
    private String version;
    private UpdateVersion versionUpdate;
    {
        fragmentClasses.put(R.id.tab_home, HomeFragment.class);
//        fragmentClasses.put(R.id.tab_monitor, MonitorFragment.class);
        fragmentClasses.put(R.id.tab_maillist, MailListFragment.class);
        fragmentClasses.put(R.id.tab_self, SelfFragment.class);

        fragmentTitles.put(R.id.tab_home, R.string.tab_home);
//        fragmentTitles.put(R.id.tab_monitor, R.string.tab_monitor);
        fragmentTitles.put(R.id.tab_maillist, R.string.tab_maillist);
        fragmentTitles.put(R.id.tab_self, R.string.tab_self1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDrawerToggle) {
            drawerToggle = (IDrawerToggle) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerToggle = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        enterpriseName = SharePreferenceUtil.getKeyValue("enterpriseName");
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleBar = root.findViewById(R.id.title);
        rl_msg = root.findViewById(R.id.rl_msg);
        tv_hot_god_msg = root.findViewById(R.id.tv_hot_god_msg);
        rl_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
            }
        });
        enterpriseName = SharePreferenceUtil.getKeyValue("enterpriseName");
        if (enterpriseName != null && !enterpriseName.equals("")) {
            titleBar.setText(enterpriseName);
        }
        bottomNavigationView = root.findViewById(R.id.tab_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(tabIds.get(0));
        try {
            String versionNum = getVersionName();
            Log.e("版本号", "当前版本" + versionNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getData();
        getDataVersion();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction beginTransaction = getChildFragmentManager().beginTransaction();
        hideAll(beginTransaction);
        getData();
        getDataVersion();
        int itemId = item.getItemId();
        if (tabIds.contains(itemId)) {
            if (fragmentTitles.get(itemId) == R.string.tab_home) {
                Log.e("输出title", "onNavigationItemSelected: 一样");
                titleBar.setText(enterpriseName);
            } else {
                titleBar.setText(fragmentTitles.get(itemId));
            }

            if (titleBar.getText().equals("")) {
                titleBar.setVisibility(View.INVISIBLE);
                rl_msg.setVisibility(View.INVISIBLE);
            } else {
                titleBar.setVisibility(View.VISIBLE);
                rl_msg.setVisibility(View.VISIBLE);
            }
            BaseFragment fragment = fragments.get(itemId);
            if (fragment == null) {
                try {
                    fragment = fragmentClasses.get(itemId).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fragment != null) {
                    beginTransaction.add(R.id.layout_content, fragment);
                    fragments.put(itemId, fragment);
                }
            } else {
                beginTransaction.show(fragment);
            }
            beginTransaction.commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    private void hideAll(FragmentTransaction beginTransaction) {
        for (Integer tabId : tabIds) {
            BaseFragment fragment = fragments.get(tabId);
            if (fragment != null) {
                beginTransaction.hide(fragment);
            }
        }
    }

    @Override
    public void onButtonClick(int id) {

    }

    public interface IDrawerToggle {
        void toggle();
    }

    public void getData() {
        ArticlesRepo.getMsgRead().observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (response.getData() != null && !response.getData().equals("")) {
                       String sumData =  response.getData();
                        if (sumData.equals("0")) {
                            tv_hot_god_msg.setVisibility(View.GONE);
                        } else {
                            int sum = Integer.parseInt(sumData);
                            if (sum>999){
                                tv_hot_god_msg.setText("999+");
                            }else{
                                tv_hot_god_msg.setText(sumData);
                            }
                            tv_hot_god_msg.setVisibility(View.VISIBLE);
                        }
                        SharePreferenceUtil.saveKeyValue("msgNum", String.valueOf(sumData));
                        Log.e("未读消息", "onSuccess: " + sumData);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                tv_hot_god_msg.setVisibility(View.GONE);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                tv_hot_god_msg.setVisibility(View.GONE);
                super.onError(throwable);

            }
        });
    }
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        version = packInfo.versionName;
        return version;
    }

    /**
     * status 1更新 2不更新
     * needed 1强制 2不强制
     */
    boolean isforce = false;
    public void getDataVersion() {

        ArticlesRepo.getUpdateVersion(String.valueOf(version)).observe(this, new ApiObserver<UpdateVersion>() {
            @Override
            public void onSuccess(Response<UpdateVersion> response) {
                try {
                    versionUpdate = response.getData();
//                versionUpdate.status =1;
//                versionUpdate.needed =2;
                    Log.e("当前版本1更新2不更新", "onSuccess: " + response.getData().status);
                    if (versionUpdate != null) {
                        if (versionUpdate.status == 1) {
                            if (versionUpdate.needed == 1) {
                                if (versionUpdate.fileUrl.contains(".apk")){
                                    isforce = true;
                                    startUpdate(isforce,versionUpdate.fileUrl);
                                }else {
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
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    public void upWeb() {
        new AlertDialog.Builder(getActivity())
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
    private void startUpdate(boolean isforce,String url) {
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
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor("#ff895b"))
                //设置对话框强制更新时进度条和文字的颜色
                .setDialogProgressBarColor(Color.parseColor("#ff895b"))
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

        manager = DownloadManager.getInstance(getActivity());
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

}
