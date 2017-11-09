# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Hari\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings
-keep class * {
    public private *;
}
-dontwarn org.apache.commons.logging.**

 -ignorewarnings
    -dontwarn org.apache.commons.**
    -keep class org.apache.http.** { *; }
    -dontwarn org.apache.http.**
    -keepattributes SourceFile,LineNumberTable
    -dontwarn java.nio.file.Files
    -dontwarn java.nio.file.Path
    -dontwarn java.nio.file.OpenOption
    -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -dontwarn android.support.design.**
    -keep class android.support.design.** { *; }
    -keep interface android.support.design.** { *; }
    -keep public class android.support.design.R$* { *; }

