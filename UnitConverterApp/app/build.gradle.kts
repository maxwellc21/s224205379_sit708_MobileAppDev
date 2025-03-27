plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.unitconverterapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.unitconverterapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // Updated to Java 17 for modern support
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true  // Enables ViewBinding for cleaner UI code
    }
}

dependencies {
    // Core AndroidX libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Material Components for modern UI
    implementation("com.google.android.material:material:1.11.0")

    // CardView dependency
    implementation("androidx.cardview:cardview:1.0.0")

    // Activity KTX for better lifecycle management
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")

    // Android Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
