package com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto

import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import kotlinx.serialization.Serializable

@Serializable
data class MiniTickerDTO(
    val e: String,    // event type
    val E: Long,      // event time
    val s: String,    // symbol
    val c: String,    // close price
    val o: String,    // open price
    val h: String,    // high price
    val l: String,    // low price
    val v: String,    // base asset volume
    val q: String     // quote asset volume
)

fun MiniTickerDTO.toCurrencyInfoEntity(): CurrencyEntity {
    val close = c.toDouble()
    val open = o.toDouble()
    val changePercent = ((close - open) / open) * 100
    val changeLast = close - open
    return CurrencyEntity(
        symbol = s,
        lastPrice = close,
        priceChangePercent = changePercent,
        priceChangeLast = changeLast,
        quoteVolume = q.toDouble(),
        lastUpdated = E
    )
}