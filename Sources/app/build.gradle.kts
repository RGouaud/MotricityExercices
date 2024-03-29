import java.util.Properties

plugins {
    id("com.android.application")
    id("org.sonarqube") version "3.0"
}


android {
    namespace = "com.example.ex_motricite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ex_motricite"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    packagingOptions {
        exclude("META-INF/LICENSE-notice.md")
        exclude("META-INF/LICENSE.md")
    }
}

val localProperties = Properties()
localProperties.load(File("local.properties").inputStream())

dependencies {
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.quickbirdstudios:opencv:4.5.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test:core:1.5.0")
    implementation("androidx.test.ext:junit:1.1.5")

    androidTestImplementation("androidx.test:runner:1.5.2")
    // junit 5 dependencies
    testImplementation ("org.junit.jupiter:junit-jupiter-params:5.10.0");
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1");
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.4.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0");

    /*implementation(fileTree(mapOf("dir" to localProperties.getProperty("sdk.dir") + "/platforms/android-34",
            "include" to listOf("*.aar", "*.jar"),
            "exclude" to listOf("")
    )))*/
}

sonarqube {
    properties {
        property("sonar.sources", ".")
        property("sonar.exclusions", "build/**, **/test/**, **/androidTest/**")
    }
}
