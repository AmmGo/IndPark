package com.hl.indpark.uis.activities.videoactivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.tu.loadingdialog.LoadingDailog;
import com.hl.indpark.R;
import com.hl.indpark.uis.activities.videoactivities.utils.RtcUtils;
import com.hl.indpark.utils.Util;

import net.arvin.baselib.utils.ToastUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;


public class Main2Activity extends BaseCallActivity {
    private static final String TAG = Main2Activity.class.getSimpleName();

    // Permission request when we want to go to next activity
    // when all necessary permissions are granted.
    private static final int PERMISSION_REQ_FORWARD = 1 << 4;

    // Permission request when we want to stay in
    // current activity even if all permissions are granted.
    private static final int PERMISSION_REQ_STAY = 1 << 3;

    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
    };
    private String id;
    private int count = 0;
    private LoadingDailog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        setIdentifier();
        checkPermissions();
//        RtcEngine.destroy();
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(Main2Activity.this)
                .setMessage("连接中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        loginVideo();
    }

    public void loginVideo() {
        rtmClient().logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "rtm client logout success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "rtm client logout failed:" + errorInfo.getErrorDescription());
            }
        });
        rtmClient().login(null, Util.getUserId(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.cancel();
                gotoDialerActivity();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                dialog.cancel();
                Toast.makeText(Main2Activity.this, "请重新登录", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onGlobalLayoutCompleted() {
    }

    private void setIdentifier() {
    }

    private void checkPermissions() {
        if (!permissionArrayGranted(null)) {
            requestPermissions(PERMISSION_REQ_STAY);
        }
    }

    private boolean permissionArrayGranted(@Nullable String[] permissions) {
        String[] permissionArray = permissions;
        if (permissionArray == null) {
            permissionArray = PERMISSIONS;
        }

        boolean granted = true;
        for (String per : permissionArray) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(int request) {
        ActivityCompat.requestPermissions(this, PERMISSIONS, request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_FORWARD ||
                requestCode == PERMISSION_REQ_STAY) {
            boolean granted = permissionArrayGranted(permissions);
            if (granted && requestCode == PERMISSION_REQ_FORWARD) {
                loginVideo();
            } else if (!granted) {
                toastNeedPermissions();
            }
        }
    }

    public void onStartCall(View view) {
        requestPermissions(PERMISSION_REQ_FORWARD);
    }

    private void toastNeedPermissions() {
        Toast.makeText(this, "需要权限", Toast.LENGTH_LONG).show();
    }

    public void setActivity(Main2Activity activity) {
        mActivity = activity;
    }

    @Override
    public RtmClient rtmClient() {
        return application().rtmClient();
    }

    Main2Activity mActivity;

    public void gotoDialerActivity() {
        String peer = id;
        Set<String> peerSet = new HashSet<>();
        peerSet.add(peer);
        rtmClient().queryPeersOnlineStatus(peerSet,
                new ResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onSuccess(Map<String, Boolean> statusMap) {
                        Boolean bOnline = statusMap.get(peer);
                        if (bOnline != null && bOnline) {
                            String uid = Util.getUserId();
                            String channel = RtcUtils.channelName(uid, peer);
                            gotoCallingInterface(peer, channel, Constants.ROLE_CALLER);
                            count++;
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Main2Activity.this,
                                            R.string.peer_not_online,
                                            Toast.LENGTH_SHORT).show();
                                    count++;
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        ToastUtil.showToast(Main2Activity.this, "该用户暂无账号");
                        count++;
                        finish();

                    }
                });
    }
}
