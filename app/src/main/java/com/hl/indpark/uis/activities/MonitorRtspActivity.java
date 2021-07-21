package com.hl.indpark.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.hl.indpark.R;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;

import java.net.URLEncoder;

import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerView;

public class MonitorRtspActivity extends BaseActivity {

    private NodePlayerView nodePlayerView;
    private NodePlayer nodePlayer;
    private String url;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nodePlayer.stop();
        nodePlayer.release();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_monitor_rtsp;
    }
    /**
     * URLEncoder编码
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        url = getIntent().getStringExtra("URL");
        url ="http://www.nxzwgyyqgwh.com.cn/live?url="+ toURLEncoded(url);
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("视频详情");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nodePlayerView = findViewById(R.id.nodePlayer);
        //设置渲染器类型
        nodePlayerView.setRenderType(NodePlayerView.RenderType.SURFACEVIEW);
        //设置视频画面缩放模式
        nodePlayerView.setUIViewContentMode(NodePlayerView.UIViewContentMode.ScaleToFill);

        nodePlayer = new NodePlayer(this);
        //设置播放视图
        nodePlayer.setPlayerView(nodePlayerView);
        //设置RTSP流使用的传输协议,支持的模式有:
        nodePlayer.setRtspTransport(NodePlayer.RTSP_TRANSPORT_TCP);
        nodePlayer.setInputUrl(url);
        //设置视频是否开启
        nodePlayer.setVideoEnable(true);
        nodePlayer.setBufferTime(0);
        nodePlayer.setMaxBufferTime(0);
        nodePlayer.start();
    }

}