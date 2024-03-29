package com.hl.indpark.uis.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartSH1DataActivity;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseFragment;

import org.json.JSONObject;

import butterknife.OnCheckedChanged;


/**
 * Created by yjl on 2021/3/9 13:19
 * Function：
 * Desc：报警分析---危险源
 */
public class TabAHSFragment extends BaseFragment {
    private WebView mPieChart;
    private JSONObject jsonObject;
    private String type = "2";
    private int typeData = 2;
    private LinearLayout linearLayout;
    private HSAlarmEvent alarmEvent;
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
        Intent intent = new Intent(getActivity(), PieChartSH1DataActivity.class);
        intent.putExtra("timeType",typeData);
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
    @OnCheckedChanged({R.id.rg_month, R.id.rg_quarter, R.id.rg_year})
    public void OnCheckedChangeListener(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.rg_month:
                if (ischanged) {
                    type = "2";
                    typeData = Util.wxyMonthly;
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
        return R.layout.fragment_tab_ahs;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPieChart = root.findViewById(R.id.chart_sep);
        linearLayout = root.findViewById(R.id.ll_gone);
        initData(type);
    }

    private void initData(String ty) {
        ArticlesRepo.getHSAlarm(ty).observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
                try {
                    if (response != null && response.getData() != null&&response.getData().name!=null) {
                        alarmEvent = response.getData();
                        if (!response.getData().name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                        } else if (response.getData().name.equals("0")){
                            setWebview();
                            linearLayout.setVisibility(View.VISIBLE);
                        }else{
                            linearLayout.setVisibility(View.GONE);
                        }
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
                Util.login(String.valueOf(code),getActivity());
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                linearLayout.setVisibility(View.GONE);

            }
        });
    }

}
