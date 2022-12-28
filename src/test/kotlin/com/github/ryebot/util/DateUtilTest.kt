package com.github.ryebot.util

import com.github.ryebot.util.DateUtil.toISO8601Format
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class DateUtilTest {

    @Test
    @DisplayName("ISO8601 날짜/시간 확인")
    fun toISO8601FormatTest() {

        val now = LocalDateTime.of(2022, 12, 28, 9, 14, 55)

        now.toISO8601Format() shouldBe "2022-12-28T09:14:55Z"
    }
}
