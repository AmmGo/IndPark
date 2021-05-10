package com.hl.indpark.uis.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyMsgEvent;

import java.util.List;


 /**
  * Created by yjl on 2021/5/10 13:30
  * Function：
  * Desc：日志管理
  */
public class LogManagerAdapter extends BaseQuickAdapter<MyMsgEvent.RecordsBean, BaseViewHolder> {
    public LogManagerAdapter(List<MyMsgEvent.RecordsBean> list) {
        super(R.layout.item_log_manger, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyMsgEvent.RecordsBean item) {

            holder.setText(R.id.tv_content, item.name);
            holder.setText(R.id.tv_time, item.pushTime);

    }


}
