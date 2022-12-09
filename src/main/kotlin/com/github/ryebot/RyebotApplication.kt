package com.github.ryebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RyebotApplication

fun main(args: Array<String>) {
    runApplication<RyebotApplication>(*args)
}
