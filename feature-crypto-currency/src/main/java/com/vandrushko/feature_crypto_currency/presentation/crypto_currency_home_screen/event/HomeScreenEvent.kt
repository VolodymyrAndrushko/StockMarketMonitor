package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event

import com.vandrushko.feature_crypto_currency.domain.model.Currency

sealed interface HomeScreenEvent {
    data class FindBySymbol(val symbol: String): HomeScreenEvent
    data class AddToFavourite(val currency: Currency): HomeScreenEvent
    object  EnterScreen: HomeScreenEvent
}