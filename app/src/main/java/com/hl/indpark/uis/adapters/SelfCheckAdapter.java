package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.SelfCheck;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的上报
 * Desc：
 */
public class SelfCheckAdapter extends BaseQuickAdapter<SelfCheck.RecordsBean, BaseViewHolder> {
    public SelfCheckAdapter(List<SelfCheck.RecordsBean> list) {
        super(R.layout.item_self_check, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, SelfCheck.RecordsBean item) {
        holder.setText(R.id.tv_type, getType(item.checkType));
        holder.setText(R.id.tv_time, item.createTime);
        holder.setText(R.id.tv_content, item.details);


    }
    //@ApiModelProperty(value = "自检类型 1：安全自检 2： 消防自检 3：设备自检 4：其他自检", name = "checkType")
    public String getType(int type) {
        String type1 = "";
        switch (type) {
            case 1:
                type1 = "安全自检";
                break;
            case 2:
                type1 = "消防自检";
                break;
            case 3:
                type1 = "设备自检";
                break;
            case 4:
                type1 = "其他自检";
                break;
            default:
                type1 = "其他自检";
        }
        return type1;
    }

}
