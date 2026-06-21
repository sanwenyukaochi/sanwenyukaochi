import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.java.GoogleJavaFormatStep

plugins {
    id("com.diffplug.spotless") version "8.7.0"
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.sanwenyukaochi"
    version = "0.0.1-SNAPSHOT"
    pluginManager.apply("com.diffplug.spotless")

    pluginManager.withPlugin("com.diffplug.spotless") {
        extensions.configure<SpotlessExtension> {
            encoding("UTF-8")
            java {
                target("**/*.java")
                forbidWildcardImports()
                forbidModuleImports()
                googleJavaFormat(GoogleJavaFormatStep.defaultVersion())
                    .aosp()
                    .reflowLongStrings(false)
                    .formatJavadoc(true)
                    .reorderImports(true)
                    .groupArtifact(GoogleJavaFormatStep.defaultGroupArtifact())
                importOrder()
                removeUnusedImports()
                formatAnnotations()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            kotlin {
                target("**/*.kt")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            kotlinGradle {
                target("**/*.gradle.kts")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            gherkin {
                target("**/*.feature")
                gherkinUtils()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            toml {
                target("**/*.toml")
                versionCatalog()
                    .stripQuotedKeys(true)
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            json {
                target("**/*.json")
                gson()
                    .indentWithSpaces(4)
                    .sortByKeys()
                    .escapeHtml()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }
        }
    }
}

subprojects {
}
