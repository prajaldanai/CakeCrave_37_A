plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cakecrave"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cakecrave"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        compose = true
    }

    // ❌ REMOVED composeOptions
    // Kotlin 2.2.20 + Compose plugin handles this automatically

    // ===============================
    // QUIETER TEST OUTPUT
    // Show PASSED/FAILED per test + full details on failure only
    // ===============================
    testOptions {
        unitTests.all {
            it.testLogging {
                events("passed", "failed", "skipped")
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                showStandardStreams = false
            }
            it.afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
                if (desc.parent == null) {
                    println("\n========== TEST SUMMARY ==========")
                    println("Result : ${result.resultType}")
                    println("Tests  : ${result.testCount}")
                    println("Passed : ${result.successfulTestCount}")
                    println("Failed : ${result.failedTestCount}")
                    println("Skipped: ${result.skippedTestCount}")
                    println("==================================\n")
                }
            }))
        }
    }
}

dependencies {

    // ===============================
    // ANDROID CORE
    // ===============================
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.2")

    // ===============================
    // COMPOSE BOM (single source of truth)
    // ===============================
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")

    // ===============================
    // MATERIAL 2 (needed for RippleTheme)
    // ===============================
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material-ripple")

    // ===============================
    // MATERIAL 3
    // ===============================
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // ===============================
    // NAVIGATION
    // ===============================
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // ===============================
    // FIREBASE (NO STORAGE)
    // ===============================
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // ===============================
    // CLOUDINARY (IMAGE UPLOAD)
    // ===============================
    implementation("com.cloudinary:cloudinary-android:2.1.0")

    // ===============================
    // COIL 3 (IMAGE DISPLAY)
    // ===============================
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    // ===============================
    // GOOGLE SIGN-IN
    // ===============================
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // ===============================
    // DEBUG & TEST
    // ===============================
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

// ===============================
// CUSTOM TASK: run tests with minimal Gradle noise
// Usage: ./gradlew testQuiet
// ===============================
tasks.register("testQuiet") {
    group = "verification"
    description = "Runs unit tests with quiet Gradle output (PASS/FAIL + failures only)"
    dependsOn("testDebugUnitTest")
}

