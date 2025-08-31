package com.vandrushko.feature_crypto_currency.data.remote.rest.dto

import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDTO(
    val symbol: String,
    val lastPrice: Double,
    val priceChangePercent: Double,
    val priceChange: Double,
    val quoteVolume: Double,
    val status: String? = null
)

fun CurrencyDTO.toCurrencyEntity(): CurrencyEntity =
    CurrencyEntity(
        symbol = symbol,
        quoteVolume = quoteVolume,
        lastPrice = lastPrice,
        priceChangeLast = priceChange,
        priceChangePercent = priceChangePercent,
        lastUpdated = System.currentTimeMillis(),
    )