plugins {
//    id 'com.android.library'
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
//    id 'org.jetbrains.kotlin.android'
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":domain"))
    implementation(project(":data:remote"))
    implementation(project(":data:local"))

    implementation(Libraries.core_ktx)
    implementation(Libraries.app_compat)
    implementation(Libraries.material)
    implementation(Libraries.location)
    implementation(Libraries.timber)
}