plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
    id("maven-publish")
}

ksp {
    arg("withIndex", "true")
    arg("randomSuffix", "10")
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
//                groupId = "com.github.DanteAndroid"
//                artifactId = "FakeData"
//                version = "0.1"
//
//                pom {
//                    name.value("FakeData")
//                    description.value("Make faking data easy.")
//                    url.value("https://github.com/DanteAndroid/FakeData")
//
//                    licenses {
//                        license {
//                            //协议类型
//                            name.value("The MIT License")
//                            url.value("https://github.com/DanteAndroid/FakeData/blob/master/LICENSE")
//                        }
//                    }
//
//                    developers {
//                        developer {
//                            id.value("DanteAndroid")
//                            name.value("DanteAndroid")
//                            email.value("danteandroi@gmail.com")
//                        }
//                    }
//
//                    scm {
//                        connection.value("scm:git@github.com/DanteAndroid/FakeData.git")
//                        developerConnection.value("scm:git@github.com/DanteAndroid/FakeData.git")
//                        url.value("https://github.com/DanteAndroid/FakeData")
//                    }
//                }
            }
        }
    }
}