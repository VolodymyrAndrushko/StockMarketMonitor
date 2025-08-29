package com.vandrushko.feature_crypto_currency.data.remote.web_sockets

import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

interface WebSocketCurrencyMarketApi {
    fun stopConnection()
    suspend fun subscribeSymbol(symbols: List<String>, id: Int = 1)
    suspend fun unsubscribeSymbol(symbols: List<String>, id: Int = 2)
    fun startConnection()
    val currencyFlow: Flow<CurrencyEntity>
}