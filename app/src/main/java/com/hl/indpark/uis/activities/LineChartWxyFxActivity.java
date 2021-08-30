package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartDouble;
import com.hl.indpark.entities.events.LineChartWxy;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
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
    public void getLineChart7(String labelId) {
        try {
            ArticlesRepo.getLineChartWxy(labelId, fx7).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart7 = new ArrayList<>();
                    lineChart7 = response.getData();
                    try {
                        if (lineChart7.size()>0){

                            lineChartDouble7 = new LineChartDouble(mLineChart7,lineChart7,LineChartWxyFxActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    try {
                        if (lineChart30.size()>0){

                            lineChartDouble30 = new LineChartDouble(mLineChart30,lineChart30,LineChartWxyFxActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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