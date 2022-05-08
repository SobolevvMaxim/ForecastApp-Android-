import ApiKey.API_KEY

plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
}

android {
    compileSdkVersion(AndroidSdk.compileSdk)

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdk)
        targetSdkVersion(AndroidSdk.targetSdk)

//        consumerProguardFiles "consumer-rules.pro"
//        buildConfigField("String", "API_KEY", API_KEY) todo
        buildConfigString("API_KEY", API_KEY)
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

    implementation(Libraries.converter)
    implementation(Libraries.retrofit_runtime)
    implementation(Libraries.adapter)
    implementation(Libraries.okhttp_logging_interceptor)
    implementation(Libraries.kotlinstd)

    kapt(Libraries.hilt_compiler)
    implementation(Libraries.hilt_android)
}

fun com.android.build.api.dsl.BaseFlavor.buildConfigString(name: String, value: String) =
    buildConfigField("String", name, "\"$value\"")