package com.github.ryebot.application

/**
 * 액션타입 참고
 * https://docs.github.com/developers/webhooks-and-events/webhooks/webhook-events-and-payloads
 */
enum class WebHookEventType {
    OPENED,
    ;

    companion object {
        fun getCurrentActionOrNull(action: String): WebHookEventType? {
            return WebHookEventType.values().find { it.name.lowercase() == action }
        }
    }
}
