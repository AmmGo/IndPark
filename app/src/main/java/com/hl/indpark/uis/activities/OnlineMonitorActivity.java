package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.hl.indpark.R;
import com.hl.indpark.uis.fragments.OnLineHbFragment;
import com.hl.indpark.uis.fragments.OnLineWxyFragment;

import net.arvin.baselib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineMonitorActivity extends BaseActivity {
    private OnLineHbFragment hbFragment;

    private OnLineWxyFragment wxyFragment;

    private PopupWindow popupwindow;
    @BindView(R.id.title)
    TextView textView;
    private static String hbtitle = "环保在线监测";
    private static String wxytitle = "危险源在线监测";

    @OnClick({R.id.ll_title, R.id.img_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ll_title:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 5);
                }
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_online_monitor;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textView.setText(hbtitle);
        initHb();

    }

    private void initHb() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (hbFragment == null) {
            hbFragment = new OnLineHbFragment();
            transaction.add(R.id.fr_online, hbFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(hbFragment);
        transaction.commit();

    }

    private void initWxy() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (wxyFragment == null) {
            wxyFragment = new OnLineWxyFragment();
            transaction.add(R.id.fr_online, wxyFragment);
        }
        hideFragment(transaction);
        transaction.show(wxyFragment);

        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (hbFragment != null) {
            transaction.hide(hbFragment);
        }
        if (wxyFragment != null) {
            transaction.hide(wxyFragment);
        }
    }

    public void initmPopupWindowView() {

        View customView = getLayoutInflater().inflate(R.layout.item_online,
                null, false);
        popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setAnimationStyle(R.style.AnimationFade);
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }

                return false;
            }
        });
        /** 在这里可以实现自定义视图的功能 */
        LinearLayout ll_hbzxjc = (LinearLayout) customView.findViewById(R.id.ll_hbzxjc);
        LinearLayout ll_wxyzxjc = (LinearLayout) customView.findViewById(R.id.ll_wxyzxjc);
        ll_hbzxjc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHb();
                textView.setText(hbtitle);
                popupwindow.dismiss();
            }
        });
        ll_wxyzxjc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWxy();
                textView.setText(wxytitle);
                popupwindow.dismiss();
            }
        });


    }
}