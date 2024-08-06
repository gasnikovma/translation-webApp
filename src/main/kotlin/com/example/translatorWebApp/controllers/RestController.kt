package com.example.translatorWebApp.controllers

import com.example.translatorWebApp.models.request.TranslationRequest
import com.example.translatorWebApp.models.response.TranslationsListResponse
import com.example.translatorWebApp.clients.TranslatorClient
import com.example.translatorWebApp.services.TranslationService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@Controller
class RestController(val translationService: TranslationService) {
    var translatedText: String? = null


    @PostMapping("/api/translate")
    fun translate(
        translationRequest: TranslationRequest, httpServletRequest: HttpServletRequest
    ): String {
        val ipAddress = httpServletRequest.remoteAddr
        return try {
            translatedText = translationService.translate(translationRequest, ipAddress)
            "redirect:/"
        } catch (e: Exception) {
            translationService.handleTranslationError(e)
        }

    }

    @GetMapping("/")
    fun show(model: Model, httpServletRequest: HttpServletRequest): String {
        model.addAttribute("message", if (translatedText != null) translatedText else "")

        return "translation"
    }

}