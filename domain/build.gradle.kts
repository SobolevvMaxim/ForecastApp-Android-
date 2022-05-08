plugins {
    id(BuildPlugins.ModulePlugins.androidLibrary)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.Kotlin.kapt)
    id(BuildPlugins.hiltAndroidPlugin)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)
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
    kapt(Libraries.Hilt.hilt_compiler)
    implementation(Libraries.Hilt.hilt_android)
    implementation(Libraries.Kotlin.coroutines)
}