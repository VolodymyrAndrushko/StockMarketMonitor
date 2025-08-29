package com.vandrushko.feature_crypto_currency.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vandrushko.core.data.utils.doublePriceFormat
import com.vandrushko.core.data.utils.formatTimestampToHHMMSS
import com.vandrushko.feature_crypto_currency.domain.model.Currency

//Primary key is string, because binance does not provide id for currency symbols
@Entity(
    indices = [Index("symbol")]
)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val symbol: String,
    val quoteVolume: Double,
    val priceChangeLast: Double,
    val priceChangePercent: Double,
    val lastPrice: Double,
    val lastUpdated: Long,
    val isFavourite: Boolean = false
)

fun CurrencyEntity.toCurrency(): Currency = Currency(
    currencyName = symbol,
    currentPrice = lastPrice,
    currentPriceText = doublePriceFormat(lastPrice),
    priceChangePercent = priceChangePercent,
    priceChangeLast = priceChangeLast,
    quoteVolume = quoteVolume,
    lastTimeUpdate = formatTimestampToHHMMSS(lastUpdated),
    timestamp = lastUpdated
)
//
//fun CurrencyEntity.toPriceHistoryEntity(): PriceHistoryEntity = PriceHistoryEntity(
//    symbol = symbol, lastPrice = lastPrice, timestamp = lastUpdated
//)
