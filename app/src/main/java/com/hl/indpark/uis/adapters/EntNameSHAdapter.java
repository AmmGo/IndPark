package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntNameEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：企业名称选择
 * Desc：
 */
public class EntNameSHAdapter extends BaseQuickAdapter<EntNameEvent, BaseViewHolder> {
    public Activity activity;

    public EntNameSHAdapter(List<EntNameEvent> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntNameEvent item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
