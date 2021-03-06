#---------这里提供一份这个lib中最好不要混淆的地方，前边的配置都差不多，主要是第三方包以及其他不需要混淆的代码----
#---------------------------------基本指令以及一些固定不混淆的代码--开始--------------------------------

#<基本指令>
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#忽略警告
-ignorewarnings
#记录生成的日志数据,gradle build时在本项目根目录输出apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#</基本指令>
-keep public class com.hl.indpark.App{*;}
-keep public class com.hl.indpark.AppLike{*;}

#<基础>
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
#</基础>

#<view相关>
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
   public void *(android.view.View);
}
#</view相关>

#<Serializable、Parcelable>
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#</Serializable、Parcelable>

#<R文件>
-keep class **.R$* {
 *;
}
#</R文件>

#<enum>
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#</enum>

#<natvie>
-keepclasseswithmembernames class * {
    native <methods>;
}
#</natvie>

#---------------------------------基本指令以及一些固定不混淆的代码--结束-----------

#---------------------------------第三方包--开始-------------------------------

#<okhttp3.x>
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#</okhttp3.x>

#<retrofit2.x>
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
#</retrofit2.x>

# zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}

#</okhttp3.x>

#<eventbus 3.0>
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#</eventbus 3.0>

#<Gson>
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keep class com.hl.indpark.entities.** { *; }
#</Gson>

#<glide>
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#</glide>

-keep class net.arvin.permissionhelper.**{*;}
-keep class net.arvin.selector.**{*;}
-keep class net.arvin.itemdecorationhelper.**{*;}

#<BaseQuickAdapter>
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
#</BaseQuickAdapter>

-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}

-keep class com.youth.banner.** {*;}
-keep class com.facebook.shimmer.** {*;}
-keep class com.rengwuxian.materialedittext.** {*;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# tinker混淆规则
-dontwarn com.tencent.tinker.**
-keep class com.tencent.tinker.** { *; }
#agora
-keep class io.agora.**{*;}

#视频流
-keep class cn.nodemedia.**{*;}

#权限
-keep class org.zhx.common.bgstart.**{*;}

# androidx的混淆
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

# 极光推送混淆
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-keep class cn.jiguang.** { *; }
-keep class * extends cn.jpush.android.service.JPushMessageReceiver{*;}

  # 3D 地图 V5.0.0之前：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.amap.mapcore.*{*;}
    -keep   class com.amap.api.trace.**{*;}

   #  3D 地图 V5.0.0之后：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.**{*;}
    -keep   class com.amap.api.trace.**{*;}

   #  定位
    -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.loc.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}

   #  搜索
    -keep   class com.amap.api.services.**{*;}

 #    2D地图
    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}

  #   导航
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
#----------------------------------第三方包--结束--------------------------

#---------------------------------一些不要混淆的代码--开始-------------------

-keep class net.arvin.baselib.** { *; }
-keep class net.arvin.wanandroid.entities.** { *; }


#<反射>
-keep class net.arvin.wanandroid.uis.uihelpers.** { *; }
-keep class net.arvin.wanandroid.uis.adapters.** { *; }
#</反射>

#<js>

#</js>

#<自定义View的类>
#</自定义View的类>

#---------------------------------一些不要混淆的代码--结束-------------------

