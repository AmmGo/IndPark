package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.hl.indpark.R;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

public class SetUpActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("设置");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
