package com.example.translatorWebApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class TranslatorWebAppApplication

fun main(args: Array<String>) {
	runApplication<TranslatorWebAppApplication>(*args)
}
