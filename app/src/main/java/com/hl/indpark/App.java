package com.hl.indpark;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.hl.indpark.uis.activities.videoactivities.agora.Config;
import com.hl.indpark.uis.activities.videoactivities.agora.EngineEventListener;
import com.hl.indpark.uis.activities.videoactivities.agora.Global;
import com.hl.indpark.uis.activities.videoactivities.agora.IEventListener;
import com.hl.indpark.uis.activities.videoactivities.utils.FileUtil;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;

/**
 * Created by arvinljw on 2018/11/22 16:37
 * Function：
 * Desc：
 */
public class App extends TinkerApplication {
    private static final String TAG = "fdsa";
    private static App app;
//    private ArticleDatabase db;
private static App instance;

    public static App getInstance() {
        return instance;
    }
    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.hl.indpark.AppLike");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init1();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
        instance = this;
        app = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        AppCompatDelegate.setDefaultNightMode(SharePreferenceUtil.isDarkStyle() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

//        db = Room.databaseBuilder(this, ArticleDatabase.class, "wanandroid").build();
    }

    static{
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.primary, R.color.text);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    public static App getApp() {
        return app;
    }

//    public ArticleDatabase getDB() {
//        return db;
//    }
private void init1() {
    initConfig();
    initEngine();
}
    private RtcEngine mRtcEngine;
    private RtmClient mRtmClient;
    private RtmCallManager rtmCallManager;
    private EngineEventListener mEventListener;
    private Config mConfig;
    private Global mGlobal;
    private void initConfig() {
        mConfig = new Config(getApplicationContext());
        mGlobal = new Global();
    }

    private void initEngine() {
        String appId = getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventListener = new EngineEventListener();
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), appId, mEventListener);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableDualStreamMode(true);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));

            mRtmClient = RtmClient.createInstance(getApplicationContext(), appId, mEventListener);
            mRtmClient.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));

            if (Config.DEBUG) {
                mRtcEngine.setParameters("{\"rtc.log_filter\":65535}");
                mRtmClient.setParameters("{\"rtm.log_filter\":65535}");
            }

            rtmCallManager = mRtmClient.getRtmCallManager();
            rtmCallManager.setEventListener(mEventListener);

            // By default do not use rtm token
//            mRtmClient.login(null, mConfig.getUserId(), new ResultCallback<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.i(TAG, "rtm client login success");
//                }
//
//                @Override
//                public void onFailure(ErrorInfo errorInfo) {
//                    Log.i(TAG, "rtm client login failed:" + errorInfo.getErrorDescription());
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public RtmClient rtmClient() {
        return mRtmClient;
    }

    public void registerEventListener(IEventListener listener) {
        mEventListener.registerEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        mEventListener.removeEventListener(listener);
    }

    public RtmCallManager rtmCallManager() {
        return rtmCallManager;
    }

    public Config config() {
        return mConfig;
    }

    public Global global() {
        return mGlobal;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        destroyEngine();
    }

    private void destroyEngine() {
        RtcEngine.destroy();

        mRtmClient.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "rtm client logout success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "rtm client logout failed:" + errorInfo.getErrorDescription());
            }
        });
    }
    public static Map<String, Activity> activityMap = new HashMap<String, Activity>();
    //收集创建的Activity
    public static void putActivityInfoToMap(Activity activity) {
        if (activity != null) {
            String activityName = activity.getClass().getSimpleName();
            Log.i("info", "putActivity--->" + activityName);

            activityMap.put(activityName, activity);
        }
    }

    //移除activity
    public static void removeActivityInfoFromMap(Activity activity) {
        if (activity != null) {
            String activityName = activity.getClass().getSimpleName();
            Log.i("info", "removeActivity--->" + activityName);
            if (activityMap.containsKey(activityName)) {
                activityMap.remove(activityName);
            }
        }
    }

    //关闭所有界面
    public static void closeAllActivityByMap() {
        if (!activityMap.isEmpty()) {
            Collection<Activity> activities = activityMap.values();
            Iterator<Activity> it = activities.iterator();
            while (it.hasNext()) {
                Activity activity = it.next();
                String activityName = activity.getClass().getSimpleName();

                Log.i("info", "removeActivity--->" + activityName);

                activity.finish();
            }
        }

    }
}

