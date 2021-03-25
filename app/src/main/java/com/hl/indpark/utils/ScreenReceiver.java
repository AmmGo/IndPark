package com.hl.indpark.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hl.indpark.uis.activities.ProtectActivity;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("save", "锁屏");
            Intent intentNew = new Intent(context, ProtectActivity.class);
            intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intentNew);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.e("save", "解锁");
            ProtectActivity protectActivity = ProtectActivity.weakReference != null ? ProtectActivity.weakReference.get() : null;
            protectActivity.finish();
        }
    }

}
