package com.vandrushko.feature_crypto_currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.withTransaction
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDao {

    @Query("DELETE FROM currencyEntity")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CurrencyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: CurrencyEntity)

    @Query(
        """
    DELETE FROM currencyEntity
    WHERE symbol = :symbol
      AND id NOT IN (
          SELECT id 
          FROM currencyEntity
          WHERE symbol = :symbol
          ORDER BY lastUpdated DESC
          LIMIT 1
      )
      AND lastUpdated < :cutoff
"""
    )
    suspend fun deleteOlderThan(symbol: String, cutoff: Long)

    @Transaction
    suspend fun insertWithCleanup(history: CurrencyEntity) {
        insert(history)
        val cutoff = history.lastUpdated - 5 * 60 * 1000
        deleteOlderThan(history.symbol, cutoff)
    }

    @Query("UPDATE CurrencyEntity SET isFavourite = 1 WHERE symbol = :symbol")
    suspend fun markSymbolAsFavourite(symbol: String)

    @Query(
        """
    SELECT c.*
    FROM currencyEntity c
    INNER JOIN (
        SELECT symbol, MAX(lastUpdated) AS maxUpdated
        FROM currencyEntity
        WHERE isFavourite = 1
        GROUP BY symbol
    ) c2 ON c.symbol = c2.symbol AND c.lastUpdated = c2.maxUpdated
"""
    )
    fun getAllFavouriteCurrencies(): Flow<List<CurrencyEntity>>

    @Query(
        """
        SELECT * FROM currencyEntity
        WHERE symbol = :symbol
        AND lastUpdated >= :fromTimestamp
        ORDER BY lastUpdated ASC
    """
    )
    fun getLast5MinutesFromOldest(
        symbol: String,
        fromTimestamp: Long = System.currentTimeMillis() - 5 * 60 * 1000
    ): Flow<List<CurrencyEntity>>

    suspend fun replaceAllData(db: CurrencyDatabase, newItems: List<CurrencyEntity>) {
        db.withTransaction {
            db.currencyDao.deleteAll()
            db.currencyDao.insertAll(newItems)
        }
    }

    @Query(
        """
    SELECT *
    FROM currencyEntity
    WHERE symbol = :symbol
    ORDER BY lastUpdated DESC
    LIMIT 1
"""
    )
    fun getLatestBySymbol(symbol: String): Flow<CurrencyEntity?>

    @Query("UPDATE currencyEntity SET lastPrice = :price, lastUpdated = :timestamp WHERE symbol = :symbol")
    suspend fun updatePrice(
        symbol: String,
        price: Double,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM currencyEntity ORDER BY symbol ASC")
    fun getAllCurrencies(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currencyEntity WHERE symbol LIKE '%' || :symbolPart || '%' AND lastUpdated >= :fromTimestamp")
    suspend fun findCurrenciesContainingSymbol(
        symbolPart: String,
        fromTimestamp: Long = System.currentTimeMillis() - 5 * 60 * 1000
    ): List<CurrencyEntity>
}