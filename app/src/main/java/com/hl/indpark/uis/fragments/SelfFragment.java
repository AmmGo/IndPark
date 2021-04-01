package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.MeActivity;
import com.hl.indpark.uis.activities.MyApprovalActivity;
import com.hl.indpark.uis.activities.MyMsgActivity;
import com.hl.indpark.uis.activities.MyReportActivity;
import com.hl.indpark.uis.activities.SetUpActivity;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.CircleImageView;

import net.arvin.baselib.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yjl on 2021/3/8 11:06
 * Function：
 * Desc：我的
 */
public class SelfFragment extends BaseFragment {

    @BindView(R.id.imgAvatar)
    CircleImageView imageView;
    @BindView(R.id.txtName)
    TextView tvName;
    @BindView(R.id.img_new_msg)
    ImageView imgNewMsg;
    @BindView(R.id.tv_photo_sjh)
    TextView tvPhone;
    @BindView(R.id.ll_wd_shenp)
    LinearLayout ll_wd_shenp;
    private UserInfoEvent userInfoEvent;
    private boolean hideNew;
    private String msgNum;

    @OnClick({R.id.ll_wd_shenp, R.id.ll_wdsp, R.id.ll_wdxx, R.id.img_set_up, R.id.ll_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wd_shenp:
                startActivity(new Intent(getActivity(), MyApprovalActivity.class));
//                startActivity(new Intent(getActivity(), ReportApprovalActivity.class));
                break;
            case R.id.ll_wdsp:
                startActivity(new Intent(getActivity(), MyReportActivity.class));
                break;
            case R.id.ll_wdxx:
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
                break;
            case R.id.img_set_up:
                startActivity(new Intent(getActivity(), MeActivity.class));
                break;
            case R.id.ll_info:
                startActivity(new Intent(getActivity(), SetUpActivity.class));
                break;
            default:
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_self;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String roleId = SharePreferenceUtil.getKeyValue("roleId");
        if (roleId != null && !roleId.equals("") && roleId.equals("46")) {
            ll_wd_shenp.setVisibility(View.GONE);
        } else {
            ll_wd_shenp.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        initData();
        getNewHide();
        super.onResume();
    }

    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
                userInfoEvent = response.getData();
                initViewData(userInfoEvent);
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code),getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    public void getNewHide(){
        ArticlesRepo.getMsgRead().observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response.getData() != null && !response.getData().equals("")) {
                    if (response.getData().equals("0")) {
                        imgNewMsg.setVisibility(View.GONE);
                    } else {
                        imgNewMsg.setVisibility(View.VISIBLE);
                    }
                }
                Log.e("未读消息", "onSuccess: " + response.getData());
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    public void initViewData(UserInfoEvent user) {
        tvName.setText(user.name);
        tvPhone.setText(user.phone);
        if (user.sex == 1) {
            imageView.setBackgroundResource(R.mipmap.user_img_men);
        } else {
            imageView.setBackgroundResource(R.mipmap.user_img_nv);
        }
        //设置图片圆角角度
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.a_error)
//                .fallback(R.drawable.a_error)
//                .error(R.drawable.a_error);
//        Glide.with(getActivity()).load("https://gitee.com/ammgo/zjb/raw/master/blog_img/2020_08_10/pexels-eberhard-grossgasteiger-691668.jpg")
//                .apply(options).into(imageView);
    }

}
