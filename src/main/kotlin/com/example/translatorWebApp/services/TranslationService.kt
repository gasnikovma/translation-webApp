package com.example.translatorWebApp.services

import com.example.translatorWebApp.TranslationDTO
import com.example.translatorWebApp.clients.TranslatorClient
import com.example.translatorWebApp.exceptions.BadRequestException
import com.example.translatorWebApp.models.request.TranslationRequest
import com.example.translatorWebApp.models.response.TranslationsListResponse
import com.example.translatorWebApp.repository.TranslationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.concurrent.Executors

@Service
class TranslationService(
    @Autowired private val translatorClient: TranslatorClient,
    @Autowired private val translationRepository: TranslationRepository
) {
    private val executorService = Executors.newFixedThreadPool(10)
    fun translate(translationRequest: TranslationRequest, userIp: String): String {
        val words = translationRequest.texts.split(" ")
        val translations = words.map { word ->
            executorService.submit<TranslationsListResponse> {
               val f =  translatorClient.translate(
                    TranslationRequest(
                        texts = word,
                        sourceLang = translationRequest.sourceLang,
                        targetLanguageCode = translationRequest.targetLanguageCode
                    )
                )
                f.body
            }
        }.map { it.get() }
        val translatedText = translations.joinToString(" ") { it.translations[0].text }
        translationRepository.saveTranslation(TranslationDTO(userIp, translationRequest.texts, translatedText))
        return translatedText
    }

    fun handleTranslationError(e: Exception): String {
        when (e) {
            is HttpClientErrorException -> when (e.statusCode) {
                HttpStatus.BAD_REQUEST -> throw BadRequestException("Invalid request: ${e.responseBodyAsString}")
                HttpStatus.UNAUTHORIZED -> throw BadRequestException("Unauthorized: ${e.responseBodyAsString}")
                HttpStatus .FORBIDDEN -> throw BadRequestException("Forbidden: ${e.responseBodyAsString}")
                HttpStatus.NOT_FOUND -> throw BadRequestException("Not found: ${e.responseBodyAsString}")
                else -> throw BadRequestException("HTTP error: ${e.statusCode}. Response: ${e.responseBodyAsString}")
            }

            else -> throw BadRequestException("An error occurred: ${e.message}")
        }
    }

}