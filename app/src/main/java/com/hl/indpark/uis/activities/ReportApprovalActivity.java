package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.entities.events.MyPeportIDEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.fragments.ImageFragment;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.tv_event_onte)
    TextView tv_event_onte;
    @BindView(R.id.ed_event_onte)
    EditText editOnte;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @BindView(R.id.rl_note)
    RelativeLayout rlNote;
    @BindView(R.id.rl_ed_event_onte)
    RelativeLayout rledNote;
    boolean islMaxCount;
    private int idEvent;

    @OnClick({R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_report:
                String edOnte = editOnte.getText().toString().trim().replaceAll(" ", "");
                if (edOnte != null && !edOnte.equals("")) {
                    updataData();
                } else {
                    ToastUtil.showToast(this, "请填写处理意见");
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
                Util.login(String.valueOf(code),ReportApprovalActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void setViewData(MyPeportIDEvent idEvent) {
//        String eventType = "<font color='#3A3A3A'>事件类型</font><br/><font color='#BABABA'"+idEvent.typeName+"</font>";
        tvType.setText("事件类型:" + idEvent.typeName);
        tvTitle.setText("事件名称:" + idEvent.title);
        tvDes.setText("事件内容:" + idEvent.content);
        if (idEvent.imageList != null && idEvent.imageList.size() > 0) {
            mediaList = idEvent.imageList;
            mediaFragment.setActivity(this, mediaList);
            llImg.setVisibility(View.VISIBLE);
        } else {
            llImg.setVisibility(View.GONE);
        }
        if (idEvent.status == 1) {
            tvReport.setVisibility(View.GONE);
            rledNote.setVisibility(View.GONE);
            if (idEvent.handleOpinion != null && !idEvent.handleOpinion.equals("")) {
                rlNote.setVisibility(View.VISIBLE);
                tv_event_onte.setText("审批意见:" + idEvent.handleOpinion);
            } else {
                rlNote.setVisibility(View.GONE);
            }
        } else {
            tvReport.setVisibility(View.VISIBLE);
            rledNote.setVisibility(View.VISIBLE);
            rlNote.setVisibility(View.GONE);
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
        mediaFragment.setActivity(ReportApprovalActivity.this, mediaList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }

    public void updataData() {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(ReportApprovalActivity.this)
                .setMessage("提交中...")
                .setCancelable(false)
                .setCancelOutside(false);
        LoadingDailog dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        Map<String, String> paramMap = new HashMap<>();
        /**
         * 通用参数配置
         * */
        paramMap.put("handleOpinion", String.valueOf(editOnte.getText()));
        paramMap.put("id", String.valueOf(idEvent));
        ArticlesRepo.getMyPeportIDUpdateEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showToast(ReportApprovalActivity.this, "提交成功");
                MyApprovalEvent event = new MyApprovalEvent();
                EventBus.getDefault().post(event);
                dialog.cancel();
                finish();
                Log.e("提交审批", "onSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(ReportApprovalActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code),ReportApprovalActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });


    }


}
