package com.vandrushko.feature_crypto_currency.presentation.detailed_graph.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vandrushko.core.domain.settings.usecase.RefreshTimeStateUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.SubscribeToOneCurrencyUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.WatchCurrencyHistoryUseCase
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.event.DetailedGraphEvent
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.model.DetailedGraphState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailedGraphViewModel @Inject constructor(
    private val watchCurrencyHistory: WatchCurrencyHistoryUseCase,
    private val subscribeToOneCurrency: SubscribeToOneCurrencyUseCase,
    private val refreshTimeState: RefreshTimeStateUseCase
) : ViewModel() {

    private var watchJob: Job? = null
    private val _state = MutableStateFlow(DetailedGraphState())
    val state = _state.asStateFlow()

    fun emit(event: DetailedGraphEvent) {
        when (event) {
            is DetailedGraphEvent.WatchCurrencyHistory -> {
                subscribeToCurrencyHistory(event.currency)
            }

            is DetailedGraphEvent.StopWatch -> {
            }
        }
    }

    private fun subscribeToCurrencyHistory(currency: Currency) {
        refreshTimeState().value.let { refreshTime ->
            _state.update {
                it.copy(updatePeriod = refreshTime)
            }
        }

        watchJob?.cancel()
        watchJob = viewModelScope.launch {
            watchCurrencyHistory(currency).sample(_state.value.updatePeriod.millis)
                .collectLatest { currencies ->
                    _state.update {
                        it.copy(
                            currencies = currencies,
                            lastCurrency = currencies.maxBy { it.timestamp })
                    }
                    println("ASDASDASD " + currencies.size)
                    println("ASDASDASD " + _state.value.currencies.toString())
                }
        }
    }
}