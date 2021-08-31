package com.hl.indpark.uis.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartThreePc;
import com.hl.indpark.entities.events.LineChartsHb;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.DensityUtils;
import com.hl.indpark.utils.LineChatUtils;
import com.hl.indpark.utils.TimeUtils;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    private LineChartThreePc lineChartThree1;
    private LineChartThreePc lineChartThree7;
    private LineChartThreePc lineChartThree30;
    private TextView textView;
    private String value;
    @BindView(R.id.tv_ad)
    TextView tv_ad;
    @BindView(R.id.tv_zd)
    TextView tv_zd;
    @BindView(R.id.tv_cod)
    TextView tv_cod;

    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_hb;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        value = "";
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

//        entId = "640500000076";
//        pointId = "2";
//        startDate1 = "2021-06-01 00:00:00";
//        startDate7 = "2021-06-01 00:00:00";
//        startDate30 = "2021-06-01 00:00:00";
//        endDate1 = "2021-09-01 00:00:00";
        textView = findViewById(R.id.tv_sssj);
        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
        if (type.equals("1")) {
            tv_ad.setText(LineChatUtils.AD);
            tv_zd.setText(LineChatUtils.ZD);
            tv_cod.setText(LineChatUtils.COD);
        } else {
            tv_ad.setText(LineChatUtils.YC);
            tv_zd.setText(LineChatUtils.EYHL);
            tv_cod.setText(LineChatUtils.DYHW);
        }

        mLineChart1.setNoDataText("暂无数据");
        mLineChart1.setNoDataTextColor(ContextCompat.getColor(this, R.color.third_text));
        Paint paint = mLineChart1.getPaint(Chart.PAINT_INFO);
        paint.setTextSize(DensityUtils.dp2px(this, 14));

        mLineChart7.setNoDataText("暂无数据");
        mLineChart7.setNoDataTextColor(ContextCompat.getColor(this, R.color.third_text));
        Paint paint7 = mLineChart7.getPaint(Chart.PAINT_INFO);
        paint7.setTextSize(DensityUtils.dp2px(this, 14));
        mLineChart30.setNoDataText("暂无数据");
        mLineChart30.setNoDataTextColor(ContextCompat.getColor(this, R.color.third_text));
        Paint paint30 = mLineChart30.getPaint(Chart.PAINT_INFO);
        paint30.setTextSize(DensityUtils.dp2px(this, 14));


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
//                        if (chartsHbs.get(0).value.get(0).avgStrength != null && !chartsHbs.get(0).value.get(0).avgStrength.equals("")) {
                            update(chartsHbs);
                            lineChartThree1 = new LineChartThreePc(mLineChart1, LineChartHbPcActivity.this);
//                        }

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
                chartsHbs = response.getData();
                try {
                    if (chartsHbs.size() > 0) {
//                        if (chartsHbs.get(0).value.get(0).avgStrength != null && !chartsHbs.get(0).value.get(0).avgStrength.equals("")) {
                            update(chartsHbs);
                            lineChartThree7 = new LineChartThreePc(mLineChart7, LineChartHbPcActivity.this);
//                        }
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
//                        if (chartsHbs.get(0).value.get(0).avgStrength != null && !chartsHbs.get(0).value.get(0).avgStrength.equals("")) {
                            update(chartsHbs);
                            lineChartThree30 = new LineChartThreePc(mLineChart30, LineChartHbPcActivity.this);
//                        }
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
    public static Map<Integer, String> mapTime = new HashMap<>();
    public static List<LineChartsHb.ValueBean> listZl = new ArrayList<>();
    public static List<LineChartsHb.ValueBean> listAd = new ArrayList<>();
    public static List<LineChartsHb.ValueBean> listCod = new ArrayList<>();
    public static String KEY_NAME1 = "";
    public static String KEY_NAME2 = "";
    public static String KEY_NAME3 = "";

    public void update(List<LineChartsHb> chartsHbs) {
        listAd = new ArrayList<>();
        listZl = new ArrayList<>();
        listCod = new ArrayList<>();
        mapZlTime = new HashMap<>();
        mapAdTime = new HashMap<>();
        mapCodTime = new HashMap<>();
        mapCod = new HashMap<>();
        mapZl = new HashMap<>();
        mapAd = new HashMap<>();
        mapTime = new HashMap<>();
        for (int i = 0; i < chartsHbs.size(); i++) {
            if (chartsHbs.get(i).key.equals(LineChatUtils.AD)) {
                KEY_NAME1 = LineChatUtils.AD;
                listAd = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals(LineChatUtils.ZD)) {
                KEY_NAME2 = LineChatUtils.ZD;
                listZl = chartsHbs.get(i).value;
            } else if (chartsHbs.get(i).key.equals(LineChatUtils.COD)) {
                KEY_NAME3 = LineChatUtils.COD;
                listCod = chartsHbs.get(i).value;
            }else if (chartsHbs.get(i).key.equals(LineChatUtils.YC)){
                KEY_NAME1 = LineChatUtils.YC;
                listAd = chartsHbs.get(i).value;
            }else if (chartsHbs.get(i).key.equals(LineChatUtils.EYHL)){
                KEY_NAME2 = LineChatUtils.EYHL;
                listZl = chartsHbs.get(i).value;
            }else if (chartsHbs.get(i).key.equals(LineChatUtils.DYHW)){
                KEY_NAME3 = LineChatUtils.DYHW;
                listCod = chartsHbs.get(i).value;
            }
        }
        mapTime = new HashMap<>();
        try {
            if (listZl.size() > 0) {
                for (int i = 0; i < listZl.size(); i++) {
                    value = listZl.get(i).avgStrength;
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapZl.put(i, Float.valueOf(value));
                    String timeData = listZl.get(i).monitorTime;
                    String time = timeData.contains(".") ? timeData.split("/.")[0] : timeData;
                    mapZlTime.put(i, time);
                }
            }
            if (listAd.size() > 0) {
                for (int i = 0; i < listAd.size(); i++) {
                    value = listAd.get(i).avgStrength;
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapAd.put(i, Float.valueOf(value));
                    String timeData = listAd.get(i).monitorTime;
                    String time = timeData.contains(".") ? timeData.split("/.")[0] : timeData;
                    mapAdTime.put(i, time);
                }
            }
            if (listCod.size() > 0) {
                for (int i = 0; i < listCod.size(); i++) {
                    value = listCod.get(i).avgStrength;
                    if (value == null || value.equals("")) {
                        value = "0";
                    }
                    mapCod.put(i, Float.valueOf(value));
                    String timeData = listCod.get(i).monitorTime;
                    String time = timeData.contains(".") ? timeData.split("/.")[0] : timeData;
                    mapCodTime.put(i, time);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i=mapZlTime.size();
        int j=mapAdTime.size();
        int k=mapCodTime.size();
        int max=(i>j? i:j)>k? (i>j? i:j):k;
        if (max==mapZlTime.size()){
            mapTime = mapZlTime;
        }else if (max==mapAdTime.size()){
            mapTime = mapAdTime;
        }else if (max ==mapCodTime.size()){
            mapTime = mapCodTime;
        }
    }

}