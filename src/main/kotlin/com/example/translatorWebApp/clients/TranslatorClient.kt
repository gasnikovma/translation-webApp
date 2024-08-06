package com.example.translatorWebApp.clients

import com.example.translatorWebApp.models.request.TranslationRequest
import com.example.translatorWebApp.models.response.TranslationResponse
import com.example.translatorWebApp.models.response.TranslationsListResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Service
class TranslatorClient(@Value("\${api.bearer-token}") val bearerToken: String) {
    private val restTemplate = RestTemplate()
    val baseUrl = "https://translate.api.cloud.yandex.net/translate/v2/translate"

    fun translate(requestPayLoad: TranslationRequest): ResponseEntity<TranslationsListResponse> {
        val httpHeaders = org.springframework.http.HttpHeaders()
        httpHeaders.apply {
            set("Content-Type", "application/json")
            set(
                "Authorization",
                bearerToken
            )
        }
        val mapper = jacksonObjectMapper()
        val requestJson = mapper.writeValueAsString(requestPayLoad)
        val entity = HttpEntity(requestJson, httpHeaders)
        return try {
            val response =  restTemplate.exchange(baseUrl, HttpMethod.POST, entity, TranslationsListResponse::class.java)
            if(response.statusCode.is2xxSuccessful){
                response
            }
            else{
                throw RuntimeException("Failed with HTTP error")
            }

        }
        catch (ex:HttpClientErrorException){
            when(ex.statusCode){
                HttpStatus.BAD_REQUEST ->throw  RuntimeException("Invalid request")
                else -> throw RuntimeException("Http Error")
            }
        }
    }


}