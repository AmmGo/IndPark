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

public class MyMarkerView extends MarkerView {
    Map<Integer, String> map = new HashMap<>();


    private TextView tvContent;
    private DecimalFormat format = new DecimalFormat("##0");

    public MyMarkerView(Context context, Map<Integer, String> map) {
        super(context, R.layout.layout_markerview);//这个布局自己定义
        this.map = map;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    //显示的内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        StringBuffer sb = new StringBuffer();
        sb.append(""+e.getY());
        if (map.size() > 0) {
            sb.append(map.get((int)e.getY())).append("<br>");
        }
        tvContent.setText(Html.fromHtml(sb.toString()));
        super.refreshContent(e, highlight);
    }

    //标记相对于折线图的偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}