package com.hl.indpark.uis.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bigkoo.pickerview.TimePickerView;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.MachineCheck;
import com.hl.indpark.entities.events.MapPointEvent;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.permission.DefaultResourceProvider;
import com.hl.indpark.uis.fragments.MediaFragment;
import com.hl.indpark.utils.GlideEngine;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.EntDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.permissionhelper.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MachineCheckActivity extends BaseActivity {
    private EntDialog pop;
    private PopEvent popEvent;
    private MediaFragment mediaFragment;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> mediaList = new ArrayList<>();
    @BindView(R.id.rb_yqxz_s)
    RadioButton rb_yqxz_s;
    @BindView(R.id.rb_yqxz_f)
    RadioButton rb_yqxz_f;
    private static int YES = 1;
    private static int NO = 2;
    @BindView(R.id.tv_count_onte)
    TextView tvCount;
    @BindView(R.id.tv_sbmc)
    TextView tv_sbmc;
    @BindView(R.id.tv_sbdz)
    TextView tv_sbdz;
    @BindView(R.id.tv_sbxh)
    TextView tv_sbxh;
    @BindView(R.id.tv_m_c_xjry)
    TextView tv_m_c_xjry;
    @BindView(R.id.tv_xjrylxdh)
    TextView tv_xjrylxdh;
    @BindView(R.id.tv_xjrq)
    TextView tv_xjrq;
    @BindView(R.id.tv_xjqy)
    TextView tv_xjqy;
    @BindView(R.id.tv_m_c_qylxr)
    TextView tv_m_c_qylxr;
    @BindView(R.id.tv_m_c_qylxr_lxdh)
    TextView tv_m_c_qylxr_lxdh;
    @BindView(R.id.img_add)
    ImageView imageView;
    @BindView(R.id.img_xjqy)
    ImageView img_xjqy;

    @BindView(R.id.ll_sbdz)
    LinearLayout ll_sbdz;
    @BindView(R.id.ll_sbxh)
    LinearLayout ll_sbxh;
    @BindView(R.id.ed_event_onte)
    EditText ed_event_onte;
    boolean islMaxCount;
    private MachineCheck uploadData;
    private String uploadEventDecs;

    @OnTextChanged(value = R.id.ed_event_onte, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = editable.length();
        tvCount.setText(detailLength + "/300");
        if (detailLength == 299) {
            islMaxCount = true;
        }
        if (detailLength == 300 && islMaxCount) {
            ToastUtil.showToast(MachineCheckActivity.this, "????????????300??????");
            islMaxCount = false;
        }
    }

    @OnCheckedChanged({R.id.rb_yqxz_s, R.id.rb_yqxz_f})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rb_yqxz_s:
                if (ischanged) {
                    uploadData.isException = 1;
                }
                break;
            case R.id.rb_yqxz_f:
                if (ischanged) {
                    uploadData.isException = 2;
                }
                break;
        }
    }

    private PermissionUtil permissionUtil;

    @OnClick({R.id.ll_sbmc, R.id.ll_m_c_xjry, R.id.ll_m_c_xjrq, R.id.ll_m_c_xjqy, R.id.ll_m_c_qylxr, R.id.tv_report, R.id.img_add})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ll_sbmc:
                try {
                    pop = new EntDialog(MachineCheckActivity.this, popEvent, 105);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_report:
                uploadEventDecs = ed_event_onte.getText().toString().replaceAll(" ", "");
                uploadData.description = uploadEventDecs;
                if (uploadData.equipmentId==null) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }

                if (TextUtils.isEmpty(uploadData.examinePersonnel)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(uploadData.examineTime)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(uploadData.enterpriseId)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }
                if (uploadData.contactId == null) {
                    ToastUtil.showToast(this, "????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(uploadEventDecs)) {
                    ToastUtil.showToast(this, "?????????????????????");
                    return;
                }
                if (mediaList == null || mediaList.size() <= 0) {
                    ToastUtil.showToast(this, "????????????????????????");
                    return;
                }
                if (mediaList != null && mediaList.size() > 0) {
                    try {
                        for (int i = 0; i < mediaList.size(); i++) {
                            uploadFile.add(new File(mediaList.get(i).getCompressPath()));
                        }
                    } catch (Exception e) {
                        for (int i = 0; i < mediaList.size(); i++) {
                            uploadFile.add(new File(mediaList.get(i).getPath()));
                        }
                        e.printStackTrace();
                    }
                    getUpdateUserImgS(uploadFile);
                } else {
                    getUploadEvent();
                }
                break;
            case R.id.ll_m_c_xjry:
                try {
                    pop = new EntDialog(MachineCheckActivity.this, popEvent, 104);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_m_c_xjrq:
                //?????????????????????
                TimePickerView timePickerView = new TimePickerView.Builder(MachineCheckActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {
                        tv_xjrq.setText(getDataTime(date) + " 00:00:00");
                        uploadData.createTime = getDataTime(date) + " 00:00:00";
                        uploadData.examineTime = getDataTime(date) + " 00:00:00";
                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                        .setCancelText("??????")
                        .setSubmitText("??????")
                        .setContentSize(20)//??????????????????
                        .setTitleSize(20)//??????????????????
                        .setOutSideCancelable(true)
                        .isCyclic(true)
                        .setTextColorCenter(Color.BLACK)//????????????????????????
                        .setSubmitColor(Color.GRAY)//????????????????????????
                        .setCancelColor(Color.GRAY)//????????????????????????
                        .isCenterLabel(false)
                        .build();
                timePickerView.show();
                break;
            case R.id.ll_m_c_xjqy:
                if (Util.getIsRoleld()) {
                    try {
                        pop = new EntDialog(MachineCheckActivity.this, popEvent, 99);
                        pop.setCanceledOnTouchOutside(true);
                        pop.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ll_m_c_qylxr:
                Intent intent = new Intent(MachineCheckActivity.this, PhoneActivity.class);
                startActivityForResult(intent, 800);
                break;
            case R.id.img_add:
                permissionUtil.request("????????????????????????", Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.RequestPermissionListener() {
                    @Override
                    public void callback(boolean granted, boolean isAlwaysDenied) {
                        if (granted) {
                            PictureSelector.create(MachineCheckActivity.this)
                                    .openGallery(PictureMimeType.ofImage())//??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                                    .theme(R.style.picture_default_style)//????????????(????????????????????????) ????????????demo values/styles??? ?????????R.style.picture.white.style
                                    .maxSelectNum(6)// ???????????????????????? int
                                    .minSelectNum(1)// ?????????????????? int
                                    .imageEngine(GlideEngine.createGlideEngine())
                                    .selectionData(mediaList)
                                    .imageSpanCount(3)// ?????????????????? int
                                    .isCamera(true)// ???????????????????????? true or false
                                    .isZoomAnim(true)// ?????????????????? ???????????? ??????true
                                    .isEnableCrop(false)// ???????????? true or false
                                    .isCompress(true)// ???????????? true or false
                                    .minimumCompressSize(100)// ??????100kb??????????????????
                                    .forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
                        }
                    }
                });
                break;
            default:
        }
    }

    public String getDataTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * ??????fragment
     */
    private void setMediaFragment() {
        mediaFragment = new MediaFragment();
        mediaFragment.setActivity(this, selectList);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_media, mediaFragment).commitAllowingStateLoss();
    }

    private void initPermissionConfig() {
        PermissionUtil.setPermissionTextProvider(new DefaultResourceProvider());
        permissionUtil = new PermissionUtil.Builder().with(this).build();
    }

    private List<File> uploadFile = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = new ArrayList<>();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.clear();
                    mediaList.addAll(selectList);
                    mediaFragment.setActivity(this, mediaList);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    // ????????????
                    selectList = new ArrayList<>();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mediaList.addAll(selectList);
                    mediaFragment.setActivity(this, mediaList);
                    break;
                default:
            }
        } else {
            switch (requestCode) {
                case 800:
                    String qylxrName = data.getStringExtra("xjryName");
                    String qylxrUserid = data.getStringExtra("xjryUserid");
                    String qylxrPhone = data.getStringExtra("xjryPhone");
                    tv_m_c_qylxr.setText(qylxrName);
                    tv_m_c_qylxr_lxdh.setText(qylxrPhone);
                    uploadData.contactId = qylxrUserid;
                    break;
                default:
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_machine_check;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        uploadData = new MachineCheck();
        uploadData.isException = 1;
        uploadData.createId = Util.getUserId();
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("????????????");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initPermissionConfig();
        setMediaFragment();
        uploadFile = new ArrayList<>();
        mapData();
        hideShow();
    }

    //??????????????????????????????????????????????????????????????????????????????
    public void hideShow() {
        if (Util.getIsRoleld()) {

        } else {
            String entName = Util.getEnterpriseName();
            if (entName != null && entName.length() > 0) {
                tv_xjqy.setText(entName);
            }
            uploadData.enterpriseId = Util.getEnterpriseId();
            img_xjqy.setVisibility(View.GONE);
        }


    }

    //??????map??????
    public void mapData() {
        popEvent = new PopEvent();
        //????????????
        getCheckMachine();
        //????????????
        getXjryData();
        //????????????
        getEntName();
        //?????????????????????

    }


    @Subscribe
    public void getEntName(EntNameEvent event) {
        tv_xjqy.setText(event.name);
        uploadData.enterpriseId = String.valueOf(event.id);
        pop.cancel();
    }

    @Subscribe
    public void getXjry(PhoneEvent event) {
        tv_m_c_xjry.setText(event.name);
        tv_xjrylxdh.setText(event.phone);
        uploadData.examinePersonnel = String.valueOf(event.name);
        uploadData.phone = String.valueOf(event.phone);
        pop.cancel();
    }

    @Subscribe
    public void getMachine(MapPointEvent event) {
        tv_sbmc.setText(event.name);
        uploadData.equipmentName = event.name;
        uploadData.equipmentId = String.valueOf(event.id);
        uploadData.equipmentModel = event.model;
        uploadData.equipmentAddress = event.address;
        if (event.address != null) {
            tv_sbdz.setText(uploadData.equipmentAddress);
            ll_sbdz.setVisibility(View.VISIBLE);
        } else {
            ll_sbdz.setVisibility(View.GONE);
        }
        if (event.model != null) {
            tv_sbxh.setText(uploadData.equipmentModel);
            ll_sbxh.setVisibility(View.VISIBLE);
        } else {
            ll_sbxh.setVisibility(View.GONE);
        }

        pop.cancel();
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


    //????????????
    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                try {
                    popEvent.entNameEvents = response.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), MachineCheckActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    //???????????????
    public void getXjryData() {
        ArticlesRepo.getXjryCheck().observe(this, new ApiObserver<List<PhoneEvent>>() {
            @Override
            public void onSuccess(Response<List<PhoneEvent>> response) {
                List<PhoneEvent> data = response.getData();
                popEvent.phoneList = data;

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), MachineCheckActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    //????????????
    public void getCheckMachine() {
        ArticlesRepo.getCheckPoint().observe(this, new ApiObserver<List<MapPointEvent>>() {
            @Override
            public void onSuccess(Response<List<MapPointEvent>> response) {
                Log.e("??????", "onSuccess: ");
                List<MapPointEvent> list = new ArrayList<>();
                list = response.getData();
                popEvent.machineList = response.getData();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), MachineCheckActivity.this);
                ToastUtil.showToast(MachineCheckActivity.this, msg);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(MachineCheckActivity.this, "??????????????????");
            }
        });
    }

    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {

        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
//            //?????????????????????
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//            //file?????????????????????????????????,?????????file.getName()????????????????????????,??????????????????????????????
//            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            parts.add(part);
        }
        return parts;
    }

    private String imgS;
    private LoadingDailog dialog;

    public void getUpdateUserImgS(List<File> files) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(MachineCheckActivity.this)
                .setMessage("?????????...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        List<MultipartBody.Part> partList = filesToMultipartBodyParts(files);
        Log.e("fdas", "getUpdateUserImgS: ");
        ArticlesRepo.getUploadImgS(partList).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("??????????????????", "onSuccess: " + response.getData());
                imgS = response.getData();
                getUploadEvent();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), MachineCheckActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getUploadEvent() {
        if (mediaList != null && mediaList.size() > 0) {

        } else {
            LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(MachineCheckActivity.this)
                    .setMessage("?????????...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog = loadBuilder.create();
            dialog.getWindow().setDimAmount(0f);
            dialog.show();
        }

        Map<String, String> paramMap = new HashMap<>();
        /**
         * ??????????????????
         * */

        paramMap.put("equipmentId", String.valueOf(uploadData.equipmentId));
        paramMap.put("equipmentName", uploadData.equipmentName);
        if (uploadData.equipmentModel != null) {
            paramMap.put("equipmentModel", uploadData.equipmentModel);
        }
        if (uploadData.equipmentAddress != null) {
            paramMap.put("equipmentAddress", uploadData.equipmentAddress);
        }
        if (uploadData.examinePersonnel != null) {
            paramMap.put("examinePersonnel", uploadData.examinePersonnel);
        }
        if (uploadData.examinePersonnel != null) {
            paramMap.put("examinePersonnel", uploadData.examinePersonnel);
        }
        if (uploadData.examinePersonnel != null) {
            paramMap.put("examinePersonnel", uploadData.examinePersonnel);
        }
        if (uploadData.examineTime != null) {
            paramMap.put("examineTime", uploadData.examineTime);
        }
        if (uploadData.enterpriseId != null) {
            paramMap.put("enterpriseId", uploadData.enterpriseId);
        }
        if (String.valueOf(uploadData.contactId) != null) {
            paramMap.put("contactId", String.valueOf(uploadData.contactId));
        }
        paramMap.put("description", uploadData.description);
        paramMap.put("createId", uploadData.createId);
        paramMap.put("phone", uploadData.phone);
        paramMap.put("isException", String.valueOf(uploadData.isException));
        if (imgS != null && !imgS.equals("")) {
            paramMap.put("image", imgS);
        }
        ArticlesRepo.getMachineCheckReportEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("????????????", "onSuccess: ");
                ToastUtil.showToast(MachineCheckActivity.this, "????????????");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(MachineCheckActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), MachineCheckActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }
}