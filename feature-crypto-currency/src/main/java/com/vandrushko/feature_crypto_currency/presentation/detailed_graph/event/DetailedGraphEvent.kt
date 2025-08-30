package com.vandrushko.feature_crypto_currency.presentation.detailed_graph.event

import com.vandrushko.feature_crypto_currency.domain.model.Currency

sealed interface DetailedGraphEvent {
    data class WatchCurrencyHistory(val currency: Currency): DetailedGraphEvent
    object EnterScreen : DetailedGraphEvent
    object StopWatch : DetailedGraphEvent
    data class RemoveFromFavourite(val currency: Currency) : DetailedGraphEvent
}