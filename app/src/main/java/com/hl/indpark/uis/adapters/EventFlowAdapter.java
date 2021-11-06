package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyMsgEvent;
import com.hl.indpark.entities.new2.EventFlow;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的消息
 * Desc：
 */
public class EventFlowAdapter extends BaseQuickAdapter<EventFlow, BaseViewHolder> {
    public EventFlowAdapter(List<EventFlow> list) {
        super(R.layout.item_event_flow, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, EventFlow item) {

            holder.setText(R.id.tv_0, item.codeName);
            holder.setText(R.id.tv_1, item.handlerContent);
            holder.setText(R.id.tv_2, "创建时间："+item.handlerTime);
            if (item.remarkes!=null){
                holder.setVisible(R.id.tv_3,true);
                holder.setText(R.id.tv_3, "处理意见："+item.remarkes);
            }else{
                holder.setVisible(R.id.tv_3,false);
                holder.setText(R.id.tv_3, "处理意见："+item.remarkes);
            }
//            holder.setText(R.id.tv_3, item.name);

    }


}
