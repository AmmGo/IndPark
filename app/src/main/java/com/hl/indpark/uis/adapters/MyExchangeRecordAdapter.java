package com.hl.indpark.uis.adapters;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyExchangeRecordEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：积分兑换item
 * Desc：
 */
public class MyExchangeRecordAdapter extends BaseQuickAdapter<MyExchangeRecordEvent.RecordsBean, BaseViewHolder> {
    public MyExchangeRecordAdapter(List<MyExchangeRecordEvent.RecordsBean> list) {
        super(R.layout.item_my_exchange_record, list);
    }
    @Override
    protected void convert(BaseViewHolder holder, MyExchangeRecordEvent.RecordsBean item) {
        try {
            if (item.createTime!=null&&item.createTime.length()>10){
                holder.setText(R.id.tv_time, item.createTime.substring(0, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_name, item.product+"");
        try {
            int num = item.state;
            if (num == 1) {
                String red = "<font color='#FF0000'>"
                        + "" + item.credit + "分</font>";
                holder.setText(R.id.tv_scores, Html.fromHtml(red) );
            } else {
                String green = "<font color='#07C92C'>"
                        + "" + item.credit + "分</font>";
                holder.setText(R.id.tv_scores, Html.fromHtml(green) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
