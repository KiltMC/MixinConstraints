plugins {
    java
    idea
    id("maven-publish")
}

version = "1.0.3"
group = "xyz.bluspring.kiltmc.mixinconstraints"

idea.module.isDownloadSources = true

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.spongepowered.org/maven") // provides lexforge
    maven("https://maven.neoforged.net/releases")
}

val fabric: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

/*val forge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val neoforge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}*/

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.jetbrains:annotations:24.1.0")

    "fabricImplementation"("net.fabricmc:fabric-loader:0.15.0")
}

tasks.jar {
    from(fabric.output)
    //from(forge.output)
    //from(neoforge.output)
}

tasks.register<Jar>("sourcesJar") {
    group = "build"
    archiveClassifier.set("sources")
    sourceSets.map { it.allSource }.forEach {
        from(it)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "xyz.bluspring.kiltmc"
            artifactId = "MixinConstraints"
        }
    }
    repositories {
        maven {
            url = uri("https://mvn.devos.one/snapshots")
            credentials {
                username = System.getenv()["MAVEN_USER"]
                password = System.getenv()["MAVEN_PASS"]
            }
        }
    }
}