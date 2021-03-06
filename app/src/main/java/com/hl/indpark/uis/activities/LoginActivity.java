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
        if(sp2.getBoolean("save",false)==true  ){    //???????????????????????????save==true
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
    //??????
    private void clearDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }
    //????????????
    private void saveDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("loginEdt",edName.getText().toString());
        editor.putString("passwordEdt",edPassword.getText().toString());
        editor.putBoolean("save",true);
        editor.commit();            //????????????
    }
    //????????????
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
                .with(this)//??????????????????FragmentActivity???v4.Fragment??????
                .setTitleText("??????")//????????????
                .setEnsureBtnText("??????")//????????????????????????????????????
                .setCancelBtnText("??????")//??????????????????????????????????????????
                .setSettingEnsureText("??????")//??????????????????????????????????????????
                .setSettingCancelText("??????")//??????????????????????????????????????????
                .setSettingMsg("?????????????????????????????????\n?????????\"??????\"-\"??????\"-?????????????????????")//????????????????????????????????????
                .setInstallAppMsg("????????????????????????????????????")//??????????????????????????????????????????
                .setShowRequest(true)//??????????????????????????????
                .setShowSetting(true)//????????????????????????
                .setShowInstall(true)//???????????????????????????????????????
                .setRequestCancelable(true)//??????????????????????????????cancelable
                .setSettingCancelable(true)//??????????????????????????????cancelable
                .setInstallCancelable(true)//?????????????????????????????????????????????cancelable
                .setTitleColor(Color.BLACK)//????????????????????????
                .setMsgColor(Color.GRAY)//????????????????????????
                .setEnsureBtnColor(Color.BLACK)//????????????????????????
                .setCancelBtnColor(Color.BLACK)//????????????????????????
                .build();
        permissionUtil.request("????????????", PermissionUtil.asArray(      Manifest.permission.RECORD_AUDIO,
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
                if(saveChb.isChecked()){                    //??????????????????????????????????????????
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
            edName.setError("??????????????????");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("???????????????");
            return;
        }
//         name = "admin";
//         password ="hldk1119";
//         name = "yidong";
//         password ="123456";
        final String psw = Util.getMd5(password);
        dialogUtil.showProgressDialog("?????????...");
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
                SharePreferenceUtil.saveKeyValue("category", String.valueOf(data.category));
                Log.e("????????????Id", "onSuccess: "+data.id );
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
//                code":10015??????????????????????????????
            }
        });
    }
}
