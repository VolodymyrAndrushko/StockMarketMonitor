package com.vandrushko.feature_crypto_currency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class], version = 1
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
}