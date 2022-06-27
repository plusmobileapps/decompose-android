buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.Jetbrains.kotlinGradlePlugin)
        classpath(Deps.Android.androidGradlePlugin)
        classpath(Deps.SqlDelight.gradlePlugin)
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}