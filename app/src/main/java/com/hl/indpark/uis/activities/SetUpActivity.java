package com.hl.indpark.uis.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bigkoo.pickerview.TimePickerView;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.IDCard;
import com.hl.indpark.utils.JPushUtils;
import com.hl.indpark.utils.SelectDialog;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SetUpActivity extends BaseActivity {
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.edit_card_id)
    EditText editCardId;
    @BindView(R.id.tv_education_level)
    TextView tvEdLevl;
    @BindView(R.id.tv_marital_status)
    TextView tvMarStatus;
    @BindView(R.id.tv_date_of_birth)
    TextView tvDataBirth;
    @BindView(R.id.tv_sele_1)
    TextView tvqxz1;
    @BindView(R.id.tv_sele_2)
    TextView tvqxz2;
    @BindView(R.id.tv_sele_3)
    TextView tvqxz3;
    @BindView(R.id.tv_report)
    TextView tv_report;
    @BindView(R.id.switch_1)
    Switch aSwitch;
    @BindView(R.id.switch_2)
    Switch bSwitch;
    private UserInfoEvent userInfoEvent;
    private int updataEdLv = 0;
    private int updataMarital = 0;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private Calendar calendar = null;
    /**
     * ?????? (??????0)0,?????? 1,?????? 2,????????? 3,??????
     * ????????????(??????0)0,?????? 1,?????????2????????????3????????????4????????????5????????????????????????
     */
    @OnClick({R.id.ll_education_level, R.id.ll_marital_status, R.id.ll_date_of_birth, R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ll_education_level:
                List<String> edlvList = new ArrayList<>();
                edlvList.add("??????");
                edlvList.add("??????");
                edlvList.add("?????????");
                edlvList.add("??????");
                selectDialog(edlvList, 0);
                break;
            case R.id.ll_marital_status:
                List<String> marList = new ArrayList<>();
                marList.add("??????");
                marList.add("??????");
                marList.add("?????????????????????");
                selectDialog(marList, 1);
                break;
            case R.id.ll_date_of_birth:
                //?????????????????????????????????
                TimePickerView timePickerView = new TimePickerView.Builder(SetUpActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {
                        tvDataBirth.setText(getDataTime(date)+" 00:00:00");
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
            case R.id.tv_report:
                Util.hideInputManager(SetUpActivity.this, v);
                upData();
                break;
            default:
        }
    }
    public String getDataTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    private void initDate() {
        // ???????????????????????????
        calendar = Calendar.getInstance();
        // ?????????????????????????????????
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        //????????????
    }
    private void selectDialog(List<String> list, int type) {
        List<String> names = new ArrayList<>();
        names.addAll(list);
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (type == 0) {
                            updataEdLv = 0;
                            tvEdLevl.setText(names.get(position));
                        } else {
                            updataMarital = 0;
                            tvMarStatus.setText(names.get(position));
                        }
                        break;
                    case 1:
                        if (type == 0) {
                            updataEdLv = 1;
                            tvEdLevl.setText(names.get(position));
                        } else {
                            updataMarital = 1;
                            tvMarStatus.setText(names.get(position));
                        }
                        break;
                    case 2:
                        if (type == 0) {
                            updataEdLv = 2;
                            tvEdLevl.setText(names.get(position));
                        } else {
                            updataMarital = 5;
                            tvMarStatus.setText(names.get(position));
                        }
                        break;
                    case 3:
                        if (type == 0) {
                            updataEdLv = 3;
                            tvEdLevl.setText(names.get(position));
                        } else {

                        }
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("??????");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initDate();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushUtils.resumePush();
                    ToastUtil.showToast(SetUpActivity.this, "????????????");
                } else {
                    JPushUtils.stopPush();
                    ToastUtil.showToast(SetUpActivity.this, "????????????");
                }
            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.showToast(SetUpActivity.this, "????????????");
                } else {
                    ToastUtil.showToast(SetUpActivity.this, "????????????");
                }
            }
        });
        initData();
    }

    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("????????????", "onSuccess: ");
                userInfoEvent = response.getData();
                try {
                    initViewData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code),SetUpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    /**
     * ?????? (??????0)0,?????? 1,?????? 2,????????? 3,??????
     * ????????????(??????0)0,?????? 1,?????????2????????????3????????????4????????????5????????????????????????
     */
    public void initViewData() {
        editName.setText(userInfoEvent.name);
        editPhone.setText(userInfoEvent.phone);
        editCardId.setText(userInfoEvent.idNumber);
        String edllv = "0";
        String marStatus = "0";
        switch (userInfoEvent.degree) {
            case 0:
                edllv = "??????";
                break;
            case 1:
                edllv = "??????";
                break;
            case 2:
                edllv = "?????????";
                break;
            case 3:
                edllv = "??????";
                break;
            default:
                edllv = "?????????";
        }
        switch (userInfoEvent.maritalStatus) {
            case 0:
            case 4:
                marStatus = "??????";
                break;
            case 1:
            case 2:
            case 3:
                marStatus = "??????";
                break;
            case 5:
                marStatus = "?????????????????????";
                break;
            default:
                marStatus = "?????????";
        }
        if (edllv.equals("?????????")) {
            tvEdLevl.setText("");
            tvqxz1.setVisibility(View.VISIBLE);

        } else {
            tvEdLevl.setText(edllv);
            tvqxz1.setVisibility(View.INVISIBLE);
        }
        if (marStatus.equals("?????????")) {
            tvMarStatus.setText("");
            tvqxz2.setVisibility(View.VISIBLE);

        } else {
            tvMarStatus.setText(marStatus);
            tvqxz2.setVisibility(View.INVISIBLE);
        }
        if (userInfoEvent.brithDate != null && !userInfoEvent.brithDate.equals("")) {
            tvDataBirth.setText(userInfoEvent.brithDate);
            tvqxz3.setVisibility(View.INVISIBLE);

        } else {
            tvDataBirth.setText("");
            tvqxz3.setVisibility(View.VISIBLE);
        }


    }

    public void upData() {
        Map<String, String> paramMap1 = new HashMap<>();
        /**
         * ??????????????????
         * */
        if (TextUtils.isEmpty(editName.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "???????????????");
            return;
        }
        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "??????????????????");
            return;
        }
        if (TextUtils.isEmpty(editCardId.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "?????????????????????");
            return;
        }
        if (!IDCard.isIdCardNum(editCardId.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "????????????????????????");
            return;
        }
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(SetUpActivity.this)
                .setMessage("?????????...")
                .setCancelable(false)
                .setCancelOutside(false);
        LoadingDailog dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        paramMap1.put("name", editName.getText().toString());
        paramMap1.put("phone", editPhone.getText().toString());
        paramMap1.put("idNumber", editCardId.getText().toString());
        paramMap1.put("degree", String.valueOf(updataEdLv));
        paramMap1.put("maritalStatus", String.valueOf(updataMarital));
        paramMap1.put("brithDate", tvDataBirth.getText().toString());
        paramMap1.put("personnelId", String.valueOf(userInfoEvent.personnelId));
        ArticlesRepo.getUserInfoUpdateEvent(paramMap1).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                ToastUtil.showToast(SetUpActivity.this, "????????????");
                Log.e("??????????????????", "onSuccess: ");
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                dialog.cancel();
                ToastUtil.showToast(SetUpActivity.this, msg);
                Util.login(String.valueOf(code),SetUpActivity.this);
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.cancel();
                super.onError(throwable);

            }
        });
    }


    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(SetUpActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!SetUpActivity.this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


}
