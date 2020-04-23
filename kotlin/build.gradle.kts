import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "codedgame-kotlin"
version = "0.0.0"

val kotlinVersion = "1.3.72";

buildscript {
    val kotlinVersion = "1.3.72";
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

tasks.withType(Wrapper::class) {
    gradleVersion = "6.3"
}

subprojects {
    buildscript {
        repositories {
            jcenter()
            mavenCentral()
        }
    }

    apply {
        plugin("kotlin")
    }

    repositories {
        jcenter()
        mavenCentral()
    }

    tasks {
        withType(Test::class) {
            useJUnitPlatform()
        }

        withType(KotlinCompile::class) {
            kotlinOptions.jvmTarget="1.8"
        }
    }

    configure<KotlinJvmProjectExtension>{
        sourceSets["main"].kotlin.srcDirs("src")
        sourceSets["test"].kotlin.srcDirs("test")

        this.target {
            this.attributes
        }
    }

    dependencies {
        "implementation"(kotlin("stdlib", kotlinVersion))
        "testImplementation" ("org.junit.jupiter:junit-jupiter-api:5.6.2")
        "testImplementation" ("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    }
}