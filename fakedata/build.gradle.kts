plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
    id("maven-publish")
    id("java-library")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("java") {
                from(components.findByName("java"))
                groupId = "com.github.DanteAndroid"
                artifactId = "FakeData"
                version = "0.25"
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}