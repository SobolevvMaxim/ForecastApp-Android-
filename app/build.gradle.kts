plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.hiltAndroidPlugin)
    id(BuildPlugins.navigationSafeArgs)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        applicationId = "com.example.homeworksandroid"
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:repository"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":common:extensions"))
    implementation(project(":common:features"))

//    implementation deps.lifecycle.viewmodel_ktx
//    implementation deps.lifecycle.livedata_ktx
    implementation(Libraries.viewmodel_ktx)
    implementation(Libraries.livedata_ktx)

//    implementation deps.hilt.android
//    kapt deps.hilt.compiler
    kapt(Libraries.hilt_compiler)
    implementation(Libraries.hilt_android)

//    implementation deps.room.runtime
//    kapt deps.room.compiler
    implementation(Libraries.room_runtime)
    kapt(Libraries.room_compiler)

//    implementation deps.gson.converter
//    implementation deps.retrofit.runtime
//    implementation deps.retrofit.adapter
//    implementation deps.okhttp_logging_interceptor
    implementation(Libraries.converter)
    implementation(Libraries.retrofit_runtime)
    implementation(Libraries.adapter)
    implementation(Libraries.okhttp_logging_interceptor)

//    implementation deps.kotlin.kotlinstd
//    implementation deps.kotlin.coroutines
    implementation(Libraries.kotlinstd)
    implementation(Libraries.coroutines)

//    implementation deps.navigation.fragment
//    implementation deps.navigation.ui_ktx
//    implementation deps.core_ktx
//    implementation deps.app_compat
//    implementation deps.activity.activity_ktx
//    implementation deps.fragment.runtime_ktx
//    implementation deps.material
//    implementation deps.constraint_layout
//    implementation deps.refresh_layout
//    debugImplementation deps.leak_canary
//    implementation deps.multidex
//    implementation deps.location
//    implementation deps.timber
    implementation(Libraries.fragment)
    implementation(Libraries.ui_ktx)
    implementation(Libraries.core_ktx)
    implementation(Libraries.app_compat)
    implementation(Libraries.activity_ktx)
    implementation(Libraries.runtime_ktx)
    implementation(Libraries.material)
    implementation(Libraries.constraint_layout)
    implementation(Libraries.refresh_layout)
    debugImplementation(Libraries.leak_canary)
    implementation(Libraries.multidex)
    implementation(Libraries.location)
    implementation(Libraries.timber)
}

kapt {
    correctErrorTypes = true
}