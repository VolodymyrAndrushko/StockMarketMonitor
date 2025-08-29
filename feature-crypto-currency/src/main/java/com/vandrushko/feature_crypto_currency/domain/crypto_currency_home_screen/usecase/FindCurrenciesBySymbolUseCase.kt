package com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository

class FindCurrenciesBySymbolUseCase(
    private val repository: CryptoCurrencyRepository
) {
    suspend operator fun invoke(symbol: String): List<Currency> =
        repository.getAllCurrenciesThatContains(symbol)

}