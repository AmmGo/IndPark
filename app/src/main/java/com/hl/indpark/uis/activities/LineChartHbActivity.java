package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartDouble;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;

import net.arvin.baselib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LineChartHbActivity extends BaseActivity {
    private LineChart mLineChart1;
    private LineChart mLineChart7;
    private LineChart mLineChart30;
    private LineChartDouble lineChartDouble1;
    private LineChartDouble lineChartDouble7;
    private LineChartDouble lineChartDouble30;
    private List<LineChartsHb> lineChart1;
    private List<LineChartsHb> lineChart7;
    private List<LineChartsHb> lineChart30;
    int fx1 = 0;
    int fx7 = 1;
    int fx30 = 2;
    int DDType = 2;
    int HHType = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_hb;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
        String entId = "640500000072";
        String pointId = "2";
        String timeType = "30";
        getHb1(entId, pointId, "1");
//        getHb1(entId, pointId, "7");
//        getHb1(entId, pointId, "30");
    }

    public void getHb1(String entId, String pkId, String dateType) {
        ArticlesRepo.getLineChartHb(entId,pkId,dateType).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();
                Log.e("修改状态成功", "onSuccess: ");
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