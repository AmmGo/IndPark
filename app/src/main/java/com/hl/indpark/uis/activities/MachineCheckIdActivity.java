package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MachineCheckId;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.fragments.ImageMachineCheckFragment;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MachineCheckIdActivity extends BaseActivity {
    @BindView(R.id.tv_sbmc)
    TextView tv_sbmc;
    @BindView(R.id.tv_sbdz)
    TextView tv_sbdz;

    @BindView(R.id.tv_xjry)
    TextView tv_xjry;
    @BindView(R.id.tv_xjry_lxdh)
    TextView tv_xjry_lxdh;

    @BindView(R.id.tv_xjrq)
    TextView tv_xjrq;

    @BindView(R.id.tv_xjqy)
    TextView tv_xjqy;
    @BindView(R.id.tv_qylxr)
    TextView tv_qylxr;
    @BindView(R.id.tv_qylxr_lxdh)
    TextView tv_qylxr_lxdh;
    @BindView(R.id.tv_yqjzsfzc)
    TextView tv_yqjzsfzc;
    @BindView(R.id.tv_ycms)
    TextView tv_ycms;

    @Override
    protected int getContentView() {
        return R.layout.activity_machine_check_id;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String idEvent = intent.getStringExtra("id");
        if (idEvent == null) {
            idEvent = "21";
        }
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("巡检详情");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getData(idEvent);
        setMediaFragment();

    }


    public void showData(MachineCheckId item) {
        String str1 = item.equipmentName == null ? "" : item.equipmentName;
        String str2 = item.equipmentAddress == null ? "" : item.equipmentAddress;
        String str3 = item.examinePerson == null ? "" : item.examinePerson;
        String str4 = item.phone == null ? "" : item.phone;
        String str5 = item.examineTime == null ? "" : item.examineTime;
        String str6 = item.enterpriseName == null ? "" : item.enterpriseName;
        String str7 = item.enterpriseContactPerson == null ? "" : item.enterpriseContactPerson;
        String str8 = item.enterpriseContactPhone == null ? "" : item.enterpriseContactPhone;
        String str9 = item.isException.equals("1")? "是" : "否";
        String str10 = item.description == null ? "" : item.description;
        tv_sbmc.setText("设备名称："+str1);
        tv_sbdz.setText("设备地址："+str2);
        tv_xjry.setText("巡检人员："+str3);
        tv_xjry_lxdh.setText("联系电话："+str4);
        tv_xjrq.setText("巡检日期："+str5);
        tv_xjqy.setText("巡检企业："+str6);
        tv_qylxr.setText("企业联系人："+str7);
        tv_qylxr_lxdh.setText("联系电话："+str8);
        tv_yqjzsfzc.setText("仪器校准是否正常："+str9);
        tv_ycms.setText("异常描述："+str10);
        try {
            String[] image = new String[]{};
            image = item.image.split(",");
            if (image != null && image.length > 0) {
                mediaList = Arrays.asList(image);
                mediaFragment.setActivity(this, mediaList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(String id) {

        ArticlesRepo.getMachineCheckReportIdEvent(id).observe(this, new ApiObserver<List<MachineCheckId>>() {
            @Override
            public void onSuccess(Response<List<MachineCheckId>> response) {
                if (response.getData() != null && response.getData().size() > 0) {
                    MachineCheckId machineCheckId = new MachineCheckId();
                    machineCheckId = response.getData().get(0);
                    showData(machineCheckId);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(MachineCheckIdActivity.this, msg);
                Util.login(String.valueOf(code), MachineCheckIdActivity.this);
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
    private ImageMachineCheckFragment mediaFragment;
    private List<String> mediaList = new ArrayList<>();

    private void setMediaFragment() {
        mediaFragment = new ImageMachineCheckFragment();
        mediaList = new ArrayList<>();
        mediaFragment.setActivity(this, mediaList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }
}