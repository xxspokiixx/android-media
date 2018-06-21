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

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class com.tickaroo.tikxml.** { *; }
-keep class **$$TypeAdapter { *; }

-keepclasseswithmembernames class * {
    @com.tickaroo.tikxml.* <fields>;
}

-keepclasseswithmembernames class * {
    @com.tickaroo.tikxml.* <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-dontwarn knf.kuma.**
-keep public enum knf.kuma.**{*;}
-keep class es.munix.multidisplaycast.**{*;}
-keep class com.connectsdk.**{* ;}
-keepclassmembers public class * implements pl.droidsonroids.jspoon.ElementConverter {
   public <init>(...);
}
-keep public class * extends java.lang.Exception
-keeppackagenames org.jsoup.nodes
-dontwarn com.amazon.client.metrics.**
-dontwarn com.beloo.widget.chipslayoutmanager.**
-dontwarn com.squareup.okhttp.**
-dontwarn dagger.android.**
-dontwarn dagger.android.support.**
-dontwarn okhttp3.**
-dontwarn okhttp3.internal.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn com.dropbox.core.**
-dontwarn io.branch.**
-dontwarn com.bumptech.glide.**
