package com.hl.indpark.uis.fragments;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.events.MyPeportEvent;
import com.hl.indpark.entities.events.MyPeportIDEvent;
import com.hl.indpark.entities.events.UserInfoEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.RoundImageView;

import net.arvin.baselib.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by yjl on 2021/3/8 11:06
 * Function：
 * Desc：我的
 */
public class SelfFragment extends BaseFragment {

    @BindView(R.id.imgAvatar)
    RoundImageView imageView;
    @BindView(R.id.txtName)
    TextView tvName;
    @BindView(R.id.tv_photo_sjh)
    TextView tvPhone;
    private UserInfoEvent userInfoEvent;

    @Override
    protected int getContentView() {
        return R.layout.fragment_self;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initData();
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
        ArticlesRepo.getMyMsgEvent("1", "20").observe(this, new ApiObserver<MyMsgEvent>() {
            @Override
            public void onSuccess(Response<MyMsgEvent> response) {
                Log.e("我的消息", "onSuccess: ");
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
        ArticlesRepo.getMyPeportEvent("1", "20").observe(this, new ApiObserver<MyPeportEvent>() {
            @Override
            public void onSuccess(Response<MyPeportEvent> response) {
                Log.e("上报列表", "onSuccess: ");
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
        ArticlesRepo.getMyApprovalEvent("1", "20").observe(this, new ApiObserver<MyApprovalEvent>() {
            @Override
            public void onSuccess(Response<MyApprovalEvent> response) {
                Log.e("审批列表", "onSuccess: ");
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
        Map<String, String> paramMap = new HashMap<>();
        /**
         * 通用参数配置
         * */
        paramMap.put("handleOpinion", "这个事件没处理");
        paramMap.put("id", "22");
        ArticlesRepo.getMyPeportIDUpdateEvent(paramMap).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
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
        ArticlesRepo.getMyPeportIDEvent("23").observe(this, new ApiObserver<MyPeportIDEvent>() {
            @Override
            public void onSuccess(Response<MyPeportIDEvent> response) {
                Log.e("审批列表-ID-查询事件", "onSuccess: ");
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
    }
}
