package com.vandrushko.feature_crypto_currency.data.remote.rest

import com.vandrushko.feature_crypto_currency.data.remote.rest.dto.CurrencyDTO

interface RestCurrencyMarkerApi {
    suspend fun getLastTickerSorted(): Result<List<CurrencyDTO>>
}