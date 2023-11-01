plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    // KSP 版本，需根据 Kotlin 版本指定
    // https://github.com/google/ksp/releases
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}
