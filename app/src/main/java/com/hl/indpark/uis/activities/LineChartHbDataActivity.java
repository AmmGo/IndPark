package com.hl.indpark.uis.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.AnimationUtils;
import com.hl.indpark.utils.ChartUtils;
import com.hl.indpark.utils.ShowUtils;

import net.arvin.baselib.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineChartHbDataActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;

    private TextView tvDate2;
    private ImageView ivDate2;
    private LineChart chart2;

    private TextView tvDate3;
    private ImageView ivDate3;
    private LineChart chart3;

    private static final String[] dates = new String[]{"今日", "本周", "本月"};
    private List<String> dateList = Arrays.asList(dates);
    //        map.put("w21003", "氨氮");
//        map.put("w21011", "总磷");
//        map.put("w01018", "化学需氧量(COD)");
    public static String[] water = new String[]{""};
    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_hb_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);

        tvDate2 = (TextView) findViewById(R.id.tv_date2);
        ivDate2 = (ImageView) findViewById(R.id.iv_date2);
        chart2 = (LineChart) findViewById(R.id.chart2);

        tvDate3 = (TextView) findViewById(R.id.tv_date3);
        ivDate3 = (ImageView) findViewById(R.id.iv_date3);
        chart3 = (LineChart) findViewById(R.id.chart3);

        ivDate.setColorFilter(Color.WHITE);
        tvDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);

        ivDate2.setColorFilter(Color.WHITE);
        tvDate2.setOnClickListener(this);
        ivDate2.setOnClickListener(this);

        ivDate3.setColorFilter(Color.WHITE);
        tvDate3.setOnClickListener(this);
        ivDate3.setOnClickListener(this);

        ChartUtils.initChart(chart);
        ChartUtils.notifyDataSetChanged(chart, getData(), ChartUtils.dayValue);

        ChartUtils.initChart(chart2);
        ChartUtils.notifyDataSetChanged(chart2, getData(), ChartUtils.dayValue);

        ChartUtils.initChart(chart3);
        ChartUtils.notifyDataSetChanged(chart3, getData(), ChartUtils.dayValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
            case R.id.iv_date:
                String data = tvDate.getText().toString();

                if (!ShowUtils.isPopupWindowShowing()) {
                    AnimationUtils.startModeSelectAnimation(ivDate, true);
                    ShowUtils.showPopupWindow(this, tvDate, 90, 166, dateList,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    ShowUtils.updatePopupWindow(position);
                                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                                    ShowUtils.popupWindowDismiss();
                                    tvDate.setText(dateList.get(position));
                                    // 更新图表
                                    ChartUtils.notifyDataSetChanged(chart, getData(), position);
                                }
                            });
                } else {
                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                    ShowUtils.popupWindowDismiss();
                }

                if (dateList.get(0).equals(data)) {
                    ShowUtils.updatePopupWindow(0);
                } else if (dateList.get(1).equals(data)) {
                    ShowUtils.updatePopupWindow(1);
                } else if (dateList.get(2).equals(data)) {
                    ShowUtils.updatePopupWindow(2);
                }
                break;
            case R.id.tv_date2:
            case R.id.iv_date2:
                String data2 = tvDate2.getText().toString();

                if (!ShowUtils.isPopupWindowShowing()) {
                    AnimationUtils.startModeSelectAnimation(ivDate2, true);
                    ShowUtils.showPopupWindow(this, tvDate2, 90, 166, dateList,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    ShowUtils.updatePopupWindow(position);
                                    AnimationUtils.startModeSelectAnimation(ivDate2, false);
                                    ShowUtils.popupWindowDismiss();
                                    tvDate2.setText(dateList.get(position));
                                    // 更新图表
                                    ChartUtils.notifyDataSetChanged(chart2, getData(), position);
                                }
                            });
                } else {
                    AnimationUtils.startModeSelectAnimation(ivDate2, false);
                    ShowUtils.popupWindowDismiss();
                }

                if (dateList.get(0).equals(data2)) {
                    ShowUtils.updatePopupWindow(0);
                } else if (dateList.get(1).equals(data2)) {
                    ShowUtils.updatePopupWindow(1);
                } else if (dateList.get(2).equals(data2)) {
                    ShowUtils.updatePopupWindow(2);
                }
                break;
            case R.id.tv_date3:
            case R.id.iv_date3:
                String data3 = tvDate3.getText().toString();

                if (!ShowUtils.isPopupWindowShowing()) {
                    AnimationUtils.startModeSelectAnimation(ivDate3, true);
                    ShowUtils.showPopupWindow(this, tvDate3, 90, 166, dateList,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    ShowUtils.updatePopupWindow(position);
                                    AnimationUtils.startModeSelectAnimation(ivDate3, false);
                                    ShowUtils.popupWindowDismiss();
                                    tvDate3.setText(dateList.get(position));
                                    // 更新图表
                                    ChartUtils.notifyDataSetChanged(chart3, getData(), position);
                                }
                            });
                } else {
                    AnimationUtils.startModeSelectAnimation(ivDate3, false);
                    ShowUtils.popupWindowDismiss();
                }

                if (dateList.get(0).equals(data3)) {
                    ShowUtils.updatePopupWindow(0);
                } else if (dateList.get(1).equals(data3)) {
                    ShowUtils.updatePopupWindow(1);
                } else if (dateList.get(2).equals(data3)) {
                    ShowUtils.updatePopupWindow(2);
                }
                break;

            default:
                break;
        }
    }

    private List<Entry> getData() {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 15));
        values.add(new Entry(1, 15));
        values.add(new Entry(2, 15));
        values.add(new Entry(3, 20));
        values.add(new Entry(4, 25));
        values.add(new Entry(5, 20));
        values.add(new Entry(6, 20));
        return values;
    }

    public void getHbChart1(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();

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

    public void getHbChart2(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();

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

    public void getHbChart3(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();

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