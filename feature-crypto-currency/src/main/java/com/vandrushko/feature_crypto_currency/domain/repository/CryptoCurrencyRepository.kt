package com.vandrushko.feature_crypto_currency.domain.repository

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CryptoCurrencyRepository {
    fun getFavouriteCryptoCurrencies(): Flow<List<Currency>>
    suspend fun getAllBinanceCryptoCurrenciesAndSaveToDb()
    suspend fun subscribeMultipleCurrencies(listCurrencies: List<Currency>)
    suspend fun watchCurrencyChanges(currency: Currency): Flow<List<Currency>>
    suspend fun markCurrencyAsFavourite(currency: Currency)
    suspend fun getAllCurrenciesThatContains(symbol: String): List<Currency>
    suspend fun subscribeToOneCurrency(currency: Currency)

}