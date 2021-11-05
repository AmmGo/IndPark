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
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.new2.Clr;
import com.hl.indpark.entities.new2.EventId;
import com.hl.indpark.entities.new2.IsWpSb;
import com.hl.indpark.entities.new2.Sbry;
import com.hl.indpark.entities.new2.Wpry;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.fragments.ImageSpFragment;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EventSpActivity extends BaseActivity {
    PopEvent popEvent = new PopEvent();
    private LoadingDailog dialog;
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
    @BindView(R.id.tv_event_time)
    TextView tv_event_time;
    @BindView(R.id.tv_event_person)
    TextView tv_event_person;
    @BindView(R.id.tv_event_phone)
    TextView tv_event_phone;
    boolean islMaxCount;
    private int idEvent;

    @OnClick({R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_report:
                Util.hideInputManager(EventSpActivity.this, v);
                String edOnte = editOnte.getText().toString().trim().replaceAll(" ", "");
                if (edOnte != null && !edOnte.equals("")) {
//                    updataData();
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
            ToastUtil.showToast(EventSpActivity.this, "最多输入300个字");
            islMaxCount = false;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_event_sp;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        idEvent = intent.getIntExtra("id", 0);
        getData(idEvent);
        setMediaFragment();
    }

    public void getData(int id) {
        ArticlesRepo.getMyPeportIDEvent(String.valueOf(id)).observe(this, new ApiObserver<EventId>() {
            @Override
            public void onSuccess(Response<EventId> response) {
                Log.e("审批列表-ID-查询事件", "onSuccess: ");
                try {
                    setViewData(response.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventSpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void setViewData(EventId idEvent) {
//        String eventType = "<font color='#3A3A3A'>事件类型</font><br/><font color='#BABABA'"+idEvent.typeName+"</font>";
        tvType.setText("事件类型:" + Util.selectTypeName(idEvent.eventType));
        tvTitle.setText("事件名称:" + idEvent.name);
        tvDes.setText("事件内容:" + idEvent.detail);
        tv_event_time.setText("上报时间:" + idEvent.createTime);
        tv_event_person.setText(idEvent.createName == null ? "上报人:" : "上报人:" + idEvent.createName);
        tv_event_phone.setText("联系电话:" + idEvent.phone);
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
            if (idEvent.handlingOpinions != null && !idEvent.handlingOpinions.equals("")) {
                rlNote.setVisibility(View.VISIBLE);
                tv_event_onte.setText("审批意见:" + idEvent.handlingOpinions);
            } else {
                rlNote.setVisibility(View.GONE);
            }
        } else {
            tvReport.setVisibility(View.VISIBLE);
            rledNote.setVisibility(View.VISIBLE);
            rlNote.setVisibility(View.GONE);
        }
        if (idEvent.status == 1) {
            tvReport.setVisibility(View.GONE);
            rledNote.setVisibility(View.GONE);
        }

    }

    /**
     * 设置fragment
     */
    private ImageSpFragment mediaFragment;
    private List<String> mediaList = new ArrayList<>();

    private void setMediaFragment() {
        mediaFragment = new ImageSpFragment();
        mediaList = new ArrayList<>();
        mediaFragment.setActivity(EventSpActivity.this, mediaList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }

    /**
     * 下一步操作：委派，上报
     */
    public void getIsWpSb(int id) {
        ArticlesRepo.getIsWpsb(String.valueOf(id)).observe(this, new ApiObserver<IsWpSb>() {
            @Override
            public void onSuccess(Response<IsWpSb> response) {

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventSpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    /**
     * 获取委派人员
     */
    public void getWpry() {
        ArticlesRepo.getWpry().observe(this, new ApiObserver<List<Wpry>>() {
            @Override
            public void onSuccess(Response<List<Wpry>> response) {
                List<Wpry> wpry = response.getData();
                List<Clr> clrs = new ArrayList<>();
                for (int i = 0; i < wpry.size(); i++) {
                    clrs.add(new Clr(wpry.get(i).name, wpry.get(i).id, wpry.get(i).phone));
                }
                popEvent.clrList = clrs;

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventSpActivity.this);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    /**
     * 获取上报人员
     */
    public void getSbry() {
        ArticlesRepo.getSbry().observe(this, new ApiObserver<Sbry>() {
            @Override
            public void onSuccess(Response<Sbry> response) {

                if (response.getData() != null) {
                    List<Clr> clrs = new ArrayList<>();
                    clrs.add(new Clr(response.getData().name, response.getData().id, response.getData().phone));
                    popEvent.clrList = clrs;
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), EventSpActivity.this);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    /**
     * 委派
     */
    public void getWpSp() {

        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(EventSpActivity.this)
                .setMessage("提交中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("eventId", "事件id");
        paramMap.put("msg", "意见");
        paramMap.put("personnelId", "下一步操作人");

        ArticlesRepo.getEventAssign(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上报事件", "onSuccess: ");
                ToastUtil.showToast(EventSpActivity.this, "提交成功");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(EventSpActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), EventSpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }

    /**
     * 上报
     */
    public void getSbSp() {

        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(EventSpActivity.this)
                .setMessage("提交中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("eventId", "事件id");
        paramMap.put("msg", "意见");
        paramMap.put("personnelId", "下一步操作人");

        ArticlesRepo.getEventReport(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上报事件", "onSuccess: ");
                ToastUtil.showToast(EventSpActivity.this, "提交成功");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(EventSpActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), EventSpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }

    /**
     * 结束
     */
    public void getEndSp() {

        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(EventSpActivity.this)
                .setMessage("提交中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("eventId", "事件id");
        paramMap.put("msg", "意见");
        paramMap.put("personnelId", "下一步操作人");

        ArticlesRepo.getEventEnd(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("上报事件", "onSuccess: ");
                ToastUtil.showToast(EventSpActivity.this, "提交成功");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(EventSpActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), EventSpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }
}