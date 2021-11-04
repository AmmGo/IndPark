package com.hl.indpark.uis.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.entities.events.WxyTjEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartSHDataActivity;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yjl on 2021/3/9 13:19
 * Function：
 * Desc：报警统计---危险源
 */
public class TabSHSFragment extends BaseFragment {
    private WebView mPieChart;
    private JSONObject jsonObject;
//    private PieChartView mPieChart;
    private LinearLayout linearLayout;
    private HSAlarmEvent alarmEvent = new HSAlarmEvent();
    private List<HSAlarmEvent.ValueBean> valueBeanList;

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_shs;
    }
    @SuppressLint("JavascriptInterface")
    private void setWebview() {
        WebSettings webSettings = mPieChart.getSettings();
        //与js交互必须设置
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mPieChart.setHorizontalScrollBarEnabled(false);//水平不显示
        mPieChart.setVerticalScrollBarEnabled(false); //垂直不显示
        mPieChart.loadUrl("file:///android_asset/chart/src/pie.html");
        mPieChart.addJavascriptInterface(this, "justJump");
        mPieChart.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mPieChart.loadUrl("javascript:loadChart('" + getJson() + "')");
                super.onPageFinished(view, url);
            }
        });
    }
    @JavascriptInterface
    public void jump() {
        Intent intent = new Intent(getActivity(), PieChartSHDataActivity.class);
        intent.putExtra("timeType", Util.wxyDay);
        startActivity(intent);
    }
    private String getJson() {
        String result = "";
        try {
            jsonObject = new JSONObject();
            jsonObject.put("ggb", getPieJson(1));
            jsonObject.put("gb", getPieJson(2));
            jsonObject.put("ddb", getPieJson(4));
            jsonObject.put("db", getPieJson(3));
            jsonObject.put("sum", alarmEvent.name);
            result = jsonObject.toString();
        } catch (Exception e) {

        }
        return result;
    }
    public int getPieJson(int type){
        int num =0;
        for (int i = 0 ;i<alarmEvent.value.size();i++){
            if (alarmEvent.value.get(i).type==type){
                num = alarmEvent.value.get(i).num;
            }
        }
        return  num;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        mPieChart = root.findViewById(R.id.chart_sep);
        linearLayout = root.findViewById(R.id.ll_gone);
        initData1();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void initData1() {
        ArticlesRepo.getWxyTjEvent().observe(this, new ApiObserver<List<WxyTjEvent>>() {
            @Override
            public void onSuccess(Response<List<WxyTjEvent>> response) {
                if (response.getData()!=null&&response.getData().size()>0){
                    int count = 0 ;
                    List<HSAlarmEvent.ValueBean> one = new ArrayList<>();
                    alarmEvent.value = new HSAlarmEvent().value;
                    for (int i = 0 ;i<response.getData().size();i++){
                        if (response.getData().get(i).key!=0){
                            count +=response.getData().get(i).value;
                            HSAlarmEvent.ValueBean valueBean = new HSAlarmEvent.ValueBean();
                            valueBean.type = response.getData().get(i).key;
                            valueBean.num = response.getData().get(i).value;
                            one.add(valueBean);
                        }

                    }
                    alarmEvent.value = one;
                    alarmEvent.name = String.valueOf(count);
                }
                try {
                    if (response != null && response.getData() != null&&alarmEvent.name!=null) {
                        if (!alarmEvent.name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                            Log.e("危险源统计", "onSuccess: ");
                        }else if (alarmEvent.name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.GONE);
                        }
                        setWebview();
    //                    sepPieChart(alarmEvent);
                    }else{
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
                Util.login(String.valueOf(code),getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                linearLayout.setVisibility(View.GONE);

            }
        });
    }
    private void initData() {
        ArticlesRepo.getHSAlarm("1").observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
                try {
                    if (response != null && response.getData() != null&&response.getData().name!=null) {
                        alarmEvent = response.getData();
                        if (!response.getData().name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                            Log.e("危险源统计", "onSuccess: ");
                        }else if (response.getData().name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.GONE);
                        }
                        setWebview();
                        //                    sepPieChart(alarmEvent);
                    }else{
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
                Util.login(String.valueOf(code),getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                linearLayout.setVisibility(View.GONE);

            }
        });
    }
}
