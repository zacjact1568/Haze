import java.io.FileInputStream
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "net.zackzhang.code.haze"
    compileSdk = 34

    val secretProperties = Properties().apply {
        load(FileInputStream(rootProject.file("secret.properties")))
    }

    defaultConfig {
        applicationId = "net.zackzhang.code.haze"
        minSdk = 29
        targetSdk = 34

        val now = LocalDateTime.now()
        versionCode = Duration.between(
            LocalDateTime.of(2016, 5, 8, 0, 0),
            now
        ).toDays().toInt()
        versionName = DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .toFormatter().format(now)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
//                arguments += "room.schemaLocation" to "$projectDir/schemas"
            }
        }
        buildConfigField("String", "QWEATHER_PUBLIC_ID", "\"${secretProperties["qweather.publicId"]}\"")
        buildConfigField("String", "QWEATHER_KEY", "\"${secretProperties["qweather.key"]}\"")
    }

    signingConfigs {
        create("release") {
            keyAlias = secretProperties["android.signing.keyAlias"] as String
            keyPassword = secretProperties["android.signing.keystorePassword"] as String
            storeFile = file(secretProperties["android.signing.keystorePath"] as String)
            storePassword = secretProperties["android.signing.keyPassword"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        // 编译时生成 BuildConfig.java
        buildConfig = true
        viewBinding = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.airbnb.android:lottie:6.4.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.facebook.stetho:stetho:1.6.0")
    implementation("com.facebook.stetho:stetho-okhttp3:1.6.0")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    ksp("androidx.room:room-compiler:2.6.1")
}