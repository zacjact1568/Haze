plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    // KSP 版本，需根据 Kotlin 版本指定
    // https://github.com/google/ksp/releases
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}
