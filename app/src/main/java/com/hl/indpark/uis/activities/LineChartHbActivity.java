package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartThree;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineChartHbActivity extends BaseActivity {
    private LineChart mLineChart1;
    private LineChart mLineChart7;
    private LineChart mLineChart30;

    private List<LineChartsHb> lineChart1;
    private List<LineChartsHb> lineChart7;
    private List<LineChartsHb> lineChart30;
    int fx1 = 0;
    int fx7 = 1;
    int fx30 = 2;
    int DDType = 2;
    int HHType = 1;
    private String pointIdString;
    private LineChartThree lineChartThree;
    private TextView textView;

    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_hb;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mapCod = new HashMap<>();
        mapZl = new HashMap<>();
        mapAd = new HashMap<>();
        mapCodTime = new HashMap<>();
        mapZlTime = new HashMap<>();
        mapAdTime = new HashMap<>();
        listZl = new ArrayList<>();
        listAd = new ArrayList<>();
        listCod = new ArrayList<>();
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("环保数据折线图");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String entId = getIntent().getStringExtra("entId");
        String pointId = getIntent().getStringExtra("pointId");
        String type = getIntent().getStringExtra("type");
        pointIdString = pointId.equals("1") ? "水排口" : "气排口";
        String timeType = getIntent().getStringExtra("isTime");

        entId = "640500000072";
        pointId = "2";


        textView = findViewById(R.id.tv_sssj);
        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
        if (timeType.equals("1")) {
            getHb1(entId, pointId, "1", type);
            getHb7(entId, pointId, "7", type);
            getHb30(entId, pointId, "30", type);
            mLineChart1.setVisibility(View.VISIBLE);
            mLineChart7.setVisibility(View.VISIBLE);
            mLineChart30.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            getHb7(entId, pointId, "7", type);
            getHb30(entId, pointId, "30", type);
            mLineChart1.setVisibility(View.GONE);
            mLineChart7.setVisibility(View.VISIBLE);
            mLineChart30.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
//        initData();
//        lineChartThree = new LineChartThree(mLineChart1, LineChartHbActivity.this);
    }

    public void getHb1(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();
                update(chartsHbs);
                lineChartThree = new LineChartThree(mLineChart1, LineChartHbActivity.this);

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

    public void getHb7(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();
                update(chartsHbs);
                lineChartThree = new LineChartThree(mLineChart7, LineChartHbActivity.this);

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

    public void initData() {
        for (int i = 0; i < 30; i++) {
            mapZl.put(i, Float.valueOf(String.valueOf(i * (1 + 0.5))));
            mapAd.put(i, Float.valueOf(String.valueOf(i * (1 + 0.7))));
            mapCod.put(i, Float.valueOf(String.valueOf(i * (1 + 0.8))));
        }
    }

    public void getHb30(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHb(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();
                update(chartsHbs);
                lineChartThree = new LineChartThree(mLineChart30, LineChartHbActivity.this);

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

    public static Map<Integer, Float> mapCod = new HashMap<>();
    public static Map<Integer, Float> mapZl = new HashMap<>();
    public static Map<Integer, Float> mapAd = new HashMap<>();
    public static Map<Integer, String> mapCodTime = new HashMap<>();
    public static Map<Integer, String> mapZlTime = new HashMap<>();
    public static Map<Integer, String> mapAdTime = new HashMap<>();
    public static List<LineChartsHb> listZl = new ArrayList<>();
    public static List<LineChartsHb> listAd = new ArrayList<>();
    public static List<LineChartsHb> listCod = new ArrayList<>();

    //        map.put("w21003", "氨氮");
//        map.put("w21011", "总磷");
//        map.put("w01018", "化学需氧量(COD)");
    public void update(List<LineChartsHb> chartsHbs) {
        try {
            for (int i = 0; i < chartsHbs.size(); i++) {
                String pkName = chartsHbs.get(i).pollutantCode;
                if (pkName != null) {
                    if (pkName.equals("w21011")) {
                        listZl.add(chartsHbs.get(i));
                    } else if (pkName.equals("w21003")) {
                        listAd.add(chartsHbs.get(i));
                    } else if (pkName.equals("w01018")) {
                        listCod.add(chartsHbs.get(i));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < listZl.size(); i++) {
                String value = listZl.get(i).realTimeData == null ? "0" : listZl.get(i).realTimeData;
                mapZl.put(i, Float.valueOf(value));
                mapZlTime.put(i, listZl.get(i).monitorTime);
            }
            for (int i = 0; i < listAd.size(); i++) {
                String value = listAd.get(i).realTimeData == null ? "0" : listAd.get(i).realTimeData;
                mapAd.put(i, Float.valueOf(value));
                mapAdTime.put(i, listAd.get(i).monitorTime);
            }
            for (int i = 0; i < listCod.size(); i++) {
                String value = listCod.get(i).realTimeData == null ? "0" : listCod.get(i).realTimeData;
                mapCod.put(i, Float.valueOf(value));
                mapCodTime.put(i, listCod.get(i).monitorTime);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}