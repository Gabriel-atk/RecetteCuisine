// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "8.6.0" apply false
    id ("com.android.library") version "8.6.0" apply false
    id ("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id ("org.jetbrains.kotlin.kapt") version "2.0.21" apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath (libs.realm.gradle.plugin)
    }
}
