package com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase

import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository

class GetAllTickerUseCase(
    private val repository: CryptoCurrencyRepository
) {
    suspend operator fun invoke() {
        return repository.getAllBinanceCryptoCurrenciesAndSaveToDb()
    }
}