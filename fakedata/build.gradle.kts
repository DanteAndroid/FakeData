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
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")
}