package com.simple.translate.entities

import com.simple.state.ResultState

data class TranslateRequest(
    val text: String,
    val languageCode: String
)

data class TranslateResponse(
    val text: String,
    val translateState: ResultState<String>
)
