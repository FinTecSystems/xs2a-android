import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.gradleMavenPublish)
}

val versionName = providers.gradleProperty("versionName").getOrElse("LOCAL")

android {
    compileSdk = 34
    namespace = "com.fintecsystems.xs2awizard"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "VERSION", "\"$versionName\"")
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
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.browser)

    implementation(libs.android.volley)

    implementation(libs.coil)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.aesEverywhere)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    configure(AndroidSingleVariantLibrary(
        variant = "release",
        sourcesJar = false,
        publishJavadocJar = false
    ))

    coordinates(
        groupId = providers.gradleProperty("GROUP").get(),
        artifactId = providers.gradleProperty("POM_ARTIFACT_ID").get(),
        version = versionName
    )

    pom {
        organization {
            name = providers.gradleProperty("POM_ORGANIZATION_NAME").get()
            url = providers.gradleProperty("POM_ORGANIZATION_URL").get()
        }
    }

    signAllPublications()
}
