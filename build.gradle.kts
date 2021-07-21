plugins {
    id("org.jetbrains.kotlin.js") version "1.5.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}

kotlin {
    js {
        browser {
        }
        binaries.executable()
    }
}
