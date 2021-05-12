package com.hl.indpark.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.hl.indpark.R;

public class MessageHelper {

    /**
     * 获取popupwindow对象
     *
     * @param layoutView 布局文件
     * @return
     */
    public static PopupWindow getPopupWindow(View layoutView) {
        // 创建PopupWindow实例
        final PopupWindow popupwindow = new PopupWindow(layoutView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setAnimationStyle(R.style.AnimHead);

        // popupwindow.setFocusable(false);
        // 设置点击屏幕其它地方弹出框消失
        popupwindow.setOutsideTouchable(true);
        popupwindow.setFocusable(true);

        // 自定义view添加触摸事件
        layoutView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                }

                return false;
            }

        });

        return popupwindow;
    }

}
