package com.vandrushko.feature_crypto_currency.data.remote.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfoDTO(
    val symbols: List<SymbolInfoDto>
)