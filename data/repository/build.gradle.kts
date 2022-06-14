plugins {
    id(BuildPlugins.ModulePlugins.androidLibrary)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.Kotlin.kapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.hiltAndroidPlugin)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)

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
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":common:extensions"))

    implementation(Libraries.Kotlin.kotlinstd)
    implementation(Libraries.Kotlin.coroutines)
    implementation(Libraries.UI.core_ktx)
    implementation(Libraries.Retrofit.adapter)

    kapt(Libraries.Hilt.hilt_compiler)
    implementation(Libraries.Hilt.hilt_android)
}