package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartThreePc;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.TimeUtils;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineChartHbPcActivity extends BaseActivity {
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
    private LineChartThreePc lineChartThree;
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
        String startDate1 = TimeUtils.dateToString(TimeUtils.getTodayZeroPointTimestamps());
        String startDate7 = TimeUtils.dateToString(TimeUtils.NumberOfDaysStartUnixTime(7));
        String startDate30 = TimeUtils.dateToString(TimeUtils.NumberOfDaysStartUnixTime(30));
        String endDate1 = TimeUtils.dateToString(System.currentTimeMillis());


        textView = findViewById(R.id.tv_sssj);
        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
        if (timeType.equals("1")) {
            getHb1(entId, pointId, startDate1, endDate1);
            getHb7(entId, pointId, startDate7, endDate1);
            getHb30(entId, pointId, startDate30, endDate1);
            mLineChart1.setVisibility(View.VISIBLE);
            mLineChart7.setVisibility(View.VISIBLE);
            mLineChart30.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            getHb7(entId, pointId, startDate7, endDate1);
            getHb30(entId, pointId, startDate30, endDate1);
            mLineChart1.setVisibility(View.GONE);
            mLineChart7.setVisibility(View.VISIBLE);
            mLineChart30.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    public void getHb1(String enterpriseId, String pointId, String startDate, String endDate) {

        ArticlesRepo.getLineChartHbPc(enterpriseId, pointId, startDate, endDate).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                chartsHbs = response.getData();
                try {
                    if (chartsHbs.size() > 0) {
                        update(chartsHbs);
                        lineChartThree = new LineChartThreePc(mLineChart1, LineChartHbPcActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

        ArticlesRepo.getLineChartHbPc(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                List<LineChartsHb> chartsHbs = new ArrayList<>();
                try {
                    if (chartsHbs.size() > 0) {
                        chartsHbs = response.getData();
                        update7(chartsHbs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

    public void getHb30(String enterpriseId, String pointId, String timeType, String type) {

        ArticlesRepo.getLineChartHbPc(enterpriseId, pointId, timeType, type).observe(this, new ApiObserver<List<LineChartsHb>>() {
            @Override
            public void onSuccess(Response<List<LineChartsHb>> response) {
                try {
                    List<LineChartsHb> chartsHbs = new ArrayList<>();
                    chartsHbs = response.getData();
                    if (chartsHbs.size() > 0) {
                        update(chartsHbs);
                        lineChartThree = new LineChartThreePc(mLineChart30, LineChartHbPcActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
    public static List<LineChartsHb.ValueBean> listZl = new ArrayList<>();
    public static List<LineChartsHb.ValueBean> listAd = new ArrayList<>();
    public static List<LineChartsHb.ValueBean> listCod = new ArrayList<>();

    //        map.put("w21003", "氨氮");
//        map.put("w21011", "总磷");
//        map.put("w01018", "化学需氧量(COD)");
    public void update(List<LineChartsHb> chartsHbs) {
        for (int i = 0; i < chartsHbs.size(); i++) {
            if (chartsHbs.get(i).key.equals("氨氮")) {
                listAd = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals("总氮")) {
                listZl = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals("化学需氧量(COD)")) {
                listCod = chartsHbs.get(i).value;
            }
        }
        try {
            for (int i = 0; i < listZl.size(); i++) {
                String value = listZl.get(i).avgStrength == null ? "0" : listZl.get(i).avgStrength;
                mapZl.put(i, Float.valueOf(value));
                mapZlTime.put(i, listZl.get(i).monitorTime);
            }
            for (int i = 0; i < listAd.size(); i++) {
                String value = listAd.get(i).avgStrength == null ? "0" : listAd.get(i).avgStrength;
                mapAd.put(i, Float.valueOf(value));
                mapAdTime.put(i, listAd.get(i).monitorTime);
            }
            for (int i = 0; i < listCod.size(); i++) {
                String value = listCod.get(i).avgStrength == null ? "0" : listCod.get(i).avgStrength;
                mapCod.put(i, Float.valueOf(value));
                mapCodTime.put(i, listCod.get(i).monitorTime);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void update7(List<LineChartsHb> chartsHbs) {
        for (int i = 0; i < chartsHbs.size(); i++) {
            if (chartsHbs.get(i).key.equals("氨氮")) {
                listAd = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals("总氮")) {
                listZl = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals("化学需氧量(COD)")) {
                listCod = chartsHbs.get(i).value;
            }
        }
        try {
            for (int i = 0; i < listZl.size(); i++) {
                String value = listZl.get(i).avgStrength == null ? "0" : listZl.get(i).avgStrength;
                mapZl.put(i, Float.valueOf(value));
                mapZlTime.put(i, listZl.get(i).monitorTime);
            }
            for (int i = 0; i < listAd.size(); i++) {
                String value = listAd.get(i).avgStrength == null ? "0" : listAd.get(i).avgStrength;
                mapAd.put(i, Float.valueOf(value));
                mapAdTime.put(i, listAd.get(i).monitorTime);
            }
            for (int i = 0; i < listCod.size(); i++) {
                String value = listCod.get(i).avgStrength == null ? "0" : listCod.get(i).avgStrength;
                mapCod.put(i, Float.valueOf(value));
                mapCodTime.put(i, listCod.get(i).monitorTime);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        lineChartThree = new LineChartThreePc(mLineChart7, LineChartHbPcActivity.this);
    }
}