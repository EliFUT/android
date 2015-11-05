# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/felipe_lima/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontusemixedcaseclassnames
-dontoptimize
-dontpreverify
-dontobfuscate
-dontpreverify
-dontwarn com.google.common.**
-dontwarn com.google.auto.**
-dontwarn sun.misc.**
-dontwarn android.support.**
-dontwarn com.squareup.javapoet.**
-dontwarn icepick.**
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.**
-dontwarn javax.tools.**
-dontwarn java.awt.**
-dontwarn java.beans.**
-dontwarn java.lang.invoke.**
-dontwarn rx.internal.util.unsafe.**
-dontwarn com.squareup.okhttp.**
-dontwarn autovalue.shaded.com.google.common$.auto.service.processor.$AutoServiceProcessor
-keep class **$$Icepick { *; }
-keep public class * extends android.view.View
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote libcore.icu.ICU
-dontnote sun.misc.Unsafe
-dontnote java.util.Optional
-dontnote com.android.org.conscrypt.OpenSSLSocketImpl
-dontnote org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl
-dontnote com.google.appengine.api.ThreadManager
-dontnote com.google.apphosting.api.ApiProxy
-dontnote javax.lang.model.type.IntersectionType
-dontnote android.graphics.Insets
-keepattributes Exceptions, Signature, InnerClasses, EnclosingMethod
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
    *;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keep,includedescriptorclasses class retrofit.** { *; }
-keep,includedescriptorclasses class android.support.** { *; }
-keep,includedescriptorclasses class com.felipecsl.elifut.widget.**
-keep,includedescriptorclasses class com.felipecsl.elifut.models.**
-keep,includedescriptorclasses class com.squareup.moshi.** { *; }
-keepattributes *Annotation*
-keepclassmembers class ** { ** base; }
-keepclassmembers class ** { ** value; }
-keepclassmembers class ** { ** icon; }
-keepclassmembers class ** { ** title; }
-keepclassmembers class ** { ** theUnsafe; }
-keepclassmembers class ** { ** busy; }
-keepclassmembers class ** { ** actionIntent; }
-keepclassmembers class ** { ** SDK_INT; }
-dontwarn retrofit.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# Retrolambda
-dontwarn java.lang.invoke.*

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}