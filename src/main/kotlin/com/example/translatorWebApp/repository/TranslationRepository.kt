package com.example.translatorWebApp.repository

import com.example.translatorWebApp.TranslationDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


@Repository
class TranslationRepository(@Autowired private val jdbcTemplate: JdbcTemplate) {

    fun saveTranslation(translationDTO: TranslationDTO) {
        jdbcTemplate.update(
            "INSERT INTO translations(ip,original_text,translated_text) VALUES (?,?,?)",
            translationDTO.ipAddress,
            translationDTO.originalText,
            translationDTO.translatedText
        )

    }


}