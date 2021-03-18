package com.hl.indpark.uis.activities.videoactivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.hl.indpark.R;
import com.hl.indpark.uis.activities.videoactivities.utils.WindowUtil;
import com.hl.indpark.utils.Util;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtm.RemoteInvitation;

public class VideoActivity extends BaseCallActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    private FrameLayout mLocalPreviewLayout;
    private FrameLayout mRemotePreviewLayout;
    private AppCompatImageView mMuteBtn;
    private String mChannel;
    private int mPeerUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initUI();
        initVideo();
        initStatusBarHeight();
        WindowUtil.hideWindowStatusBar(getWindow());

    }

    private void initStatusBarHeight() {
        statusBarHeight = WindowUtil.getSystemStatusBarHeight(this);
    }

    private void initUI() {
        mLocalPreviewLayout = findViewById(R.id.local_preview_layout);
        mRemotePreviewLayout = findViewById(R.id.remote_preview_layout);

        mMuteBtn = findViewById(R.id.btn_mute);
        mMuteBtn.setActivated(true);
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mLocalPreviewLayout.getLayoutParams();
        params.topMargin += statusBarHeight;
        mLocalPreviewLayout.setLayoutParams(params);

        RelativeLayout buttonLayout = findViewById(R.id.button_layout);
        params = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
        params.bottomMargin = displayMetrics.heightPixels / 8;
        params.leftMargin = displayMetrics.widthPixels / 6;
        params.rightMargin = displayMetrics.widthPixels / 6;
        buttonLayout.setLayoutParams(params);
    }

    private void initVideo() {
        Intent intent = getIntent();
        mChannel = intent.getStringExtra(Constants.KEY_CALLING_CHANNEL);
        try {
            mPeerUid = Integer.valueOf(intent.getStringExtra(Constants.KEY_CALLING_PEER));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "数字",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        rtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
        setVideoConfiguration();
        setupLocalPreview();
        joinRtcChannel(mChannel, "", Integer.parseInt(Util.getUserId()));
    }

    private void setupLocalPreview() {
        setupLocalVideo();
    }

    @Override
    public void onUserJoined(final int uid, int elapsed) {
        if (uid != mPeerUid) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupRemoteVideo(uid);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        if (uid != mPeerUid) return;
        finish();
    }

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_endcall:
                finish();
                break;
            case R.id.btn_mute:
                rtcEngine().muteLocalAudioStream(mMuteBtn.isActivated());
                mMuteBtn.setActivated(!mMuteBtn.isActivated());
                break;
            case R.id.btn_switch_camera:
                rtcEngine().switchCamera();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        leaveChannel();
    }

    @Override
    public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {
        // Do not respond to any other calls
        Log.i(TAG, "Ignore remote invitation from " +
                remoteInvitation.getCallerId() + " while in calling");
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalPreviewLayout.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        rtcEngine().setupLocalVideo(mLocalVideo);
    }

    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemotePreviewLayout;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalPreviewLayout;
        }

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        if (mRemoteVideo != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalPreviewLayout);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        rtcEngine().setupRemoteVideo(mRemoteVideo);
    }

    private VideoCanvas mLocalVideo;
    private VideoCanvas mRemoteVideo;

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalPreviewLayout) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemotePreviewLayout.addView(canvas.view);
        } else if (parent == mRemotePreviewLayout) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalPreviewLayout.addView(canvas.view);
        }
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }
}
