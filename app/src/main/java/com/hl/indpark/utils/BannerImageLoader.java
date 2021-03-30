package com.hl.indpark.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 *
 * @Package:        com.hl.indpark.utils
 * @ClassName:      LENOVO
 * @Description:     java类作用描述
 * @Author:         yjl
 * @CreateDate:     2021/3/30 15:39
 * @UpdateUser:     更新者：
 * @UpdateDate:     2021/3/30 15:39
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        net.arvin.baselib.utils.ImageLoader.load(context, path, imageView, false, 0);
    }
}
