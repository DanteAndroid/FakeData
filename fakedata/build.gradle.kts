plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
}

ksp {
    arg("withIndex", "true")
    arg("randomSuffix", "10")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.11")
}