package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyPeportIDEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.fragments.ImageFragment;
import com.hl.indpark.uis.fragments.MediaFragment;
import com.hl.indpark.widgit.EntDialog;
import com.luck.picture.lib.entity.LocalMedia;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @description 上报审批
 * @date: 2021/3/14 23:18
 * @author: yjl
 */
public class ReportApprovalActivity extends BaseActivity {

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ed_event_titile)
    TextView tvTitle;
    @BindView(R.id.ed_event_decs)
    TextView tvDes;
    @BindView(R.id.tv_count_onte)
    TextView tvCount;
    @BindView(R.id.ed_event_onte)
    EditText editOnte;
    boolean islMaxCount;
    private int idEvent;

    @OnClick({ R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_report:
                if (editOnte.getText()!=null&&!editOnte.getText().equals("")){
                    updataData();
                }else {
                    ToastUtil.showToast(this,"请填写处理意见");
                }
                break;
            default:
        }
    }
    @OnTextChanged(value = R.id.ed_event_onte, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        tvCount.setText(detailLength + "/300");
        if (detailLength == 299) {
            islMaxCount = true;
        }
        if (detailLength == 300 && islMaxCount) {
            ToastUtil.showToast(ReportApprovalActivity.this, "最多输入300个字");
            islMaxCount = false;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report_approval;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的上报");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        idEvent = intent.getIntExtra("id", 0);
        //根据id查询事件
        getData(idEvent);
        setMediaFragment();
    }

    public void getData(int id) {
        ArticlesRepo.getMyPeportIDEvent(String.valueOf(id)).observe(this, new ApiObserver<MyPeportIDEvent>() {
            @Override
            public void onSuccess(Response<MyPeportIDEvent> response) {
                Log.e("审批列表-ID-查询事件", "onSuccess: ");
                setViewData(response.getData());
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

    public void setViewData(MyPeportIDEvent idEvent) {
        tvType.setText(idEvent.typeName);
        tvTitle.setText(idEvent.reportedName);
        tvDes.setText(idEvent.content);
        if (idEvent.imageList!=null&&idEvent.imageList.size()>0){
            mediaFragment.setActivity(this, mediaList);
        }

    }

    /**
     * 设置fragment
     */
    private ImageFragment mediaFragment;
    private List<String> mediaList = new ArrayList<>();
    private void setMediaFragment() {
        mediaFragment = new ImageFragment();
        mediaList = new ArrayList<>();
        mediaFragment.setActivity(this, mediaList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
//        for (int i =0;i<5;i++){
//            mediaList.add("https://bkimg.cdn.bcebos.com/pic/bd3eb13533fa828b56571042f51f4134970a5a1f?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U4MA==,g_7,xp_5,yp_5/format,f_auto");
//        }
//
//        mediaFragment.setActivity(this, mediaList);
    }

    public void updataData() {
        Map<String, String> paramMap = new HashMap<>();
        /**
         * 通用参数配置
         * */
        paramMap.put("handleOpinion", String.valueOf(editOnte.getText()));
        paramMap.put("id", String.valueOf(idEvent));
        ArticlesRepo.getMyPeportIDUpdateEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showToast(ReportApprovalActivity.this,"提交成功");

                Log.e("提交审批", "onSuccess: ");
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
