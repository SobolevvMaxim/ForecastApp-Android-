buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(BuildPlugins.GradlePlugins.androidGradlePlugin)
        classpath(BuildPlugins.GradlePlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.GradlePlugins.hiltGradleAndroid)
        classpath(BuildPlugins.navigationAndroid)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

tasks.register("clean").configure {
    delete("build")
}