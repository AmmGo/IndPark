package com.hl.indpark.uis.adapters;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntSEPEvent;

import java.util.List;
//报警类型；0: 正常;1：高高报；2：高报；3：低报；4：低低报

public class EntSEPAdapter extends BaseQuickAdapter<EntSEPEvent.RecordsBean, BaseViewHolder> {
    private int type;

    public EntSEPAdapter(@Nullable List<EntSEPEvent.RecordsBean> data) {
        super(R.layout.item_pie_data_ep, data);
    }

    public void getType(int type) {
        this.type = type;

    }

    @Override
    protected void convert(BaseViewHolder holder, EntSEPEvent.RecordsBean item) {
//        if (item.isException != null) {
        if (item.isException != null && item.isException.equals("0")) {
            holder.setImageResource(R.id.img_status, R.mipmap.img_zc);
        } else if (item.isException != null && item.isException.equals("1")) {
            holder.setImageResource(R.id.img_status, R.mipmap.img_yc);
        } else if (item.isException != null && item.isException.equals("2")) {
            holder.setImageResource(R.id.img_status, R.mipmap.img_cb);
        } else {
            holder.setImageResource(R.id.img_status, R.color.background_white);
        }
        holder.setText(R.id.tv_site, item.equipmentName);
        holder.setText(R.id.tv_time, item.monitorTime);
        if (type == 1) {
            //废气
            holder.setText(R.id.tv_num, item.exhaustData);
            holder.setText(R.id.tv_mg_1, "烟尘实测浓度mg/m3:");
            holder.setText(R.id.tv_mg_1_1, item.smokeData);
            holder.setText(R.id.tv_mg_2, "二氧化硫实测浓度mg/m3:");
            holder.setText(R.id.tv_mg_2_1, item.sulfurData);
            holder.setText(R.id.tv_mg_3, "氮氧化物实测浓度mg/m3:");
            holder.setText(R.id.tv_mg_3_1, item.nitrogenData);
        } else if (type == 2) {
            //废水
            holder.setText(R.id.tv_num, item.wastewaterData);
            holder.setText(R.id.tv_mg_1, "COD实测浓度mg/L:");
            holder.setText(R.id.tv_mg_1_1, item.coddata);
            holder.setText(R.id.tv_mg_2, "氨氮实测浓度mg/L:");
            holder.setText(R.id.tv_mg_2_1, item.ammoniacalData);
            holder.setText(R.id.tv_mg_3, "总磷实测浓度mg/L:");
            holder.setText(R.id.tv_mg_3_1, item.totalData);
        }

//        }
    }
}