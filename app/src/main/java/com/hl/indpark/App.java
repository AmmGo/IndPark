package com.hl.indpark;



import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import com.hl.indpark.utils.SharePreferenceUtil;

/**
 * Created by arvinljw on 2018/11/22 16:37
 * Function：
 * Desc：
 */
public class App extends TinkerApplication {
    private static App app;
//    private ArticleDatabase db;

    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.hl.indpark.AppLike");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        app = this;

        AppCompatDelegate.setDefaultNightMode(SharePreferenceUtil.isDarkStyle() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

//        db = Room.databaseBuilder(this, ArticleDatabase.class, "wanandroid").build();
    }

    public static App getApp() {
        return app;
    }

//    public ArticleDatabase getDB() {
//        return db;
//    }
}
