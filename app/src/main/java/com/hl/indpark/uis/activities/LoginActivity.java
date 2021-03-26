package com.hl.indpark.uis.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.hl.indpark.R;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.UserRepo;
import com.hl.indpark.permission.DefaultResourceProvider;
import com.hl.indpark.uis.activities.videoactivities.utils.WindowUtil;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.ClearWriteEditText;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.DialogUtil;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.permissionhelper.PermissionUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private ClearWriteEditText edName;
    private ClearWriteEditText edPassword;
    private DialogUtil dialogUtil;
    private CheckBox saveChb;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }
    protected int statusBarHeight;
    @Override
    protected void init(Bundle savedInstanceState) {
        initStatusBarHeight();
        WindowUtil.hideWindowStatusBar(getWindow());
        findViewById(R.id.btn_login).setOnClickListener(this);
        edName = findViewById(R.id.edit_name);
        edPassword = findViewById(R.id.edit_pwd);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.img_see_pwd).setOnClickListener(this);
        dialogUtil = new DialogUtil(this);
        saveChb = findViewById(R.id.saveChb);
        SharedPreferences sp2=getSharedPreferences("Logindb",MODE_PRIVATE);
        if(sp2.getBoolean("save",false)==true  ){    //判断是否写入了数值save==true
            getDB();
            saveChb.setChecked(true);
        }else{
            saveChb.setChecked(false);
        }
        initPermissionConfig();
    }
    private void initStatusBarHeight() {
        statusBarHeight = WindowUtil.getSystemStatusBarHeight(this);
    }
    //清除
    private void clearDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }
    //保存数据
    private void saveDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("loginEdt",edName.getText().toString());
        editor.putString("passwordEdt",edPassword.getText().toString());
        editor.putBoolean("save",true);
        editor.commit();            //写入数据
    }
    //读取数据
    private void getDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        String name= sp.getString("loginEdt","");
        String password=sp.getString("passwordEdt","");
        edName.setText(name);
        edPassword.setText(password);
    }
    private PermissionUtil permissionUtil;

    private void initPermissionConfig() {
        PermissionUtil.setPermissionTextProvider(new DefaultResourceProvider());
        permissionUtil = new PermissionUtil.Builder()
                .with(this)//必传：可使用FragmentActivity或v4.Fragment实例
                .setTitleText("提示")//弹框标题
                .setEnsureBtnText("确定")//权限说明弹框授权按钮文字
                .setCancelBtnText("取消")//权限说明弹框取消授权按钮文字
                .setSettingEnsureText("设置")//打开设置说明弹框打开按钮文字
                .setSettingCancelText("取消")//打开设置说明弹框关闭按钮文字
                .setSettingMsg("当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。")//打开设置说明弹框内容文字
                .setInstallAppMsg("允许安装来自此来源的应用")//打开允许安装此来源的应用设置
                .setShowRequest(true)//是否显示申请权限弹框
                .setShowSetting(true)//是否显示设置弹框
                .setShowInstall(true)//是否显示允许安装此来源弹框
                .setRequestCancelable(true)//申请权限说明弹款是否cancelable
                .setSettingCancelable(true)//打开设置界面弹款是否cancelable
                .setInstallCancelable(true)//打开允许安装此来源引用弹款是否cancelable
                .setTitleColor(Color.BLACK)//弹框标题文本颜色
                .setMsgColor(Color.GRAY)//弹框内容文本颜色
                .setEnsureBtnColor(Color.BLACK)//弹框确定文本颜色
                .setCancelBtnColor(Color.BLACK)//弹框取消文本颜色
                .build();
        permissionUtil.request("需要权限", PermissionUtil.asArray(      Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.ACCESS_COARSE_LOCATION), new PermissionUtil.RequestPermissionListener() {
            @Override
            public void callback(boolean granted, boolean isAlwaysDenied) {
                if (granted) {
                }
            }
        });
    }
    int pwd = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(saveChb.isChecked()){                    //当多选按钮按下时执行报损数据
                    saveDB();
                }
                else {
                    clearDB();
                }
                Util.hideInputManager(LoginActivity.this, v);
                login();
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.img_see_pwd:
                if (pwd == 0) {
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwd++;
                } else {
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwd--;
                }
        }
    }

    private void login() {
        String name = null;
        String password = null;
        Editable edNameText = edName.getText();
        Editable edPasswordText = edPassword.getText();
        if (edNameText != null) {
            name = edNameText.toString().trim();
        }
        if (edPasswordText != null) {
            password = edPasswordText.toString().trim();
        }
        if (TextUtils.isEmpty(name)) {
            edName.setError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("请输入密码");
            return;
        }
//         name = "admin";
//         password ="hldk1119";
//         name = "yidong";
//         password ="123456";
        final String psw = Util.getMd5(password);
        dialogUtil.showProgressDialog("登录中...");
        Map<String, String> map = new HashMap<>();
        map.put("account", name);
        map.put("password", psw);
        UserRepo.login(map).observe(this, new ApiObserver<LoginResultEntity>() {
            @Override
            public void onSuccess(Response<LoginResultEntity> response) {

                LoginResultEntity data = response.getData();
                SharePreferenceUtil.saveKeyValue("token", data.token);
                SharePreferenceUtil.saveKeyValue("roleId", String.valueOf(data.roleId));
                SharePreferenceUtil.saveKeyValue("enterpriseName", String.valueOf(data.enterpriseName));
                SharePreferenceUtil.saveKeyValue("userId", String.valueOf(data.id));
                SharePreferenceUtil.saveKeyValue("enterpriseId", String.valueOf(data.enterpriseId));
                Log.e("登录用户Id", "onSuccess: "+data.id );
                Log.e("TOKEN", data.token);
                dialogUtil.hideProgressDialog();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                onBackPressed();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                dialogUtil.hideProgressDialog();
                ToastUtil.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dialogUtil.hideProgressDialog();
//                code":10015登录过期，请重新登录
            }
        });
    }
}
