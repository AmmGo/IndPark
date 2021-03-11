package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：企业通用选择
 * Desc：
 */
public class ReportTypeAdapter extends BaseQuickAdapter<ReportTypeEvent, BaseViewHolder> {
    public Activity activity;

    public ReportTypeAdapter(List<ReportTypeEvent> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportTypeEvent item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
