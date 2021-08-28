package com.hl.indpark.entities.events;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LineChartDouble {
    public LineChartDouble(LineChart chart) {
        // 数据描述
        chart.getDescription().setEnabled(true);
        //背景
        chart.setBackgroundColor(0xffffffff);
        //定义数据描述得位置
        //chart.setDescriptionPosition(2,100);
        // 设置描述文字的颜色
        // chart.setDescriptionColor(0xffededed);
        // 动画Line
        chart.animateY(1000);
        //设置阴影
        //chart.setDrawBarShadow(true);
        //设置边框
        chart.setDrawBorders(true);
        // 设置是否可以触摸
        chart.setTouchEnabled(true);
        // 是否可以拖拽
        chart.setDragEnabled(true);
        // 是否可以缩放
        chart.setScaleEnabled(true);
        //设置网格背景
        chart.setGridBackgroundColor(0xffffffff);

        //设置边线宽度
        chart.setBorderWidth(0);
        //设置边线颜色
        chart.setBorderColor(0xfff5f5f5);
        // 集双指缩放
        chart.setPinchZoom(false);
        // 隐藏右边的坐标轴
        chart.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴
        chart.getAxisLeft().setEnabled(true);
        Legend mLegend = chart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.SQUARE);
        //设置图标位置
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        // 字体
        mLegend.setFormSize(4f);
        //是否显示注释
        mLegend.setEnabled(false);
        // 字体颜色
//        mLegend.setTextColor(Color.parseColor("#7e7e7e"));
        //设置X轴位置
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        //设置轴线得颜色
        xAxis.setAxisLineColor(0xfff5f5f5);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setDrawGridLines(false);
//        xAxis.setSpaceBetweenLabels(2);
        //设置网色
        xAxis.setGridColor(0xfff5f5f5);
        //设置X轴字颜色
        xAxis.setTextColor(0xff666666);
        //设置Y轴
        YAxis leftAxis = chart.getAxisLeft();
        //Y轴颜色
        leftAxis.setAxisLineColor(0xfff5f5f5);
        //Y轴参照线颜色
        leftAxis.setGridColor(0xfff5f5f5);
        //设置Y轴字颜色
        leftAxis.setTextColor(0xff666666);
        //参照线长度
//        leftAxis.setAxisLineWidth(5f);
        // 顶部居最大值站距离占比
        leftAxis.setSpaceTop(20f);
        chart.invalidate();
    }
    public LineDataSet getDataSet(List<LineChartWxy> list) {
        ArrayList<Entry> valueSet1 = new ArrayList<Entry>();
        for (int i = 0 ;i<list.size();i++){
            Entry vle = new Entry();
            vle.setX(i);
            vle.setY((float) list.get(i).value);
            valueSet1.add(vle);
        }
        LineDataSet barDataSet1 = new LineDataSet(valueSet1, "数据1注解");
        barDataSet1.setColor(Color.parseColor("#45a2ff"));
        //设置外圈颜色
        barDataSet1.setCircleColor(Color.parseColor("#45a2ff"));
        //圈大小
        barDataSet1.setCircleSize(3);
        //设置内圈颜色
        barDataSet1.setCircleColorHole(Color.parseColor("#ffffff"));
        return barDataSet1;
    }
    public ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();
        for (int j = 0; j < 12; j++){
            xAxis.add((j+1)+"");
        }
        return xAxis;
    }
}
