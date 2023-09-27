plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
    `maven-publish`
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
                version = "0.19"
            }
        }
    }
}