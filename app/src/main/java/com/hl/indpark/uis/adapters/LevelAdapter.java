package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.new2.Level;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：等级
 * Desc：
 */
public class LevelAdapter extends BaseQuickAdapter<Level, BaseViewHolder> {
    public Activity activity;

    public LevelAdapter(List<Level> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Level item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
