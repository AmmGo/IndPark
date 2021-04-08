package com.hl.indpark.uis.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.CommodityEvent;

import java.util.List;

/**
 * Created by yjl on 2021/3/11 14:40
 * Function：商品
 * Desc：
 */
public class MyCommodityAdapter extends BaseQuickAdapter<CommodityEvent.RecordsBean, BaseViewHolder> {
    public MyCommodityAdapter(List<CommodityEvent.RecordsBean> list) {
        super(R.layout.item_my_commodity, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommodityEvent.RecordsBean item) {
        try {
            holder.setText(R.id.tv_scores, item.credit+"");
            holder.setText(R.id.tv_in_stock, "库存:"+item.inventory+"");
            holder.addOnClickListener(R.id.img_exchange);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.a_error)
                    .fallback(R.drawable.a_error)
                    .error(R.drawable.a_error);
            String imgUrl = item.image;
            Glide.with(mContext).load(imgUrl)
                    .apply(options).into((ImageView) holder.getView(R.id.rl_item));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
