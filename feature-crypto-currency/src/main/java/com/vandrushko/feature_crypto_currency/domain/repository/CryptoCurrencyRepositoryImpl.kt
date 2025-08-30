package com.vandrushko.feature_crypto_currency.domain.repository

import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.notifications.model.Notification
import com.vandrushko.feature_crypto_currency.data.local.CurrencyDatabase
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyHistoryEntity
import com.vandrushko.feature_crypto_currency.data.local.entity.toCurrency
import com.vandrushko.feature_crypto_currency.data.local.entity.toCurrencyHistoryEntity
import com.vandrushko.feature_crypto_currency.data.remote.rest.RestCurrencyMarkerApi
import com.vandrushko.feature_crypto_currency.data.remote.rest.dto.toCurrencyEntity
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.WebSocketCurrencyMarketApi
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class CryptoCurrencyRepositoryImpl(
    private val db: CurrencyDatabase,
    private val apiRest: RestCurrencyMarkerApi,
    private val apiWebSockets: WebSocketCurrencyMarketApi,
    private val notificationsRepository: NotificationsRepository,
    private val scope: CoroutineScope
) : CryptoCurrencyRepository {

    init {
        scope.launch {
            apiWebSockets.startConnection()

            apiWebSockets.currencyFlow.collectLatest { entity ->
                println("TTTTTTTTTTTTT $entity")
                db.currencyDao.upsertPreserveFavourite(entity)
                db.currencyHistoryDao.insertWithCleanup(entity.toCurrencyHistoryEntity())
            }
        }

        scope.launch {
            val message =
                "Price of BTC changed from: $${109.2442} to $${100.42424}\nDifference: 4% THIS IS MOCK NOTIFICATION"

            repeat(3) {
                notificationsRepository.saveNotificationLocally(
                    Notification(
                        message = message, timestamp = System.currentTimeMillis()
                    )
                )
                delay(25000)
            }
        }
    }

    override fun getFavouriteCryptoCurrencies(): Flow<List<Currency>> {
        val currencies = db.currencyDao.getAllFavouriteCurrencies()

        return currencies.map {
            it.map { entity ->
                entity.toCurrency()
            }
        }
    }

    override suspend fun getAllBinanceCryptoCurrenciesAndSaveToDb() {
        apiRest.getLastTickerSorted()
            .onFailure {

            }.onSuccess {

                db.currencyDao.upsertAllPreserveFavourite(
                    it.map { it.toCurrencyEntity() })
            }
    }

    override suspend fun subscribeToOneCurrency(currency: Currency) {

    }

    override suspend fun markSymbolAsNotFavourite(symbol: String) {
        db.currencyDao.markSymbolAsNotFavourite(symbol)
    }

    override suspend fun unSubscribeMultipleCurrencies(listCurrencies: List<Currency>) {
        apiWebSockets.unsubscribeSymbol(listCurrencies.map { it.currencyName.lowercase() })
    }

    override suspend fun subscribeMultipleCurrencies(listCurrencies: List<Currency>) {
        apiWebSockets.subscribeSymbol(listCurrencies.map { it.currencyName.lowercase() })
    }

    override suspend fun watchCurrencyChanges(currency: Currency): Flow<List<Currency>> {
        return db.currencyHistoryDao.getLast5MinutesFromOldest(currency.currencyName)
            .mapLatest { listOfEntities ->
                checkForPriceDifference(listOfEntities, { old, last, percent ->
                    scope.launch {
                        val message =
                            "Price of ${old.symbol} changed from: $${old.lastPrice} to $${last.lastPrice}\nDifference: $percent%"
                        notificationsRepository.saveNotificationLocally(
                            Notification(
                                message = message, timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                })
                listOfEntities.map {
                    it.toCurrency()
                }
            }
    }

    private fun checkForPriceDifference(
        list: List<CurrencyHistoryEntity>,
        onDifferenceExceeded: (old: CurrencyHistoryEntity, last: CurrencyHistoryEntity, percent: Double) -> Unit
    ) {
        if (list.size > 1) {
            val last = list.last()
            list.forEach { prev ->
                val percentDiff = (last.lastPrice - prev.lastPrice) / prev.lastPrice * 100.0

                if (abs(percentDiff) > 5) {
                    onDifferenceExceeded(prev, last, percentDiff)
                }
            }
        }
    }

    override suspend fun markCurrencyAsFavourite(currency: Currency) {
        db.currencyDao.markSymbolAsFavourite(currency.currencyName)
    }

    override suspend fun getAllCurrenciesThatContains(symbol: String): List<Currency> =
        db.currencyDao.findCurrenciesContainingSymbol(symbol).map { it.toCurrency() }
}