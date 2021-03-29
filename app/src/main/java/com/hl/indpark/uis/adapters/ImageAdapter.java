package com.hl.indpark.uis.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hl.indpark.R;
import com.hl.indpark.nets.Api;

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
        ImageView img = (ImageView) helper.getView(R.id.iv_img_or_video);
        helper.addOnClickListener(R.id.iv_img_or_video);
        //设置图片圆角角度
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.a_error)
                .fallback(R.drawable.a_error)
                .error(R.drawable.a_error);
//        Glide.with(mContext).load("https://gitee.com/ammgo/zjb/raw/master/blog_img/2020_08_10/pexels-eberhard-grossgasteiger-691668.jpg")
//                .apply(options).into(img);
        Glide.with(mContext).load(Api.BASE_URL_IMG+item)
                .apply(options).into(img);

    }
}
