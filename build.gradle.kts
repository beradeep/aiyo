import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.lumo) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint)
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
        reporters {
            ReporterType.JSON
            ReporterType.CHECKSTYLE
        }
        disabledRules.set(
            listOf(
                "no-wildcard-imports",
                "filename",
                "enum-entry-name-case",
                "final-newline"
            )
        )
    }
}

tasks.register<Copy>("installPreCommitHook") {
    from(File(rootProject.rootDir, "scripts/git-hooks/pre-commit"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = "0777".toInt(8)
}

tasks.getByPath(":app:preBuild").dependsOn(":installPreCommitHook")