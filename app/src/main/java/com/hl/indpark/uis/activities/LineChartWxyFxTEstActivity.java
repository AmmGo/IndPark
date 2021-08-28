package com.hl.indpark.uis.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.LineChartWxy;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LineChartWxyFxTEstActivity extends BaseActivity {
    private LineChart mLineChart;
    List<Entry> list = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_line_chart_wxy_fx;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLineChart = findViewById(R.id.chart_1);
        // Entry中两个数字对应的分别是 X轴 Y轴
        list.add(new Entry(0, 7));
        list.add(new Entry(1, 10));
        list.add(new Entry(2, 12));
        list.add(new Entry(3, 6));
        list.add(new Entry(4, 3));
        // list是这条线的数据 "语文"是对这条线的描述（也就是图例上的文字）
        LineDataSet lineDataSet = new LineDataSet(list, "数值");
        LineData lineData = new LineData(lineDataSet);
        // 有多条数据则使用lineData.addDataSet()方法 参数是DataSet
        // lineData.addDataSet(lineDataSet);
        mLineChart.setData(lineData);

        // TODO 美化
        // 折线图背景
        mLineChart.setBackgroundColor(0xFFFFFF); // 折线图背景颜色
        mLineChart.getXAxis().setDrawGridLines(false); // 是否绘制X轴上的网格线
        mLineChart.getAxisLeft().setDrawGridLines(false); // 是否绘制Y轴上的网格线

        // 对于右下角一串字母的操作
        mLineChart.getDescription().setEnabled(true); // 是否显示右下角描述
        mLineChart.getDescription().setText("近七天数据"); // 修改右下角字母的显示
        mLineChart.getDescription().setTextSize(16); // 字体大小
        mLineChart.getDescription().setTextColor(Color.RED); // 字体颜色

        // 图例
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(true); // 是否显示图例
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); // 图例的位置

        // X轴
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setAxisLineColor(getResources().getColor(R.color.secondary_text)); // X轴颜色
        xAxis.setAxisLineWidth(1); // X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X轴所在位置 默认在上面
        xAxis.setValueFormatter(new IAxisValueFormatter() { // X轴自定义坐标
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value == 0) {
                    return "第一天";
                }
                if (value == 1) {
                    return "第二天";
                }
                if (value == 2) {
                    return "第三天";
                }
                if (value == 3) {
                    return "第四天";
                }
                if (value == 4) {
                    return "第五天";
                }
                return "";
            }
        });
        xAxis.setAxisMaximum(5); // X轴最大数值
        xAxis.setAxisMinimum(0); // X轴最小数值
        xAxis.setLabelCount(5, false); // X轴坐标标签个数 第二个参数一般为 false ; true表示强制设置标签数

        // Y轴
        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisLineColor(getResources().getColor(R.color.secondary_text)); // Y轴颜色
        yAxis.setAxisLineWidth(1); // Y轴粗细
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                for (int i = 0; i < 16; i++) {
                    if (i == value) {
                        return i + "℃";
                    }
                }
                return "";
            }
        });

        yAxis.setAxisMaximum(15); // Y轴最大数值
        yAxis.setAxisMinimum(0); // Y轴最小数值
        yAxis.setLabelCount(15, false); // Y轴坐标标签个数
        mLineChart.getAxisRight().setEnabled(false); // 是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）

        // 折线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置折线样式
        lineDataSet.setColor(getResources().getColor(R.color.primary)); // 设置折线颜色
        lineDataSet.setLineWidth(2); // 设置折线粗细
        lineDataSet.setDrawCircleHole(true); // 是否画折线点上的空心圆 false表示直接画成实心圆
        lineDataSet.setCircleHoleRadius(2); // 空心圆内圈半径
        lineDataSet.setCircleColorHole(Color.WHITE); // 空心圆内圈颜色
        //lineDataSet.setCircleColor(Color.BLACK); // 空心圆外圈颜色
        lineDataSet.setCircleRadius(4); // 空心圆外圈半径
        // 定义折线上的数据显示 实现添加单位
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (entry.getY() == value) {
                    return value + "℃";
                }
                return "";
            }
        });

        // 数据更新
//        mLineChart.notifyDataSetChanged();
//        mLineChart.invalidate();

        // 动画 (如果使用动画,则可以省去数据更新步骤)
        mLineChart.animateY(3000); // Y轴方向动画,由下向上出现折线
//        mLineChart.animateX(2000); // X轴方向动画,从左向右逐段显示折线
//        mLineChart.animateXY(2000,2000); // XY两轴混合动画

        // X轴所在位置 默认在上面
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        // 隐藏右边的Y轴
        mLineChart.getAxisRight().setEnabled(false);
    }

    public void getLineChart(String labelId, int type) {
        try {
            ArticlesRepo.getLineChartWxy(labelId, type).observe(this, new ApiObserver<List<LineChartWxy>>() {
                @Override
                public void onSuccess(Response<List<LineChartWxy>> response) {
                    List<LineChartWxy> wxyEvents = new ArrayList<>();
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    Util.login(String.valueOf(code), LineChartWxyFxTEstActivity.this);
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