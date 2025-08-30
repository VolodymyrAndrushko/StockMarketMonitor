package com.vandrushko.feature_crypto_currency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyHistory: CurrencyHistoryEntity)

    @Query("DELETE FROM currencyHistoryEntity WHERE timestamp < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long = System.currentTimeMillis() - 5 * 60 * 1000)

    @Transaction
    suspend fun insertWithCleanup(history: CurrencyHistoryEntity) {
        deleteOlderThan()
        insert(history)
    }

    @Query(
        """
        SELECT * FROM currencyHistoryEntity
        WHERE symbol = :symbol
        AND timestamp >= :fromTimestamp
        ORDER BY timestamp ASC
    """
    )
    fun getLast5MinutesFromOldest(
        symbol: String, fromTimestamp: Long = System.currentTimeMillis() - 5 * 60 * 1000
    ): Flow<List<CurrencyHistoryEntity>>
}