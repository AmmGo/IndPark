package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyApprovalEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的审批
 * Desc：
 */
public class MyApprovalAdapter extends BaseQuickAdapter<MyApprovalEvent.RecordsBean, BaseViewHolder> {
    public MyApprovalAdapter(List<MyApprovalEvent.RecordsBean> list) {
        super(R.layout.item_my_approval, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyApprovalEvent.RecordsBean item) {
        if (item.status != null) {
            if (item.status.equals("0")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_ok);
            } else if (item.status.equals("1")) {
                holder.setImageResource(R.id.img_status, R.mipmap.img_self_rep_un);
            }
            holder.setText(R.id.tv_type, item.eventType);
            holder.setText(R.id.tv_time, item.createTime);
            holder.setText(R.id.tv_report_people, item.reportedName);
            holder.setText(R.id.tv_phone, item.phone);
        }
    }


}
