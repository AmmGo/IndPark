package com.hl.indpark.uis.adapters;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntSHSEvent;

import java.util.List;
//报警类型；0: 正常;1：高高报；2：高报；3：低报；4：低低报

public class EntSHSAdapter extends BaseQuickAdapter<EntSHSEvent, BaseViewHolder> {
    public EntSHSAdapter(@Nullable List<EntSHSEvent> data) {
        super(R.layout.item_pie_data, data);
    }
    @Override
    protected void convert(BaseViewHolder holder, EntSHSEvent item) {
        if (item.type != null) {
            if (item.type.equals("0")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_zc);
            } else if (item.type.equals("1")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_ggb);
            } else if (item.type.equals("2")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_gb);
            } else if (item.type.equals("3")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_db);
            } else if (item.type.equals("4")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_ddb);
            }
            holder.setText(R.id.tv_site, item.pointName);
            holder.setText(R.id.tv_type, item.technologyName);
            holder.setText(R.id.tv_num, item.value+item.dataType);
            holder.setText(R.id.tv_phone, item.time);
        }
    }
}