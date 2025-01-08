import com.android.build.gradle.internal.ide.kmp.KotlinAndroidSourceSetMarker.Companion.android
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

group = "cz.kudladev"
version = "1.0.4"

kotlin {
    androidTarget {
        publishLibraryVariants("release","debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach{ iosTarget ->
        iosTarget.binaries.framework {
            baseName = "datetimepicker"
            isStatic = true
        }
    }

    sourceSets {
        val androidMain by getting {
            dependencies {

            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.kotlinx.datetime)
            }
        }
        val iosMain by creating {
            dependencies {
                // put your iOS dependencies here
            }
        }

    }
}

android {
    namespace = "io.github.kudl1k"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "datetimepicker-kmp", version.toString())

    pom {
        name = "Date Time Picker KMP"
        description = "A library for date and time pickers for Kotlin Multiplatform"
        inceptionYear = "2024"
        url = "https://github.com/Kudl1k/datetimepicker-kmp"
        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/license/mit"
            }
        }
        developers {
            developer {
                id = "kudl1k"
                name = "Štěpán Kudláček"
                url = "https://kudladev.cz/"
            }
        }
        scm {
            url = "https://github.com/Kudl1k/datetimepicker-kmp"
        }
    }
}