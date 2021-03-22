package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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

import butterknife.OnCheckedChanged;


/**
 * Created by yjl on 2021/3/9 13:20
 * Function：查询类型 1 当天 2 当月 3 当季度 4 当年
 * Desc：报警分析---环保
 */
public class TabAEPFragment extends BaseFragment {


    private PieChartView mPieChart;
    private String type = "2";
    private int typeData = 2;
    private LinearLayout linearLayout;

    @OnCheckedChanged({R.id.rg_month, R.id.rg_quarter, R.id.rg_year})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rg_month:
                if (ischanged) {
                    type = "2";
                    typeData = Util.hbMonthly;

                    initData(type);
                }
                break;
            case R.id.rg_quarter:
                if (ischanged) {
                    type = "3";
                    typeData = Util.wxyQuarter;
                    initData(type);
                }
                break;
            case R.id.rg_year:
                if (ischanged) {
                    type = "4";
                    typeData = Util.wxyYear;
                    initData(type);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_aep;
    }

    private void initData(String ty) {
        ArticlesRepo.getEpAlarm(ty).observe(this, new ApiObserver<EPAlarmEvent>() {
            @Override
            public void onSuccess(Response<EPAlarmEvent> response) {
                if (response != null && response.getData() != null&&response.getData().totalNumber>0) {
                    try {
                        EPAlarmEvent alarmEvent = response.getData();
                        if (alarmEvent!=null&&alarmEvent.totalNumber>0){
                            double[] datas = new double[]{alarmEvent.gasNumber,alarmEvent.waterNumber};
                            String[] texts = new String[]{alarmEvent.gasNumber+","+alarmEvent.gasStr, alarmEvent.waterNumber+","+alarmEvent.waterStr};
                            String[] strs = new String[]{alarmEvent.gasStr,alarmEvent.waterStr};
                            mPieChart.setStrList(strs);
                            mPieChart.setDatas(datas);
                            mPieChart.setTexts(texts);
                            mPieChart.setMaxNum(datas.length);
                            mPieChart.setCenterText(alarmEvent.totalNumber+",环保报警");
                            mPieChart.invalidate();
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        linearLayout.setVisibility(View.GONE);
                    }

                    Log.e("dafda", "onSuccess: ");
                }else{
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                linearLayout.setVisibility(View.GONE);
                Util.login(String.valueOf(code),getActivity());
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
        initData(type);
        mPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PieChartEPDataActivity.class);
                intent.putExtra("type", typeData);
                startActivity(intent);
            }
        });
    }

}
