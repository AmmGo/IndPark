package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.Api;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.MyApprovalActivity;
import com.hl.indpark.uis.activities.MyMsgActivity;
import com.hl.indpark.uis.activities.MyReportActivity;
import com.hl.indpark.uis.activities.SetUpActivity;
import com.hl.indpark.utils.RoundImageView;
import com.hl.indpark.widgit.CircleImageView;

import net.arvin.baselib.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

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
    private UserInfoEvent userInfoEvent;
    private boolean hideNew;

    @OnClick({R.id.ll_wd_shenp, R.id.ll_wdsp, R.id.ll_wdxx, R.id.img_set_up})
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
        initData();
        getNewHide();
    }

    public void initData() {
        ArticlesRepo.getUserInfoEvent().observe(this, new ApiObserver<UserInfoEvent>() {
            @Override
            public void onSuccess(Response<UserInfoEvent> response) {
                Log.e("用户成功", "onSuccess: ");
                userInfoEvent = response.getData();
                initViewData(userInfoEvent);
                Map<String, String> paramMap1 = new HashMap<>();
                /**
                 * 通用参数配置
                 * */
                paramMap1.put("name", "张嘉宾");
                paramMap1.put("personnelId", String.valueOf(userInfoEvent.personnelId));
                ArticlesRepo.getUserInfoUpdateEvent(paramMap1).observe(getActivity(), new ApiObserver<String>() {
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

    public void getNewHide() {
        ArticlesRepo.getMyMsgEvent(1, 10).observe(this, new ApiObserver<MyMsgEvent>() {
            @Override
            public void onSuccess(Response<MyMsgEvent> response) {
                Log.e("我的消息", "onSuccess: ");
                MyMsgEvent event = response.getData();
                if (event != null && event.records.size() > 0) {
                    imgNewMsg.setVisibility(View.VISIBLE);
                } else {
                    imgNewMsg.setVisibility(View.GONE);
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

    public void initViewData(UserInfoEvent user) {
        tvName.setText(user.nickName);
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
