package com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository

class SubscribeMultipleCryptoCurrenciesUseCase(
    private val repository: CryptoCurrencyRepository
) {
    suspend operator fun invoke(listCurrencies: List<Currency>) {
        repository.subscribeMultipleCurrencies(listCurrencies)
    }
}