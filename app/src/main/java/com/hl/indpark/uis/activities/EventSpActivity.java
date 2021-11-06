package com.hl.indpark.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.hl.indpark.widgit.EntDialog;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
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
    @BindView(R.id.ed_event_onte)
    EditText editOnte;
    @BindView(R.id.ll_img)
    LinearLayout llImg;
    @BindView(R.id.rl_ed_event_onte)
    RelativeLayout rledNote;
    @BindView(R.id.tv_event_time)
    TextView tv_event_time;
    @BindView(R.id.tv_event_person)
    TextView tv_event_person;
    @BindView(R.id.tv_event_phone)
    TextView tv_event_phone;
    @BindView(R.id.ll_clr)
    LinearLayout ll_clr;
    boolean islMaxCount;
    private int idEvent;
    int isWpSbEnd = 0;
    boolean isWp = false;
    boolean isSb = false;
    boolean isGone = false;

    private String nameP;
    @BindView(R.id.rb_wp)
    RadioButton rb_wp;
    @BindView(R.id.rb_sb)
    RadioButton rb_sb;
    @BindView(R.id.rb_end)
    RadioButton rb_end;
    @BindView(R.id.rg_sfnj)
    RadioGroup rg_sfnj;
    @BindView(R.id.tv_clr)
    TextView tv_clr;
    private Clr eventClr;
    private List<Clr> clrsSb;
    private List<Clr> clrsWp;
    private String edOnte;
    private String uploadTvClr;

    @OnCheckedChanged({R.id.rb_wp, R.id.rb_sb, R.id.rb_end})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_wp:
                if (ischanged) {
                    isWpSbEnd = 2;
                    ll_clr.setVisibility(View.VISIBLE);
                    tv_clr.setText("请选择处理人");
                    popEvent.clrList = clrsWp;
                }
                break;
            case R.id.rb_sb:
                if (ischanged) {
                    ll_clr.setVisibility(View.VISIBLE);
                    tv_clr.setText("请选择处理人");
                    isWpSbEnd = 1;
                    popEvent.clrList = clrsSb;
                }
                break;
            case R.id.rb_end:
                if (ischanged) {
                    ll_clr.setVisibility(View.GONE);
                    isWpSbEnd = 3;
                }
                break;
        }
    }

    @Subscribe
    public void getClr(Clr event) {
        tv_clr.setText(event.name);
        eventClr.id = event.id;
        eventClr.phone = event.phone;
        pop.cancel();
    }

    EntDialog pop;

    @OnClick({R.id.tv_report, R.id.ll_clr, R.id.tv_event_flow, R.id.rl_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_event_flow:
                Intent intent = new Intent(EventSpActivity.this, EventFlowActivity.class);
                intent.putExtra("id", idEvent);
                startActivity(intent);
                break;
            case R.id.tv_report:
                Util.hideInputManager(EventSpActivity.this, v);
                edOnte = editOnte.getText().toString().trim().replaceAll(" ", "");
                if (isWpSbEnd != 3) {
                    uploadTvClr = tv_clr.getText().toString().replaceAll(" ", "").replace("请选择处理人", "");
                    if (TextUtils.isEmpty(uploadTvClr)) {
                        ToastUtil.showToast(this, "请选择处理人");
                        return;
                    }
                }
                if (TextUtils.isEmpty(edOnte)) {
                    ToastUtil.showToast(this, "请填写处理意见");
                    return;
                }
                switch (isWpSbEnd) {
                    case 0:
                        break;
                    case 1:
                        getSbSp();
                        break;
                    case 2:
                        getWpSp();
                        break;
                    case 3:
                        getEndSp();
                        break;
                }

                break;
            case R.id.ll_clr:
                try {
                    pop = new EntDialog(EventSpActivity.this, popEvent, 107);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        nameP = intent.getStringExtra("name");
        eventClr = new Clr();
        getIsWpSb(idEvent);
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
        tv_event_person.setText(nameP == null ? "上报人:" : "上报人:" + nameP);
        tv_event_phone.setText("联系电话:" + idEvent.phone);
        if (isGone) {
            rg_sfnj.setVisibility(View.GONE);
            rledNote.setVisibility(View.GONE);
            tvReport.setVisibility(View.GONE);
        } else {
            rg_sfnj.setVisibility(View.VISIBLE);
            rledNote.setVisibility(View.VISIBLE);
            tvReport.setVisibility(View.VISIBLE);
        }
        idEvent.imageList = new ArrayList<>();
        if (idEvent.sceneImages != null) {
            if (idEvent.sceneImages.contains(",")) {
                String[] strings = idEvent.sceneImages.split(",");
                idEvent.imageList.addAll(Arrays.asList(strings));
            } else {
                idEvent.imageList.add(idEvent.sceneImages);
            }
            if (idEvent.imageList != null && idEvent.imageList.size() > 0) {
                mediaList = idEvent.imageList;
                mediaFragment.setActivity(this, mediaList);
                llImg.setVisibility(View.VISIBLE);
            } else {
                llImg.setVisibility(View.GONE);
            }
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
                IsWpSb isWpSb = new IsWpSb();
                isWpSb = response.getData();
                if (isWpSb.assign) {
                    getWpry();
                    isWp = true;
                    rb_wp.setClickable(true);
                } else {
                    rb_wp.setEnabled(false);
                    rb_wp.setClickable(false);
                }
                if (isWpSb.report) {
                    getSbry();
                    isSb = true;
                    rb_sb.setClickable(true);
                } else {
                    rb_sb.setEnabled(false);
                    rb_sb.setClickable(false);
                }
                if (!isWpSb.assign && !isWpSb.report) {
                    isGone = true;
                } else {
                    isGone = false;
                }
                getData(idEvent);
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
                clrsWp = new ArrayList<>();
                for (int i = 0; i < wpry.size(); i++) {
                    clrsWp.add(new Clr(wpry.get(i).name, wpry.get(i).id, wpry.get(i).phone));
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
     * 获取上报人员
     */
    public void getSbry() {
        ArticlesRepo.getSbry().observe(this, new ApiObserver<Sbry>() {
            @Override
            public void onSuccess(Response<Sbry> response) {

                if (response.getData() != null) {
                    clrsSb = new ArrayList<>();
                    clrsSb.add(new Clr(response.getData().name, response.getData().id, response.getData().phone));
                    popEvent.clrList = clrsSb;
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
        paramMap.put("eventId", String.valueOf(idEvent));
        paramMap.put("msg", edOnte);
        paramMap.put("personnelId", String.valueOf(eventClr.id));

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
        paramMap.put("eventId", String.valueOf(idEvent));
        paramMap.put("msg", edOnte);
        paramMap.put("personnelId", String.valueOf(eventClr.id));

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
        paramMap.put("eventId", String.valueOf(idEvent));
        paramMap.put("msg", edOnte);
        paramMap.put("personnelId", String.valueOf(eventClr.id));

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

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

}