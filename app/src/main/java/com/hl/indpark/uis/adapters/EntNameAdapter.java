package com.hl.indpark.uis.adapters;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntNameEvent;

import java.util.List;

public class EntNameAdapter extends BaseQuickAdapter<EntNameEvent, BaseViewHolder> {
    public Activity activity;

    public EntNameAdapter(List<EntNameEvent> list, Activity activity1) {
        super(R.layout.item_ent_name, list);
        this.activity = activity1;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntNameEvent item) {
        helper.setText(R.id.tv_name, item.name);
        helper.addOnClickListener(R.id.tv_name);
    }


}
