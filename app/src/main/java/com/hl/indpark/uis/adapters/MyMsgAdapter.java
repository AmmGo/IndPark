package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyMsgEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：我的消息
 * Desc：
 */
public class MyMsgAdapter extends BaseQuickAdapter<MyMsgEvent.RecordsBean, BaseViewHolder> {
    public MyMsgAdapter(List<MyMsgEvent.RecordsBean> list) {
        super(R.layout.item_my_msg, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyMsgEvent.RecordsBean item) {

            holder.setText(R.id.tv_content, item.name);
            holder.setText(R.id.tv_time, item.pushTime);
            if (item.read==0){
                holder.setGone(R.id.tv_red,true);
            }else{
                holder.setGone(R.id.tv_red,false);
            }

    }


}
