package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartDouble;
import com.hl.indpark.entities.events.LineChartWxy;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.LineChatUtils;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class LineChartWxyFxActivity extends BaseActivity {
    private LineChart mLineChart1;
    private LineChart mLineChart7;
    private LineChart mLineChart30;
    private LineChartDouble lineChartDouble1;
    private LineChartDouble lineChartDouble7;
    private LineChartDouble lineChartDouble30;
    private List<LineChartWxy> lineChart1;
    private List<LineChartWxy> lineChart7;
    private List<LineChartWxy> lineChart30;
    int fx7 = 1;
    int fx30 = 2;
    int DDType = 2;

    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_wxy_fx;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("危险源数据折线图");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String labelId = getIntent().getStringExtra("labelId");
        getLineChart7(labelId);
        getLineChart30(labelId);
        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
    }
    public String xValuesProcess(List<LineChartWxy> list, float keyFloat, int dateType) {
        int date = (int) keyFloat;
        LineChatUtils lu = new LineChatUtils();
        return lu.StringX(list.get(date).key, dateType);

    }
    public void getLineChart7(String labelId) {
        try {
            ArticlesRepo.getLineChartWxy(labelId, fx7).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart7 = new ArrayList<>();
                    lineChart7 = response.getData();

                    lineChartDouble7 = new LineChartDouble(mLineChart7);
                    LineData lineData = new LineData(lineChartDouble7.getDataSet(lineChart7));
                    XAxis xAxis = mLineChart7.getXAxis();
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xValuesProcess(lineChart7, value, DDType);
                        }
                    });
                    // 设置数据
                    mLineChart7.setData(lineData);
                    mLineChart7.invalidate();
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyFxActivity.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLineChart30(String labelId) {
        try {
            ArticlesRepo.getLineChartWxy(labelId, fx30).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart30 = new ArrayList<>();
                    lineChart30 = response.getData();

                    lineChartDouble30 = new LineChartDouble(mLineChart30);
                    LineData lineData = new LineData(lineChartDouble30.getDataSet(lineChart30));
                    XAxis xAxis = mLineChart30.getXAxis();
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xValuesProcess(lineChart30, value, DDType);
                        }
                    });
                    // 设置数据
                    mLineChart30.setData(lineData);
                    mLineChart30.invalidate();
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyFxActivity.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}