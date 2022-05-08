const val kotlinVersion = "1.6.10"

object ApiKey {
    const val API_KEY = "38b77b2ee349a9b3c7b01a4c19660ebb"
}

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.2.2"
        const val hiltVersion = "2.38.1"
        const val navigation = "2.4.2"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val hiltGradleAndroid = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val hiltAndroidPlugin = "dagger.hilt.android.plugin"
    const val androidLibrary = "com.android.library"
    const val navigationAndroid = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"

}

object AndroidSdk {
    const val minSdk = 16
    const val compileSdk = 31
    const val targetSdk = compileSdk
}

object Libraries {
    private object Versions {
        const val gradle = "7.0.2"
        const val kotlin_gradle = "1.6.10"
        const val activity = "1.3.1"
        const val appcompat = "1.3.1"
        const val kotlin_std = "1.5.21"
        const val constraint_layout = "2.1.0"
        const val core_ktx = "1.6.0"
        const val fragment = "1.3.6"
        const val material = "1.4.0"
        const val retrofit = "2.9.0"
        const val retrofit_coroutines = "0.9.2"
        const val interceptor = "4.9.0"
        const val room = "2.3.0"
        const val swiperefresh = "1.1.0"
        const val gson = "2.9.0"
        const val hilt = "2.38.1"
        const val junit = "4.13.2"
        const val junit_test = "1.1.3"
        const val coroutines = "1.3.9"
        const val navigation = "2.4.1"
        const val lifecycle = "2.4.0-alpha03"
        const val leak_canary = "2.9.1"
        const val multidex = "2.0.1"
        const val location = "19.0.1"
        const val datastore = "1.0.0"
        const val timber = "5.0.1"
    }


    const val activity_ktx = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary}"

    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"

    const val location = "com.google.android.gms:play-services-location:${Versions.location}"

    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"

    const val refresh_layout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefresh}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin_gradle_plugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_gradle}"
    const val hilt_plugin = ("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")

    const val core = "androidx.datastore:datastore-core:${Versions.datastore}"
    const val preferences = "androidx.datastore:datastore-preferences:${Versions.datastore}"

    const val runtime = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragment_runtime_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val testing = "androidx.fragment:fragment-testing:${Versions.fragment}"

    const val kotlinstd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_std}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val java8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val junit = "junit:junit:${Versions.junit}"
    const val junit_test = "androidx.test.ext:junit:${Versions.junit_test}"

    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"

    const val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val adapter =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofit_coroutines}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    const val converter = "com.squareup.retrofit2:converter-gson:${Versions.gson}"
}

//object TestLibraries {
//    private object Versions {
//        const val junit4 = "4.12"
//        const val testRunner = "1.1.0-alpha4"
//        const val espresso = "3.1.0-alpha4"
//    }
//    const val junit4     = "junit:junit:${Versions.junit4}"
//    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
//    const val espresso   = "androidx.test.espresso:espresso-core:${Versions.espresso}"
//}
