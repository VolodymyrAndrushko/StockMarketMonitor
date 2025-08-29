package com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository
import kotlinx.coroutines.flow.Flow

class WatchCurrencyHistoryUseCase(
    private val repository: CryptoCurrencyRepository
) {
    suspend operator fun invoke(currency: Currency): Flow<List<Currency>> =
        repository.watchCurrencyChanges(currency)
}