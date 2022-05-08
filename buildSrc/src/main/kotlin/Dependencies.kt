const val kotlinVersion = "1.6.10"

object ApiKey {
    const val API_KEY = "PutYourApiKeyHere"
}

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "4.2.2"
        const val hiltVersion = "2.38.1"
        const val navigation = "2.4.2"
    }

    object Kotlin {
        const val android = "kotlin-android"
        const val kapt = "kotlin-kapt"
    }

    object GradlePlugins {
        const val androidGradlePlugin =
            "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val hiltGradleAndroid =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    }

    object ModulePlugins {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
    }

    const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
    const val hiltAndroidPlugin = "dagger.hilt.android.plugin"
    const val navigationAndroid =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
}

object AndroidSdk {
    const val minSdk = 16
    const val compileSdk = 31
    const val targetSdk = compileSdk

    const val applicationID = "com.example.homeworksandroid"
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

    object UI {
        const val activity_ktx = "androidx.activity:activity-ktx:${Versions.activity}"
        const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
        const val app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val constraint_layout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
        const val refresh_layout =
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefresh}"
    }

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    }

    object DataStore {
        const val core = "androidx.datastore:datastore-core:${Versions.datastore}"
        const val preferences = "androidx.datastore:datastore-preferences:${Versions.datastore}"
    }

    object Fragment {
        const val runtime = "androidx.fragment:fragment:${Versions.fragment}"
        const val runtime_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
        const val testing = "androidx.fragment:fragment-testing:${Versions.fragment}"
    }

    object Kotlin {
        const val kotlinstd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_std}"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Lifecycle {
        const val runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val java8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
        const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
        const val viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    }

    object Room {
        const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
        const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    }

    object Hilt {
        const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    }

    object Retrofit {
        const val okhttp_logging_interceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"

        const val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val adapter =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofit_coroutines}"
    }

    const val material = "com.google.android.material:material:${Versions.material}"
    const val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary}"
    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    const val location = "com.google.android.gms:play-services-location:${Versions.location}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val converter = "com.squareup.retrofit2:converter-gson:${Versions.gson}"
}