package net.arvin.baselib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(getContentView());
        init(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);
}
