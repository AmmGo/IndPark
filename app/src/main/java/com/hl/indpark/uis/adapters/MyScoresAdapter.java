package com.hl.indpark.uis.adapters;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.MyScoresEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：积分item
 * Desc：
 */
public class MyScoresAdapter extends BaseQuickAdapter<MyScoresEvent.RecordsBean, BaseViewHolder> {
    public MyScoresAdapter(List<MyScoresEvent.RecordsBean> list) {
        super(R.layout.item_my_scores, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyScoresEvent.RecordsBean item) {
        try {
            if (item.createTime!=null&&item.createTime.length()>10){
                holder.setText(R.id.tv_time, item.createTime.substring(0, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.setText(R.id.tv_name, item.name);
            int num = item.type;
            if (num == 1) {
                String red = "<font color='#FF0000'>"
                        + "+" + item.credit + "</font>";
                holder.setText(R.id.tv_scores, Html.fromHtml(red) );
            } else {
                String green = "<font color='#07C92C'>"
                        + "-" + item.credit + "</font>";
                holder.setText(R.id.tv_scores, Html.fromHtml(green) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
