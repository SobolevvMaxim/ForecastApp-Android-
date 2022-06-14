plugins {
    id(BuildPlugins.ModulePlugins.androidApplication)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.Kotlin.kapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.hiltAndroidPlugin)
    id(BuildPlugins.navigationSafeArgs)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        applicationId = AndroidSdk.applicationID
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation(Libraries.Lifecycle.viewmodel_ktx)
    implementation(Libraries.Lifecycle.livedata_ktx)
    implementation(Libraries.Lifecycle.runtime_ktx)

    kapt(Libraries.Hilt.hilt_compiler)
    implementation(Libraries.Hilt.hilt_android)

    implementation(Libraries.Room.room_runtime)
    kapt(Libraries.Room.room_compiler)

    implementation(Libraries.converter)
    implementation(Libraries.Retrofit.retrofit_runtime)
    implementation(Libraries.Retrofit.adapter)
    implementation(Libraries.Retrofit.okhttp_logging_interceptor)

    implementation(Libraries.Kotlin.kotlinstd)
    implementation(Libraries.Kotlin.coroutines)

    implementation(Libraries.Navigation.fragment)
    implementation(Libraries.Navigation.ui_ktx)

    implementation(Libraries.UI.core_ktx)
    implementation(Libraries.UI.app_compat)
    implementation(Libraries.UI.activity_ktx)
    implementation(Libraries.UI.constraint_layout)
    implementation(Libraries.UI.refresh_layout)

    implementation(Libraries.material)
    implementation(Libraries.multidex)
    implementation(Libraries.location)
    implementation(Libraries.timber)
    debugImplementation(Libraries.leak_canary)
}

kapt {
    correctErrorTypes = true
}