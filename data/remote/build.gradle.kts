import ApiKey.API_KEY

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

        buildConfigString("API_KEY", API_KEY)
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

    implementation(Libraries.Retrofit.retrofit_runtime)
    implementation(Libraries.Retrofit.adapter)
    implementation(Libraries.Retrofit.okhttp_logging_interceptor)

    kapt(Libraries.Hilt.hilt_compiler)
    implementation(Libraries.Hilt.hilt_android)

    implementation(Libraries.converter)
    implementation(Libraries.Kotlin.kotlinstd)
}

fun com.android.build.api.dsl.BaseFlavor.buildConfigString(name: String, value: String) =
    buildConfigField("String", name, "\"$value\"")