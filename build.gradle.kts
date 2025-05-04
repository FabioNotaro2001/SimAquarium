import org.gradle.configurationcache.extensions.capitalized

plugins {
    java
}

allprojects {
    apply<JavaPlugin>()

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    repositories {
        mavenCentral()
    }

    group = "it.unibo.ise"
}

subprojects {
    sourceSets {
        main {
            resources {
                srcDir("src/main/asl")
            }
        }
    }

    dependencies {
        implementation("io.github.jason-lang:jason-interpreter:3.2.1")
        testImplementation("junit", "junit", "4.13.2")
    }

    tasks.register<JavaExec>("runAquariumMas") {
            group = "run"
            classpath = sourceSets.getByName("main").runtimeClasspath
            mainClass.set("env.view.SimulationSettingsGUI")
            standardInput = System.`in`
            javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
        }
}
