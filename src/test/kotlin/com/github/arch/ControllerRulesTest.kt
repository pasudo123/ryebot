package com.github.arch

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

@AnalyzeClasses(
    packages = ["com.github.ryebot"],
    importOptions = [DoNotIncludeTests::class]
)
internal class ControllerRulesTest {

    @ArchTest
    fun `api 패키지는 application 패키지에 접근할 수 있다`(importedClasses: JavaClasses) {

        val rule = classes()
            .that().resideInAPackage("com.github.ryebot.api")
            .should().onlyHaveDependentClassesThat().resideInAnyPackage(
                "..apsi..",
                "..application..",
                "..sme.."
            )

        rule.check(importedClasses)
    }
}
