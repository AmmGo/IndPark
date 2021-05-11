package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.SelfCheck;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.fragments.ImageCheckFragment;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelfCheckIdActivity extends BaseActivity {
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ed_event_titile)
    TextView address;
    @BindView(R.id.ed_event_decs)
    TextView tvDes;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @Override
    protected int getContentView() {
        return R.layout.activity_self_check_id;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int idEvent = intent.getIntExtra("id", 0);
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("自检详情");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getData(idEvent);
        setMediaFragment();
    }
    public void getData(int id) {
        ArticlesRepo.getCheckIDEvent(String.valueOf(id)).observe(this, new ApiObserver<SelfCheck.RecordsBean>() {
            @Override
            public void onSuccess(Response<SelfCheck.RecordsBean> response) {
                Log.e("-ID-查询事件", "onSuccess: ");
                try {
                    setViewData(response.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), SelfCheckIdActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    /**
     * 设置fragment
     */
    private ImageCheckFragment mediaFragment;
    private List<String> mediaList = new ArrayList<>();

    public void setViewData(SelfCheck.RecordsBean selfCheck){
        tvType.setText("自检类型:" + getType(selfCheck.checkType));
        address.setText("自检地址:" + selfCheck.address);
        tvDes.setText("自检详情:" + selfCheck.details);
        if (selfCheck.imageList != null && selfCheck.imageList.size() > 0) {
            mediaList = selfCheck.imageList;
            mediaFragment.setActivity(SelfCheckIdActivity.this, mediaList);
            llImg.setVisibility(View.VISIBLE);
        } else {
            llImg.setVisibility(View.GONE);
        }
    }
    private void setMediaFragment() {
        mediaFragment = new ImageCheckFragment();
        mediaList = new ArrayList<>();
        mediaFragment.setActivity(SelfCheckIdActivity.this, mediaList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }
    public String getType(int type) {
        String type1 = "";
        switch (type) {
            case 1:
                type1 = "安全自检";
                break;
            case 2:
                type1 = "消防自检";
                break;
            case 3:
                type1 = "设备自检";
                break;
            case 4:
                type1 = "其他自检";
                break;
            default:
                type1 = "其他自检";
        }
        return type1;
    }
}