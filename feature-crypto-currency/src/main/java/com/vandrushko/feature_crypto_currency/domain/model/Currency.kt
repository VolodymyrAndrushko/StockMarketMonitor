package com.vandrushko.feature_crypto_currency.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val currencyName: String,
    val currentPriceText: String,
    val currentPrice: Double,
    val priceChangePercent: Double,
    val priceChangePercentText: String,
    val priceChangeLast: Double,
    val quoteVolume: Double,
    val lastTimeUpdate: String,
    val timestamp: Long
)
