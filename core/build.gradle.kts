plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias (libs.plugins.serialization)
}
android {
    namespace = "com.vandrushko.core"
}

apply("$rootDir/base-module.gradle")




