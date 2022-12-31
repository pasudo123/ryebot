package com.github.arch.option

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location

/**
 * config 패키지는 archUnit 임포트 옵션에서 무시한다.
 */
class DoNotConfigPackageOption : ImportOption {

    override fun includes(location: Location?): Boolean {
        if (location == null) {
            return false
        }

        return location.contains("config").not()
    }
}
