package com.vandrushko.feature_crypto_currency.data.remote.rest

import com.vandrushko.feature_crypto_currency.data.remote.rest.dto.CurrencyDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val TICKER_ENDPOINT = "https://api.binance.com/api/v3/ticker/24hr"
private const val LAST_24_H = TICKER_ENDPOINT + "24hr"

class BinanceRestApi(
    private val httpClient: HttpClient
) : RestCurrencyMarkerApi {

    override suspend fun getLastTickerSorted(): Result<List<CurrencyDTO>> {
        return try {
            val res: List<CurrencyDTO> = httpClient.get(TICKER_ENDPOINT).body()
            val filtered =
                res.sortedByDescending { it.quoteVolume }
            Result.success(
                filtered
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}