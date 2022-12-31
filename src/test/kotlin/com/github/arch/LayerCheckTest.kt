package com.github.arch

import com.github.arch.option.DoNotConfigPackageOption
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(
    packages = ["com.github.ryebot"],
    importOptions = [
        ImportOption.DoNotIncludeTests::class,
        ImportOption.DoNotIncludeJars::class,
        ImportOption.DoNotIncludeArchives::class,
        DoNotConfigPackageOption::class,
    ]
)
class LayerCheckTest {

    @ArchTest
    fun `레이어간 의존성을 확인한다`(importedClasses: JavaClasses) {

        val layerArch = layeredArchitecture()
            .consideringAllDependencies()
            // 레이어
            .layer("Api").definedBy("..api..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .layer("Client").definedBy("..client", "..client.(error|model)..")
            .layer("Repository").definedBy("..repository..")
            // 의존관계
            .whereLayer("Api").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Api")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Client").mayOnlyBeAccessedByLayers("Domain")

        layerArch.check(importedClasses)
    }
}
