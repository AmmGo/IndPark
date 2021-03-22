package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MyMsgIdActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tvConent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private MyMsgEvent.RecordsBean bean;


    @Override
    protected int getContentView() {
        return R.layout.activity_my_msg_id;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bean = (MyMsgEvent.RecordsBean) getIntent().getExtras().get("Extra_data");
        tvConent.setText(bean.name);
        tvTime.setText(bean.pushTime);
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("消息详情");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        upData();
    }

    public void upData() {
        Map<String, String> paramMap1 = new HashMap<>();
        paramMap1.put("id", String.valueOf(bean.id));
        paramMap1.put("read", String.valueOf(1));
        ArticlesRepo.getMsgUpdate(paramMap1).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("修改状态成功", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

}
