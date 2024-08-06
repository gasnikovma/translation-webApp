package com.example.translatorWebApp.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class TranslationRepository(@Autowired private val jdbcTemplate: JdbcTemplate) {

    fun saveTranslation(ipAddress: String, originalText: String, translatedText: String) {
        jdbcTemplate.update(
            "INSERT INTO translations(ip,original_text,translated_text) VALUES (?,?,?)",
            ipAddress,
            originalText,
            translatedText
        )

    }


}