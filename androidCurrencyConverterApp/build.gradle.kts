plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.currencyconverter.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.currencyconverter.android"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.1"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
    }
    flavorDimensions += listOf("pricing")

    productFlavors {
        create("free") {
            applicationIdSuffix = ".free"
            dimension = "pricing"
            //      resValue("string","app_name","ProductFlavors-Free")
        }
        create("premium") {
            applicationIdSuffix = ".premium"
            dimension = "pricing"
            //       resValue("string", "app_name", "ProductFlavors-Paid")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    val appcompat_version = "1.7.0"
    implementation("androidx.appcompat:appcompat:$appcompat_version")
    // For loading and tinting drawables on older versions of the platform
    implementation("androidx.appcompat:appcompat-resources:$appcompat_version")
}