package com.vandrushko.feature_crypto_currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDao {
    @Query("UPDATE currencyEntity SET isFavourite = 1 WHERE symbol = :symbol")
    suspend fun markSymbolAsFavourite(symbol: String)

    @Query("UPDATE currencyEntity SET isFavourite = 0 WHERE symbol = :symbol")
    suspend fun markSymbolAsNotFavourite(symbol: String)

    @Query("SELECT * FROM currencyEntity WHERE isFavourite = 1")
    fun getAllFavouriteCurrencies(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currencyEntity WHERE symbol LIKE '%' || :symbolPart || '%' AND lastUpdated >= :fromTimestamp")
    suspend fun findCurrenciesContainingSymbol(
        symbolPart: String, fromTimestamp: Long = System.currentTimeMillis() - 5 * 60 * 1000
    ): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(currency: CurrencyEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(items: List<CurrencyEntity>): List<Long>

    @Query(
        """
    UPDATE currencyEntity 
    SET quoteVolume = :quoteVolume,
        priceChangeLast = :priceChangeLast,
        priceChangePercent = :priceChangePercent,
        lastPrice = :lastPrice,
        lastUpdated = :lastUpdated
    WHERE symbol = :symbol
"""
    )
    suspend fun updateExceptFavourite(
        symbol: String,
        quoteVolume: Double,
        priceChangeLast: Double,
        priceChangePercent: Double,
        lastPrice: Double,
        lastUpdated: Long
    )

    @Transaction
    suspend fun upsertPreserveFavourite(currency: CurrencyEntity) {
        val inserted = insertIfNotExists(currency)
        if (inserted == -1L) {
            updateExceptFavourite(
                currency.symbol,
                currency.quoteVolume,
                currency.priceChangeLast,
                currency.priceChangePercent,
                currency.lastPrice,
                currency.lastUpdated
            )
        }
    }

    @Transaction
    suspend fun upsertAllPreserveFavourite(items: List<CurrencyEntity>) {
        val results = insertIfNotExists(items)

        val existing = items.filterIndexed { index, _ -> results[index] == -1L }

        existing.forEach { item ->
            updateExceptFavourite(
                item.symbol,
                item.quoteVolume,
                item.priceChangeLast,
                item.priceChangePercent,
                item.lastPrice,
                item.lastUpdated
            )
        }
    }
}