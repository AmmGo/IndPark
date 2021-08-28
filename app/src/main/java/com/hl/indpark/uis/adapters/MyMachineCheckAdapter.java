package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MachineCheck;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的消息
 * Desc：
 */
public class MyMachineCheckAdapter extends BaseQuickAdapter<MachineCheck, BaseViewHolder> {
    public MyMachineCheckAdapter(List<MachineCheck> list) {
        super(R.layout.item_my_machine_check, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MachineCheck item) {

        String str1 = item.equipmentName == null ? "" : item.equipmentName;
        String str2 = item.equipmentAddress == null ? "" : item.equipmentAddress;
        String str3 = item.examinePerson == null ? "" : item.examinePerson;
        String str4 = item.phone == null ? "" : item.phone;
        String str5 = item.examineTime == null ? "" : item.examineTime;
        String str6 = item.enterpriseName == null ? "" : item.enterpriseName;
        holder.setText(R.id.tv_sbmc, "设备名称：" + str1);
        holder.setText(R.id.tv_sbdz, "设备地址：" + str2);
        holder.setText(R.id.tv_xjry, "巡检人员：" + str3);
        holder.setText(R.id.tv_xjry_lxdh, "联系电话：" + str4);
        holder.setText(R.id.tv_xjrq, "巡检日期：" + str5);
        holder.setText(R.id.tv_xjqy, "巡检企业：" + str6);

    }


}
