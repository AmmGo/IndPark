package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyApprovalEvent;
import com.hl.indpark.entities.events.MyPeportEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的上报
 * Desc：
 */
public class MyReportAdapter extends BaseQuickAdapter<MyPeportEvent.RecordsBean, BaseViewHolder> {
    public MyReportAdapter(List<MyPeportEvent.RecordsBean> list) {
        super(R.layout.item_my_report, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyPeportEvent.RecordsBean item) {
        if (item.status != null) {
            if (item.status.equals("1")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_ok);
            } else if (item.status.equals("2")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_un);
            }
            holder.setText(R.id.tv_type, item.eventType);
            holder.setText(R.id.tv_time, item.createTime);
            holder.setText(R.id.tv_report_people, item.reportedName);
            holder.setText(R.id.tv_phone, item.phone);
        }
    }


}
