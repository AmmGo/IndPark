package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.hl.indpark.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.util.Locale;

import butterknife.BindView;

import static com.scwang.smartrefresh.layout.util.DensityUtil.px2dp;

 /**
  * Created by yjl on 2021/4/6 13:51
  * Function：帮助详情
  * Desc：
  */
public class HelpDetailsActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;
    private String helpTitle;
    private String webUrl;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @Override
    protected int getContentView() {
        return R.layout.activity_help_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        int helpId = getIntent().getIntExtra("HELPID", 0);
        switch (helpId) {
            case 1:
                /**积分规则*/
                helpTitle = "积分规则";
                webUrl = "file:///android_asset/help/jfgz.html";
                break;
            default:
        }
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText(helpTitle);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout.autoRefresh();//自动刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWebLive();
                    }
                }, 50);
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);//开启DOM
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.finishRefresh();
                view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", px2dp(mWebView.getPaddingTop())));
            }
        });
    }
    public void getWebLive() {
        mWebView.loadUrl(webUrl);
    }
}
