# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 忽略警告
-ignorewarnings

#js调用
#-keep public class net.hkfinance.xiangzheng.web.JsCallJavaObj {
#    public void showBigImg(*);
#}

# 关掉log日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

-keep class org.simpleframework.**{public protected private *; }
-keep class org.simpleframework.xml.**{ public protected private *; }
-keep class org.simpleframework.xml.core.**{ public protected private *; }
-keep class org.simpleframework.xml.util.**{ public protected private *; }
-keep interface org.simpleframework.xml.** {public protected private *;}
-keep interface org.simpleframework.xml.core.** {public protected private *;}
-keep interface org.simpleframework.xml.util.** {public protected private *;}

-dontwarn javax.xml.stream.**

-dontwarn org.joda.convert.**

-dontwarn net.java.truelicense.obfuscate.**

-dontwarn javax.annotation.concurrent.ThreadSafe

-keep public class com.mfinance.everjoy.hungkee.xml.** {
  public *;
  private *;
  protected *;
}

# eventbus============================================
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# sina============================================
-keep class com.sina.weibo.sdk.** { *; }


#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*

# 音视频播放
-keep class com.ywl5320.wlmedia.* {*;}