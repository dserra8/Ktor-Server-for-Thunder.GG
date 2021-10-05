
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val API_KEY: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.30"
}

group = "com.example"
version = "0.0.1"

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")

    }
}


application {
    mainClass.set("com.example.ApplicationKt")
}

sourceSets.main {
    java.srcDirs("src")
    resources.srcDirs("resources")
    extra["KEY"] = API_KEY
}

sourceSets.test {
    java.srcDirs("test")
    resources.srcDirs("resources")
}


repositories {
    mavenCentral()
    jcenter()
    maven {
        url =  uri("https://kotlin.bintray.com/ktor")
    }
    maven {
        url =  uri("https://kotlin.bintray.com/kotlin-js-wrappers")
    }

}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {


    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.122-kotlin-1.4.10")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("org.litote.kmongo:kmongo:4.2.8")
    implementation("org.litote.kmongo:kmongo-coroutine:4.2.8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
    implementation("commons-codec:commons-codec:1.15")
    implementation("io.ktor:ktor-network-tls:$ktor_version")
    implementation("io.ktor:ktor-freemarker:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
}