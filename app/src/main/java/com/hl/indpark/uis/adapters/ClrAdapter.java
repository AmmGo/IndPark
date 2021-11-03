package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.new2.Clr;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：处理人
 * Desc：
 */
public class ClrAdapter extends BaseQuickAdapter<Clr, BaseViewHolder> {
    public Activity activity;

    public ClrAdapter(List<Clr> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Clr item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
