package com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteCurrenciesUseCase(
    private val repository: CryptoCurrencyRepository
) {
    operator fun invoke(): Flow<List<Currency>> {
        return repository.getFavouriteCryptoCurrencies()
    }
}