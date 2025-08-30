package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event

import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.model.SortOptions

sealed interface HomeScreenEvent {
    data class FindBySymbol(val symbol: String): HomeScreenEvent
    data class AddToFavourite(val currency: Currency): HomeScreenEvent
    data class ChangeSortOption(val sort: SortOptions): HomeScreenEvent
    object  EnterScreen: HomeScreenEvent
    object  StopWatch: HomeScreenEvent
}