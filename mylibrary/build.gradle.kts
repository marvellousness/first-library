plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

val libraryVersion = "1.0.0"
val groupId = "nova.publish"
val artifactId = "mylibrary"
val githubRepo = "marvellousness/first-library"

android {
    namespace = "nova.publish.mylibrary"
    compileSdk = 36

    defaultConfig {
        minSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    publishing {
        singleVariant("release")
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = groupId
                artifactId = artifactId
                version = libraryVersion
                from(components["release"])
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                uri("https://maven.pkg.github.com/$githubRepo")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}