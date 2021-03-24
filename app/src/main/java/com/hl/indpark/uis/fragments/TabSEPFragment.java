package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.EPAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartEPDataActivity;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.PieChartView;

import net.arvin.baselib.base.BaseFragment;


/**
 * Created by yjl on 2021/3/9 13:20
 * Function：
 * Desc：报警统计---环保
 */
public class TabSEPFragment extends BaseFragment {
    private PieChartView mPieChart;
    private LinearLayout linearLayout;
    private EPAlarmEvent alarmEvent;

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_sep;
    }

    private void initData() {
        ArticlesRepo.getEpAlarm("1").observe(this, new ApiObserver<EPAlarmEvent>() {
            @Override
            public void onSuccess(Response<EPAlarmEvent> response) {
                try {
                    if (response != null && response.getData() != null) {
                        alarmEvent = response.getData();
                        if (response.getData().totalNumber > 0) {
                            double[] datas = new double[]{alarmEvent.gasNumber, alarmEvent.waterNumber};
                            String[] texts = new String[]{alarmEvent.gasNumber + "," + alarmEvent.gasStr, alarmEvent.waterNumber + "," + alarmEvent.waterStr};
                            String[] strs = new String[]{alarmEvent.gasStr, alarmEvent.waterStr};
                            mPieChart.setStrList(strs);
                            mPieChart.setDatas(datas);
                            mPieChart.setTexts(texts);
                            mPieChart.setMaxNum(datas.length);
                            mPieChart.setCenterText(alarmEvent.totalNumber + ",环保报警");
                            mPieChart.invalidate();
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (response.getData().totalNumber == 0) {
                            double[] datas = new double[]{70, 30};
                            String[] texts = new String[]{alarmEvent.gasNumber + "," + alarmEvent.gasStr, alarmEvent.waterNumber + "," + alarmEvent.waterStr};
                            String[] strs = new String[]{alarmEvent.gasStr, alarmEvent.waterStr};
                            mPieChart.setStrList(strs);
                            mPieChart.setDatas(datas);
                            mPieChart.setTexts(texts);
                            mPieChart.setMaxNum(datas.length);
                            mPieChart.setCenterText(alarmEvent.totalNumber + ",环保报警");
                            mPieChart.invalidate();
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.GONE);
                        }

                        Log.e("dafda", "onSuccess: ");
                    } else {
                        linearLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                linearLayout.setVisibility(View.GONE);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                linearLayout.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPieChart = root.findViewById(R.id.chart_sep);
        linearLayout = root.findViewById(R.id.ll_gone);
        initData();
        mPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PieChartEPDataActivity.class);
                    intent.putExtra("type", Util.hbDay);
                    startActivity(intent);
            }
        });
    }

}
