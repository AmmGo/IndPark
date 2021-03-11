package com.hl.indpark.uis.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.HSAlarmEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.PieChartDataActivity;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;


/**
 * Created by yjl on 2021/3/9 13:19
 * Function：
 * Desc：报警统计---危险源
 */
public class TabSHSFragment extends BaseFragment {

    private PieChart mPieChart;
    private ArrayList<PieEntry> data = new ArrayList<PieEntry>();
    private PieData pieData;
    private PieEntry entry1;
    private PieEntry entry2;

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_shs;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPieChart = root.findViewById(R.id.chart_sep);
        initData();
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) return;
                Intent intent = new Intent(getActivity(), PieChartDataActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {
                //图表外部点击事件 也是（上一个方法为点击一次放大） 本方法为二次点击事件

            }
        });
    }

    private void initData() {
        ArticlesRepo.getHSAlarm("1").observe(this, new ApiObserver<HSAlarmEvent>() {
            @Override
            public void onSuccess(Response<HSAlarmEvent> response) {
                if (response != null && response.getData() != null) {
                    HSAlarmEvent alarmEvent = response.getData();
                    sepPieChart(alarmEvent);
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

    private void sepPieChart(HSAlarmEvent alarmEvent) {
        data.clear();
        for (int i = 0; i < alarmEvent.value.size(); i++) {
            data.add(new PieEntry(alarmEvent.value.get(i).num, HSAlarmEvent.getType(alarmEvent.value.get(i).type)));
        }
        PieDataSet dataSet = new PieDataSet(data, "");
        pieData = new PieData(dataSet);
        mPieChart.setUsePercentValues(false);//设置使用百分比（后续有详细介绍）
        mPieChart.setExtraOffsets(26, 5, 26, 5);//设置边距
        mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置摩擦系数（值越小摩擦系数越大）
        mPieChart.setCenterText(alarmEvent.key + "危险源报警");//设置环中的文字
        mPieChart.getDescription().setEnabled(true);//设置描述
        mPieChart.setRotationEnabled(true);//是否可以旋转
        mPieChart.setHighlightPerTapEnabled(true);//点击是否放大
        mPieChart.setCenterTextSize(8f);//设置环中文字的大小
        mPieChart.setDrawCenterText(true);//设置绘制环中文字
        mPieChart.setRotationAngle(120f);//设置旋转角度
        mPieChart.setTransparentCircleRadius(30f);//设置半透明圆环的半径,看着就有一种立体的感觉
        mPieChart.setDrawEntryLabels(true);
        //这个方法为true就是环形图，为false就是饼图
        //设置环形中间空白颜色是白色
        mPieChart.setHoleColor(Color.WHITE);
        //设置半透明圆环的颜色
        mPieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        mPieChart.setTransparentCircleAlpha(110);
        mPieChart.animateXY(1400, 1400);
        mPieChart.spin(1000, mPieChart.getRotationAngle(), mPieChart.getRotationAngle() + 360, Easing.EaseInCubic);
//        dataSet.setSelectionShift(5f);
//        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        //数据连接线距图形片内部边界的距离，为百分数(0~100f)
//        dataSet.setValueLinePart1OffsetPercentage(100f);
////设置x,y在圆外显示的值为透明(transparent = 0x00000000)
//        pieData.setValueTextColor(0x00000000);
        // 设置饼块之间的间隔
        dataSet.setSliceSpace(1f);
        dataSet.setHighlightEnabled(true);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);

        dataSet.setValueLineColor(Color.YELLOW);//设置连接线的颜色
        // 不显示图例
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.tab_sep));
        colors.add(getResources().getColor(R.color.primary));
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

}
