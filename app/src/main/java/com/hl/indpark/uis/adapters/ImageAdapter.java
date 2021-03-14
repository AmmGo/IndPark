package com.hl.indpark.uis.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;


/**
 * @author MediaAdapter
 * @time 2018/12/3 10:21
 */
public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        //设置图片圆角角度
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.a_error)
                .fallback(R.drawable.a_error)
                .error(R.drawable.a_error);
        Glide.with(mContext).load(item)
                .apply(options).into((ImageView) helper.getView(R.id.iv_img_or_video));

    }
}
