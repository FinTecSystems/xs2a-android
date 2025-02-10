plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinParcelize)
    `maven-publish`
    signing
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

publishing {
    publishing {
        publications {
            create<MavenPublication>("xs2awizard") {
                val pomArtifactId = providers.gradleProperty("POM_ARTIFACT_ID").get()
                groupId = providers.gradleProperty("GROUP").get()
                artifactId = pomArtifactId
                version = versionName

                artifact(layout.buildDirectory.file("outputs/aar/$pomArtifactId-release.aar"))

                pom {
                    name = providers.gradleProperty("POM_NAME").get()
                    description = providers.gradleProperty("POM_DESCRIPTION").get()
                    url = providers.gradleProperty("POM_URL").get()
                    packaging = providers.gradleProperty("POM_PACKAGING").get()

                    licenses {
                        license {
                            name = providers.gradleProperty("POM_LICENCE_NAME").get()
                            url = providers.gradleProperty("POM_LICENCE_URL").get()
                            distribution = providers.gradleProperty("POM_LICENCE_DIST").get()
                        }
                    }

                    developers {
                        developer {
                            id = providers.gradleProperty("POM_DEVELOPER_ID").get()
                            name = providers.gradleProperty("POM_DEVELOPER_NAME").get()
                            email = providers.gradleProperty("POM_DEVELOPER_EMAIL").get()
                        }
                    }

                    organization {
                        name = providers.gradleProperty("POM_ORGANIZATION_NAME").get()
                        url = providers.gradleProperty("POM_ORGANIZATION_URL").get()
                    }

                    scm {
                        connection = providers.gradleProperty("POM_SCM_CONNECTION").get()
                        developerConnection =
                            providers.gradleProperty("POM_SCM_DEV_CONNECTION").get()
                        url = providers.gradleProperty("POM_SCM_URL").get()
                    }
                }

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.getByName("implementation").allDependencies.forEach { dependency ->
                        if (dependency is ModuleDependency) {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dependency.group)
                            dependencyNode.appendNode("artifactId", dependency.name)
                            dependencyNode.appendNode("version", dependency.version)
                            dependencyNode.appendNode("scope", "runtime")
                        }
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = providers.gradleProperty("OSSRH_STAGING_NAME").get()
            url = uri(providers.gradleProperty("OSSRH_STAGING_URL").get())
            credentials {
                username = providers.gradleProperty("OSSRH_USER").getOrElse("")
                password = providers.gradleProperty("OSSRH_PASSWORD").getOrElse("")
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
