package com.hl.indpark.uis.fragments;


import android.os.Bundle;

import com.hl.indpark.R;

import net.arvin.baselib.base.BaseFragment;


 /**
  * Created by yjl on 2021/3/9 13:19
  * Function：
  * Desc：报警统计---危险源
  */
public class TabSHSFragment extends BaseFragment {
    
    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_shs;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        ArticlesRepo.getEpAlarm("1").observe(this, new ApiObserver<EPAlarmEvent>() {
//            @Override
////            public void onSuccess(Response<EPAlarmEvent> response) {
////                ToastUtil.showToast(getActivity(), "登录成功");
////            }
////        });
    }

}
