package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;

import java.util.HashMap;
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
    @BindView(R.id.switch_1)
    Switch aSwitch;
    @BindView(R.id.switch_2)
    Switch bSwitch;
    private UserInfoEvent userInfoEvent;

    @OnClick({R.id.ll_education_level, R.id.ll_marital_status, R.id.ll_date_of_birth})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ll_education_level:
                break;
            case R.id.ll_marital_status:
                break;
            case R.id.ll_date_of_birth:
                break;
            default:
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        TitleBar titleBar = findViewById(R.id.title_bar);
//        titleBar.getCenterTextView().setText("设置");
//        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.showToast(SetUpActivity.this, "开启推送");
                } else {
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
            case 4:
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
        if (userInfoEvent.brithDate!=null&&!userInfoEvent.brithDate.equals("")) {
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
        paramMap1.put("name", "张嘉宾");
        paramMap1.put("personnelId", String.valueOf(userInfoEvent.personnelId));
        ArticlesRepo.getUserInfoUpdateEvent(paramMap1).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e("修改用户信息", "onSuccess: ");
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
