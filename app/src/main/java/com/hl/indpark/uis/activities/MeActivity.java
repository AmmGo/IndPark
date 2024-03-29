package com.hl.indpark.uis.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.hl.indpark.App;
import com.hl.indpark.R;
import com.hl.indpark.utils.JPushUtils;
import com.hl.indpark.utils.SharePreferenceUtil;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;

import static com.hl.indpark.utils.JPushUtils.isPushStopped;


public class MeActivity extends BaseActivity {

    private Switch aSwitch;
    private LinearLayout llabout;
    private RelativeLayout relexit;

    @Override
    protected int getContentView() {
        return R.layout.activity_me;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("设置");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        aSwitch = findViewById(R.id.switch_1);
        if (isPushStopped()){
            aSwitch.setChecked(false);
        }else{
            aSwitch.setChecked(true);

        }
        llabout = findViewById(R.id.ll_about);
        relexit = findViewById(R.id.exit);
        relexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                clearDB();
                SharePreferenceUtil.clearAllValue(MeActivity.this);
                App.closeAllActivityByMap();
                App.getInstance().rtmClient().logout(new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                    }
                });
                Intent intent = new Intent(MeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        llabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this,AboutActivity.class));
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushUtils.resumePush();
                    ToastUtil.showToast(MeActivity.this, "开启推送");
                } else {
                    JPushUtils.stopPush();
                    ToastUtil.showToast(MeActivity.this, "关闭推送");
                }
            }
        });
    }
    //清除
    private void clearDB(){
        SharedPreferences sp=getSharedPreferences("Logindb",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.clear();
        editor.commit();
    }
}
