package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.EntTypeEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：企业通用选择
 * Desc：
 */
public class EntEPTypeAdapter extends BaseQuickAdapter<EntSEPTypeEvent, BaseViewHolder> {
    public Activity activity;

    public EntEPTypeAdapter(List<EntSEPTypeEvent> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntSEPTypeEvent item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
