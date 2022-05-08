plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
//    id(BuildPlugins.hiltAndroidPlugin)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)
//        versionCode = 1
//        versionName = "1.0"

//        consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    kapt(Libraries.hilt_compiler)
    implementation(Libraries.hilt_android)
    implementation(Libraries.coroutines)
}