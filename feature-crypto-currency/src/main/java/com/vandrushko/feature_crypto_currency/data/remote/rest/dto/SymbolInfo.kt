package com.vandrushko.feature_crypto_currency.data.remote.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class SymbolInfoDto(
    val symbol: String,
    val status: String
)