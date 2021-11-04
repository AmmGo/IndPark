package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.new2.EventPageList;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的上报
 * Desc：
 */
public class MyReportAdapter extends BaseQuickAdapter<EventPageList.RecordsBean, BaseViewHolder> {
    public MyReportAdapter(List<EventPageList.RecordsBean> list) {
        super(R.layout.item_my_report, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, EventPageList.RecordsBean item) {
        if (item.status != null) {
            try {
                if (item.status.equals("1")) {
                    holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_ok);
                } else if (item.status.equals("2")) {
                    holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_un);
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.setImageResource(R.id.img_status, R.color.background_white);
            }
        }
        holder.setText(R.id.tv_type, item.name);
        holder.setText(R.id.tv_time, item.createTime);
        holder.setText(R.id.tv_report_people, item.createName);
        holder.setText(R.id.tv_phone, item.phone);
    }


}
