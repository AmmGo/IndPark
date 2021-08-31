package com.hl.indpark.utils;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hl.indpark.R;
import com.hl.indpark.uis.activities.LineChartHbPcActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MyMarkerPcView extends MarkerView {
    Map<Integer, Float> map1 = new HashMap<>();
    Map<Integer, Float> map2 = new HashMap<>();
    Map<Integer, Float> map3 = new HashMap<>();


    private TextView tvContent;
    private DecimalFormat format = new DecimalFormat("##0");
    private String time = "";
    public MyMarkerPcView(Context context) {
        super(context, R.layout.layout_markerview);//这个布局自己定义
        this.map1 = LineChartHbPcActivity.mapAd;
        this.map2 = LineChartHbPcActivity.mapZl;
        this.map3 = LineChartHbPcActivity.mapCod;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    //显示的内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        StringBuffer sb = new StringBuffer();
        if (map1.size() > 0) {
            sb.append(LineChartHbPcActivity.KEY_NAME1).append(":").append(""+map1.get((int) e.getX())).append("<br>");
            time  = LineChartHbPcActivity.mapAdTime.get((int) e.getX());
        }
        if (map2.size() > 0) {
            sb.append(LineChartHbPcActivity.KEY_NAME2).append(":").append(""+map2.get((int) e.getX())).append("<br>");
            time  = LineChartHbPcActivity.mapZlTime.get((int) e.getX());
        }
        if (map3.size() > 0) {
            sb.append(LineChartHbPcActivity.KEY_NAME3).append(":").append(""+map3.get((int) e.getX())).append("<br>");
            time  = LineChartHbPcActivity.mapCodTime.get((int) e.getX());
        }
        sb.append("时间:"+time);
        tvContent.setText(Html.fromHtml(sb.toString()));
        super.refreshContent(e, highlight);
    }

    //标记相对于折线图的偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}