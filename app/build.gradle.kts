import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.devtools.ksp)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.ghost.finder.detector.radar.tracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ghost.finder.detector.radar.tracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 5
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        disable += "CustomSplashScreen"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "default"
    productFlavors {
        create("develop") {
            dimension = "default"
            applicationIdSuffix = ".dev"
            buildConfigField("boolean", "IS_TEST_AD", "true")
        }
        create("product") {
            dimension = "default"
            buildConfigField("boolean", "IS_TEST_AD", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        dataBinding = true
    }
    hilt {
        enableAggregatingTask = true
    }
    bundle {
        language {
            enableSplit = false
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.arsceneview)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.review.ktx)
    implementation(libs.androidx.preference.ktx)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-config")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.gms:play-services-ads:24.7.0")
    implementation("com.google.ads.mediation:facebook:6.20.0.1")
    implementation("com.google.ads.mediation:pangle:6.4.0.5.0")
    implementation("com.google.ads.mediation:applovin:13.0.1.0")
//
//    //fb sdk
    implementation("com.facebook.android:facebook-android-sdk:latest.release")
    implementation("com.facebook.android:audience-network-sdk:6.+")
    implementation("com.hataki.studio:ad-lib:1.0.2")
}