plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.cryptowallet"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cryptowallet"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val coinMarketCapApiKey = rootProject.ext.properties["COINMARKETCAP_API_KEY"]?.toString()
            ?: providers.gradleProperty("COINMARKETCAP_API_KEY").orNull
            ?: System.getenv("COINMARKETCAP_API_KEY") 
            ?: "\"MISSING_API_KEY\""
        
        val formattedApiKey = if (!coinMarketCapApiKey.startsWith("\"") && !coinMarketCapApiKey.endsWith("\"")) {
            "\"$coinMarketCapApiKey\""
        } else {
            coinMarketCapApiKey
        }
        
        buildConfigField("String", "COINMARKETCAP_API_KEY", formattedApiKey)
        
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    
    // Generate Binding classes for layout XML files
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10")
    
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // We're using SharedPreferences instead of Room
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}