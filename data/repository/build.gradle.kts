plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.hiltAndroidPlugin)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles "consumer-rules.pro"
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
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":common:extensions"))

    implementation(Libraries.kotlinstd)
    implementation(Libraries.coroutines)
    implementation(Libraries.adapter)

    kapt(Libraries.hilt_compiler)
    implementation(Libraries.hilt_android)

    implementation(Libraries.core_ktx)
}