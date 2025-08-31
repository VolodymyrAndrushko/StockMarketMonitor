package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vandrushko.core.domain.settings.usecase.RefreshTimeStateUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.FindCurrenciesBySymbolUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetAllTickerUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetFavouriteCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.MarkCurrencyAsFavouriteUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.SubscribeMultipleCryptoCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.model.SortOptions
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event.HomeScreenEvent
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getFavouriteCurrenciesUseCase: GetFavouriteCurrenciesUseCase,
    private val markCurrencyAsFavouriteUseCase: MarkCurrencyAsFavouriteUseCase,
    private val findCurrenciesBySymbolUseCase: FindCurrenciesBySymbolUseCase,
    private val getAllTicker: GetAllTickerUseCase,
    private val subScribeMultiple: SubscribeMultipleCryptoCurrenciesUseCase,
    private val refreshTimeState: RefreshTimeStateUseCase
) : ViewModel() {

    private var watchJob: Job? = null
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            refreshTimeState().collectLatest { updatePeriod ->
                _state.update { it.copy(updatePeriod = updatePeriod) }
            }
        }
    }

    fun emit(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.FindBySymbol -> {
                viewModelScope.launch {
                    val result = findCurrenciesBySymbolUseCase(event.symbol)
                    _state.update {
                        it.copy(findResultCurrencies = result)
                    }
                }
            }

            is HomeScreenEvent.AddToFavourite -> {
                viewModelScope.launch {
                    markCurrencyAsFavouriteUseCase(event.currency)
                    delay(500)
                    subScribeMultiple(_state.value.favouriteCurrencies)
                }
            }

            is HomeScreenEvent.EnterScreen -> {
                viewModelScope.launch {
                    getAllTicker()
                    delay(500)
                    subScribeMultiple(_state.value.favouriteCurrencies)
                }

                watchJob?.cancel()
                watchJob = viewModelScope.launch {
                    getFavouriteCurrenciesUseCase()
                        .sample(200)
                        .collectLatest { favourite ->

                            updateWithSort(_state.value.sortOption, favourite)
                        }
                }
            }

            is HomeScreenEvent.StopWatch -> {
                watchJob?.cancel()
            }

            is HomeScreenEvent.ChangeSortOption -> {
                updateWithSort(event.sort, _state.value.favouriteCurrencies)
            }
        }
    }

    private fun updateWithSort(sortOption: SortOptions, favourite: List<Currency>) {
        val sortedList = when (sortOption) {
            SortOptions.DEFAULT -> favourite
            SortOptions.L_TO_H -> favourite.sortedBy { it.currentPrice }
            SortOptions.H_TO_L -> favourite.sortedByDescending { it.currentPrice }
        }
        _state.update {
            it.copy(favouriteCurrencies = sortedList, sortOption = sortOption)
        }
    }

    override fun onCleared() {
        super.onCleared()
        watchJob?.cancel()
    }
}