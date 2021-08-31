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
import com.hl.indpark.entities.events.LineChartDouble;
import com.hl.indpark.entities.events.LineChartWxy;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.DensityUtils;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LineChartWxyTjActivity extends BaseActivity {
    public LineChart mLineChart1;
    public LineChart mLineChart7;
    public LineChart mLineChart30;
    public LineChartDouble lineChartDouble1;
    public LineChartDouble lineChartDouble7;
    public LineChartDouble lineChartDouble30;
    public static List<LineChartWxy> lineChart1;
    public static List<LineChartWxy> lineChart7;
    public static List<LineChartWxy> lineChart30;
    int fx7 = 1;
    int fx1 = 0;
    int fx30 = 2;
    @BindView(R.id.tv_dw)
    TextView tv_dw;
    @BindView(R.id.tv_pk_name)
    TextView tv_pk_name;
    @BindView(R.id.tv_dw_name)
    TextView tv_dw_name;
    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_wxy_tj;
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
        String dw_str = getIntent().getStringExtra("dw_str");
        String tv_pk_nameStr = getIntent().getStringExtra("tv_pk_name");
        String dw_name = getIntent().getStringExtra("dw_name");
        tv_pk_name.setText(tv_pk_nameStr);
        tv_dw.setText(dw_str);
        tv_dw_name.setText(dw_name);
        getLineChart1(labelId);
        getLineChart7(labelId);
        getLineChart30(labelId);
        mLineChart1 = findViewById(R.id.chart_1);
        mLineChart7 = findViewById(R.id.chart_7);
        mLineChart30 = findViewById(R.id.chart_30);
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
    }

    public void getLineChart1(String labelId) {
        try {
            ArticlesRepo.getLineChartWxyTj(labelId, fx1).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart1 = new ArrayList<>();
                    lineChart1 = response.getData();
                    try {
                        if (lineChart1.size() > 0) {
                            lineChartDouble1 = new LineChartDouble(mLineChart1, lineChart1, LineChartWxyTjActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyTjActivity.this);
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


    public void getLineChart7(String labelId) {
        try {
            ArticlesRepo.getLineChartWxyTj(labelId, fx7).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart7 = new ArrayList<>();
                    lineChart7 = response.getData();
                    try {
                        if (lineChart7.size() > 0) {
                            lineChartDouble7 = new LineChartDouble(mLineChart7, lineChart7, LineChartWxyTjActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyTjActivity.this);
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
            ArticlesRepo.getLineChartWxyTj(labelId, fx30).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    lineChart30 = new ArrayList<>();
                    lineChart30 = response.getData();
                    try {
                        if (lineChart30.size() > 0) {

                            lineChartDouble30 = new LineChartDouble(mLineChart30, lineChart30, LineChartWxyTjActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyTjActivity.this);
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