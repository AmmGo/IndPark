package com.hl.indpark.uis.activities.videoactivities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.hl.indpark.App;
import com.hl.indpark.uis.activities.videoactivities.agora.Config;
import com.hl.indpark.uis.activities.videoactivities.agora.Global;
import com.hl.indpark.uis.activities.videoactivities.agora.IEventListener;
import com.hl.indpark.uis.activities.videoactivities.utils.WindowUtil;

import java.util.Map;

import io.agora.rtc.RtcEngine;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;

;

public abstract class BaseActivity extends AppCompatActivity implements IEventListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected int statusBarHeight;
    protected DisplayMetrics displayMetrics = new DisplayMetrics();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowUtil.hideWindowStatusBar(getWindow());
        App.putActivityInfoToMap(this);
        setGlobalLayoutListener();
        getDisplayMetrics();
//        initStatusBarHeight();
    }

    private void setGlobalLayoutListener() {
        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver observer = layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onGlobalLayoutCompleted();
            }
        });
    }

    /**
     * Give a chance to obtain view layout attributes when the
     * content view layout process is completed.
     * Some layout attributes will be available here but not
     * in onCreate(), like measured width/height.
     * This callback will be called ONLY ONCE before the whole
     * window content is ready to be displayed for first time.
     */
    protected void onGlobalLayoutCompleted() {

    }

    private void getDisplayMetrics() {
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    private void initStatusBarHeight() {
        statusBarHeight = WindowUtil.getSystemStatusBarHeight(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        removeEventListener(this);
    }

    public App application() {
        return (App) getApplication();
    }

    protected RtcEngine rtcEngine() {
        return application().rtcEngine();
    }

    protected RtmClient rtmClient() {
        return application().rtmClient();
    }

    protected RtmCallManager rtmCallManager() {
        return application().rtmCallManager();
    }

    protected Config config() {
        return application().config();
    }

    protected Global global() {
        return application().global();
    }

    private void registerEventListener(IEventListener listener) {
        application().registerEventListener(listener);
    }

    private void removeEventListener(IEventListener listener) {
        application().removeEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeEventListener(this);
        App.removeActivityInfoFromMap(this);
    }

    @Override
    public void onConnectionStateChanged(int status, int reason) {
        Log.i(TAG, "onConnectionStateChanged status:" + status + " reason:" + reason);
        if (status==1){
//            App.getInstance().rtmClient().login(null, Util.getUserId(), new ResultCallback<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.e("main", "rtm client login success");
//                }
//
//                @Override
//                public void onFailure(ErrorInfo errorInfo) {
//                    Log.e("main", "rtm client login failed:" + errorInfo.getErrorDescription());
//                }
//            });
        }
    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

    }

    @Override
    public void onLocalInvitationReceived(LocalInvitation localInvitation) {

    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation localInvitation, String response) {

    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation localInvitation, String response) {

    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation localInvitation) {

    }

    @Override
    public void onLocalInvitationFailure(LocalInvitation localInvitation, int errorCode) {

    }

    @Override
    public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int errorCode) {

    }
}
