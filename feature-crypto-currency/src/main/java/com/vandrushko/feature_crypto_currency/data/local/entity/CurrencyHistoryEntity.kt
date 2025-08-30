package com.vandrushko.feature_crypto_currency.data.local.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vandrushko.core.data.utils.doublePriceFormat
import com.vandrushko.core.data.utils.doublePricePercentDiffFormat
import com.vandrushko.core.data.utils.formatTimestampToHHMMSS
import com.vandrushko.feature_crypto_currency.domain.model.Currency


@Entity(
    indices = [Index("symbol")]
)
data class CurrencyHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String,
    val quoteVolume: Double,
    val timestamp: Long,
    val lastPrice: Double,
    val priceChangeLast: Double,
    val priceChangePercent: Double,
    val lastUpdated: Long,
)

fun CurrencyHistoryEntity.toCurrency(): Currency = Currency(
    currencyName = symbol,
    currentPrice = lastPrice,
    currentPriceText = doublePriceFormat(lastPrice),
    priceChangePercent = priceChangePercent,
    priceChangePercentText = doublePricePercentDiffFormat(priceChangePercent),
    priceChangeLast = priceChangeLast,
    quoteVolume = quoteVolume,
    lastTimeUpdate = formatTimestampToHHMMSS(lastUpdated),
    timestamp = lastUpdated
)