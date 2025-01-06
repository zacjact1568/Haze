import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Properties

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "net.zackzhang.code.haze"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    val config = Properties().apply {
        project.file("config.properties").inputStream().use(::load)
    }

    defaultConfig {
        applicationId = "net.zackzhang.code.haze"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()

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
        buildConfigField("String", "QWEATHER_PUBLIC_ID", "\"${config["qweather.publicId"]}\"")
        buildConfigField("String", "QWEATHER_KEY", "\"${config["qweather.key"]}\"")
    }
    signingConfigs {
        create("release") {
            keyAlias = config["android.signing.keyAlias"] as String
            keyPassword = config["android.signing.keystorePassword"] as String
            storeFile = file(config["android.signing.keystorePath"] as String)
            storePassword = config["android.signing.keyPassword"] as String
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

    implementation(project(":util"))

    implementation(platform(libs.kotlin))
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation(libs.recyclerview)
    implementation(libs.preference.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.core.ktx)
    implementation(libs.datastore.preferences)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.lottie)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.stetho)
    implementation(libs.stetho.okhttp3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.test)
    androidTestImplementation(libs.espresso)
}