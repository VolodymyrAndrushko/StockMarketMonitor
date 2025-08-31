package com.vandrushko.feature_crypto_currency.data.remote.rest

import com.vandrushko.core.di.remote.RestClient
import com.vandrushko.feature_crypto_currency.data.remote.rest.dto.CurrencyDTO
import com.vandrushko.feature_crypto_currency.data.remote.rest.dto.ExchangeInfoDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.collections.asSequence
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.toSet
import kotlin.map
import kotlin.sequences.filter
import kotlin.sequences.map
import kotlin.sequences.toSet
import kotlin.text.contains
import kotlin.text.filter
import kotlin.text.map
import kotlin.text.toSet

private const val BASE_URL = "https://api.binance.com/api/v3"
private const val TICKER_ENDPOINT = "$BASE_URL/ticker/24hr"
private const val EXCHANGE_INFO = "$BASE_URL/exchangeInfo"

class BinanceRestApi(
    @RestClient private val httpClient: HttpClient
) : RestCurrencyMarkerApi {

    override suspend fun getLastTickerSorted(): Result<List<CurrencyDTO>> {
        return try {
            val tickers: List<CurrencyDTO> = httpClient.get(TICKER_ENDPOINT).body()
            val exchangeInfo: ExchangeInfoDTO = httpClient.get(EXCHANGE_INFO).body()

            val tradableSymbols = exchangeInfo.symbols
                .asSequence()
                .filter { it.status == "TRADING" }
                .map { it.symbol }
                .toSet()

            val filtered = tickers
                .filter { it.symbol in tradableSymbols }
                .sortedByDescending { it.quoteVolume }

            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}