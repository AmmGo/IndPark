package com.hl.indpark.uis.activities.videoactivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hl.indpark.uis.activities.videoactivities.connectionservice.OpenDuoConnectionService;

import org.zhx.common.bgstart.library.impl.BgStart;

import java.util.List;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmMessage;

public abstract class BaseCallActivity extends BaseRtcActivity implements RtmChannelListener, ResultCallback<Void> {
    private static final String TAG = BaseCallActivity.class.getSimpleName();

    protected RtmCallManager mRtmCallManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRtmCallManager = rtmCallManager();
    }

    public void gotoCallingInterface(String peerUid, String channel, int role) {
        if (config().useSystemCallInterface()) {
            // Not supported yet.
            // placeSystemCall(String.valueOf(config().getUserId()), peerUid, channel);
        } else {
            gotoCallingActivity(channel, peerUid, role);
        }
    }

    private void placeSystemCall(String myUid, String peerUid, String channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Bundle extras = new Bundle();
            extras.putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL);

            Bundle extraBundle = new Bundle();
            extraBundle.putString(Constants.CS_KEY_UID, myUid);
            extraBundle.putString(Constants.CS_KEY_SUBSCRIBER, peerUid);
            extraBundle.putString(Constants.CS_KEY_CHANNEL, channel);
            extraBundle.putInt(Constants.CS_KEY_ROLE, Constants.CALL_ID_OUT);
            extras.putBundle(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, extraBundle);

            try {
                TelecomManager telecomManager = (TelecomManager)
                        getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
                PhoneAccount pa = telecomManager.getPhoneAccount(
                        config().getPhoneAccountOut().getAccountHandle());
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, pa.getAccountHandle());
                telecomManager.placeCall(Uri.fromParts(
                        OpenDuoConnectionService.SCHEME_AG, peerUid, null), extras);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    protected void gotoCallingActivity(String channel, String peer, int role) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(Constants.KEY_CALLING_CHANNEL, channel);
        intent.putExtra(Constants.KEY_CALLING_PEER, peer);
        intent.putExtra(Constants.KEY_CALLING_ROLE, role);
//        startActivity(intent);
//        Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        BgStart.getInstance().startActivity(this, intent, CallActivity.class.getName());
    }
//主动
    protected void inviteCall(final String peerUid, final String channel) {
        LocalInvitation invitation = mRtmCallManager.createLocalInvitation(peerUid);
        invitation.setContent(channel);
        invitation.setChannelId(channel);
        mRtmCallManager.sendLocalInvitation(invitation, this);
        global().setLocalInvitation(invitation);
    }
//被动
    protected void answerCall(final RemoteInvitation invitation) {
        if (mRtmCallManager != null && invitation != null) {
            mRtmCallManager.acceptRemoteInvitation(invitation, this);
        }
    }

    protected void cancelLocalInvitation() {
        if (mRtmCallManager != null && global().getLocalInvitation() != null) {
            mRtmCallManager.cancelLocalInvitation(global().getLocalInvitation(), this);
        }
    }

    protected void refuseRemoteInvitation(@NonNull RemoteInvitation invitation) {
        if (mRtmCallManager != null) {
            mRtmCallManager.refuseRemoteInvitation(invitation, this);
        }
    }

    @Override
    public void onMemberCountUpdated(int count) {

    }

    @Override
    public void onAttributesUpdated(List<RtmChannelAttribute> list) {

    }

    @Override
    public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberJoined(RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberLeft(RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onSuccess(Void aVoid) {

    }

    @Override
    public void onFailure(ErrorInfo errorInfo) {

    }

    @Override
    public void onLocalInvitationReceived(LocalInvitation localInvitation) {
        super.onLocalInvitationReceived(localInvitation);
    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation localInvitation, String response) {
        Log.i("BaseActivity", "onLocalInvitationAccepted by peer:" + localInvitation.getCalleeId());
        gotoVideoActivity(localInvitation.getChannelId(), localInvitation.getCalleeId());
    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation localInvitation, String response) {
        super.onLocalInvitationRefused(localInvitation, response);
    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation localInvitation) {
        super.onLocalInvitationCanceled(localInvitation);
    }

    @Override
    public void onLocalInvitationFailure(LocalInvitation localInvitation, int errorCode) {
        super.onLocalInvitationFailure(localInvitation, errorCode);
        Log.w("BaseActivity", "onLocalInvitationFailure:" + errorCode);
    }

    @Override
    public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {
        Log.i("BaseActivity", "onRemoteInvitationReceived from caller:" + remoteInvitation.getCallerId());
        global().setRemoteInvitation(remoteInvitation);//开始
        gotoCallingActivity(remoteInvitation.getChannelId(), remoteInvitation.getCallerId(), Constants.ROLE_CALLEE);
    }

    @Override
    public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {
        Log.i("BaseActivity", "onRemoteInvitationAccepted from caller:" + remoteInvitation.getCallerId());
        gotoVideoActivity(remoteInvitation.getChannelId(), remoteInvitation.getCallerId());
    }

    @Override
    public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {
        super.onRemoteInvitationRefused(remoteInvitation);
    }

    @Override
    public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {
        super.onRemoteInvitationCanceled(remoteInvitation);
    }

    @Override
    public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int errorCode) {
        super.onRemoteInvitationFailure(remoteInvitation, errorCode);
        Log.w("BaseActivity", "onRemoteInvitationFailure:" + errorCode);
    }

    public void gotoVideoActivity(String channel, String peer) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(Constants.KEY_CALLING_CHANNEL, channel);
        intent.putExtra(Constants.KEY_CALLING_PEER, peer);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
