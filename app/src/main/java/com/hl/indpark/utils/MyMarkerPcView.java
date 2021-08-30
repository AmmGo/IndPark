package com.hl.indpark.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hl.indpark.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MyMarkerPcView extends MarkerView {
    Map<Integer, Float> map1 = new HashMap<>();
    Map<Integer, Float> map2 = new HashMap<>();
    Map<Integer, Float> map3 = new HashMap<>();


    private TextView tvContent;
    private DecimalFormat format = new DecimalFormat("##0");

    public MyMarkerPcView(Context context, Map<Integer, Float> map1,Map<Integer, Float> map2,Map<Integer, Float> map3) {
        super(context, R.layout.layout_markerview);//这个布局自己定义
        this.map1 = map1;
        this.map2 = map2;
        this.map3 = map3;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    //显示的内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(String.valueOf(map1.get((int)e.getY()))+String.valueOf(map2.get((int)e.getY()))+String.valueOf(map3.get((int)e.getY())));
        super.refreshContent(e, highlight);
    }

    //标记相对于折线图的偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}