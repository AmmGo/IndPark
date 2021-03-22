package com.hl.indpark.uis.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
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

import java.util.ArrayList;
import java.util.Calendar;
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
     * 学历 (默认0)0,本科 1,大专 2,研究生 3,其他
     * 婚姻状况(默认0)0,未婚 1,初婚；2：再婚；3：丧偶；4：离婚；5：未说明婚姻状况
     */
    @OnClick({R.id.ll_education_level, R.id.ll_marital_status, R.id.ll_date_of_birth, R.id.tv_report})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ll_education_level:
                List<String> edlvList = new ArrayList<>();
                edlvList.add("本科");
                edlvList.add("大专");
                edlvList.add("研究生");
                edlvList.add("其他");
                selectDialog(edlvList, 0);
                break;
            case R.id.ll_marital_status:
                List<String> marList = new ArrayList<>();
                marList.add("未婚");
                marList.add("已婚");
                marList.add("未说明婚姻状况");
                selectDialog(marList, 1);
                break;
            case R.id.ll_date_of_birth:
                // 日期对话框
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth ;
                        String strDay ;
                        int intMonth = monthOfYear+1;
                        int intDay = dayOfMonth;
                        if (intMonth<10){
                             strMonth = "0"+intMonth;
                        }else{
                            strMonth = intMonth+"";
                        }
                        if (intDay<10){
                            strDay = "0"+intDay;
                        }else{
                            strDay = ""+intDay;
                        }
                        tvDataBirth.setText(year + "-" + strMonth + "-" + strDay+" 00:00:00");
                    }
                }, year, calendar.get(Calendar.MONTH), day).show();
                break;
            case R.id.tv_report:
                Util.hideInputManager(SetUpActivity.this, v);
                upData();
                break;
            default:
        }
    }
    private void initDate() {
        // 获取日历的一个对象
        calendar = Calendar.getInstance();
        // 获取年月日时分秒的信息
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);
        //设置标题
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
        titleBar.getCenterTextView().setText("设置");
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
                    ToastUtil.showToast(SetUpActivity.this, "开启推送");
                } else {
                    JPushUtils.stopPush();
                    ToastUtil.showToast(SetUpActivity.this, "关闭推送");
                }
            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.showToast(SetUpActivity.this, "开启跟踪");
                } else {
                    ToastUtil.showToast(SetUpActivity.this, "关闭跟踪");
                }
            }
        });
        initData();
    }

    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
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
     * 学历 (默认0)0,本科 1,大专 2,研究生 3,其他
     * 婚姻状况(默认0)0,未婚 1,初婚；2：再婚；3：丧偶；4：离婚；5：未说明婚姻状况
     */
    public void initViewData() {
        editName.setText(userInfoEvent.name);
        editPhone.setText(userInfoEvent.phone);
        editCardId.setText(userInfoEvent.idNumber);
        String edllv = "0";
        String marStatus = "0";
        switch (userInfoEvent.degree) {
            case 0:
                edllv = "本科";
                break;
            case 1:
                edllv = "大专";
                break;
            case 2:
                edllv = "研究生";
                break;
            case 3:
                edllv = "其他";
                break;
            default:
                edllv = "请选择";
        }
        switch (userInfoEvent.maritalStatus) {
            case 0:
            case 4:
                marStatus = "未婚";
                break;
            case 1:
            case 2:
            case 3:
                marStatus = "已婚";
                break;
            case 5:
                marStatus = "未说明婚姻状况";
                break;
            default:
                marStatus = "请选择";
        }
        if (edllv.equals("请选择")) {
            tvEdLevl.setText("");
            tvqxz1.setVisibility(View.VISIBLE);

        } else {
            tvEdLevl.setText(edllv);
            tvqxz1.setVisibility(View.INVISIBLE);
        }
        if (marStatus.equals("请选择")) {
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
         * 通用参数配置
         * */
        if (TextUtils.isEmpty(editName.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(editCardId.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "请输入身份证号");
            return;
        }
        if (!IDCard.isIdCardNum(editCardId.getText().toString())) {
            ToastUtil.showToast(SetUpActivity.this, "证件号码格式不对");
            return;
        }
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(SetUpActivity.this)
                .setMessage("提交中...")
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
                ToastUtil.showToast(SetUpActivity.this, "提交成功");
                Log.e("修改用户信息", "onSuccess: ");
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
