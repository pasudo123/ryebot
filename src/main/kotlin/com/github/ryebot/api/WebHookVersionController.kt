package com.github.ryebot.api

import com.github.ryebot.util.DateUtil.toISO8601Format
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("version")
class WebHookVersionController {

    @GetMapping
    fun version(): String {
        return """
            "version": "1.0.0",
            "now": ${LocalDateTime.now().toISO8601Format()}
        """.trimIndent()
    }
}
