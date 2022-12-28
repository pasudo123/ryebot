package com.github.ryebot.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    private val ISO_8601_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss'Z'")

    /**
     * YYYY-MM-dd'T'HH:mm:ss'Z'
     */
    fun LocalDateTime.toISO8601Format(): String {
        return this.format(ISO_8601_FORMAT)
    }
}
