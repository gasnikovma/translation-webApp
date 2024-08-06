package com.example.translatorWebApp.models.request


data class TranslationRequest(

    val folderId: String = "b1gq4jau4nobchssamgo",
    val texts: String,
    val targetLanguageCode: String,
    val sourceLang: String
)
