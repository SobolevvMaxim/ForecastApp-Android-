plugins {
    id(BuildPlugins.ModulePlugins.androidLibrary)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.Kotlin.kapt)
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
    implementation(project(":domain"))

    implementation(Libraries.Room.room_runtime)
    kapt(Libraries.Room.room_compiler)

    kapt(Libraries.Hilt.hilt_compiler)
    implementation(Libraries.Hilt.hilt_android)

    implementation(Libraries.DataStore.core)
    implementation(Libraries.DataStore.preferences)
}