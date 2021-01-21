// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
val kotlin_version by extra("1.4.21")
    //
//    ext.kotlin_version = "1.4.10"
//    ext.kotlin_coroutines_version = "1.3.7"
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        maven("https://maven.google.com")


        flatDir {
            dirs("libs","../baselib/libs")
        }

    }
//    ext.kotlin_version = "1.4.10"
    dependencies {
        classpath ("com.android.tools.build:gradle:4.1.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")

        //kotlin
        classpath ("org.jetbrains.kotlin:kotlin-android-extensions:1.4.21")
        //数据库操作框架
        classpath ("org.greenrobot:greendao-gradle-plugin:3.3.0") // add plugin
        classpath ("com.google.gms:google-services:4.3.4")
    }

}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        maven("https://maven.google.com")

        flatDir {
            dirs("libs","../baselib/libs")
        }
//
//        flatDir {
//            dirs 'libs', '../baselib/libs'
//        }
    }

}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}