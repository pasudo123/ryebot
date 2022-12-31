package com.github.arch

import com.github.arch.option.DoNotConfigPackageOption
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@AnalyzeClasses(
    packages = ["com.github.ryebot"],
    importOptions = [
        ImportOption.DoNotIncludeTests::class,
        ImportOption.DoNotIncludeJars::class,
        ImportOption.DoNotIncludeArchives::class,
        DoNotConfigPackageOption::class,
    ]
)
class CycleCheckTest {

    /**
     * bean 의 서로간 의존성 호출 뿐만 아니라, 계층간 재 호출 여부도 확인이 가능.
     */
    @ArchTest
    fun `패키지 내 사이클이 존재하는지 확인한다`(importClasses: JavaClasses) {

        /**
         * 괄호로 감싼 부분을 namingSlices 인자로 줄 수 있음
         */
        val layerCycleArch = slices()
            .matching("..(ryebot).(*)..").namingSlices("$2 of $1")
            .should().beFreeOfCycles()

        layerCycleArch.check(importClasses)
    }
}
