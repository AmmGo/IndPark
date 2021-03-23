package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import com.hl.indpark.R;
import com.hl.indpark.entities.LoginResultEntity;
import com.hl.indpark.entities.Response;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.UserRepo;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.ClearWriteEditText;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.DialogUtil;
import net.arvin.baselib.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private ClearWriteEditText edName;
    private ClearWriteEditText edPassword;
    private DialogUtil dialogUtil;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.btn_login).setOnClickListener(this);
        edName = findViewById(R.id.edit_name);
        edPassword = findViewById(R.id.edit_pwd);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.img_see_pwd).setOnClickListener(this);
        dialogUtil = new DialogUtil(this);
    }

    int pwd = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
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

                break;
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
