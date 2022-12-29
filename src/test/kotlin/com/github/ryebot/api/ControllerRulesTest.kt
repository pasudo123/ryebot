package com.github.ryebot.api

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = ["com.github.ryebot.api", "com.github.ryebot.application", "com.github.ryebot.domain"])
internal class ControllerRulesTest {

    /**
     * importedClasses 패키지를 가지고 아래의 rule 을 검사한다.
     */
    @ArchTest
    fun `api 패키지는 ryebot api 패키지에서만 접근할 수 (있다)`(importedClasses: JavaClasses) {

        val rule = classes()
            .that().resideInAPackage("..api..")
            .should().onlyBeAccessed().byAnyPackage(
                "com.github.ryebot.api",
                "com.github.ryebot.api.model",
                "com.github.ryebot.api.model.detail",
            )

        rule.check(importedClasses)
    }

    @ArchTest
    fun `api 패키지는 ryebot application 패키지에서 접근할 수 (없다)`(importedClasses: JavaClasses) {

        val rule = noClasses()
            .that().resideInAPackage("..api..")
            .should().onlyBeAccessed().byAnyPackage(
                "com.github.ryebot.application"
            )

        rule.check(importedClasses)
    }

    @ArchTest
    fun `api 패키지는 ryebot domain 패키지에서 접근할 수 (없다)`(importedClasses: JavaClasses) {

        val rule = noClasses()
            .that().resideInAPackage("..api..")
            .should().onlyBeAccessed().byAnyPackage(
                "com.github.ryebot.domain"
            )

        rule.check(importedClasses)
    }
}
