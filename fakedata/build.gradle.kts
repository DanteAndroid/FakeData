plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.findByName("release"))
                groupId = "com.github.DanteAndroid"
                artifactId = "FakeData"
                version = "0.20"
            }
        }
    }
}