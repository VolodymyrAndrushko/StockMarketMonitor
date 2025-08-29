package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.FindCurrenciesBySymbolUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetAllTickerUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetFavouriteCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.MarkCurrencyAsFavouriteUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.SubscribeMultipleCryptoCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event.HomeScreenEvent
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getFavouriteCurrenciesUseCase: GetFavouriteCurrenciesUseCase,
    private val markCurrencyAsFavouriteUseCase: MarkCurrencyAsFavouriteUseCase,
    private val findCurrenciesBySymbolUseCase: FindCurrenciesBySymbolUseCase,
    private val getAllTicker: GetAllTickerUseCase,
    private val subScribeMultiple: SubscribeMultipleCryptoCurrenciesUseCase,

    ) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    val ticker = flow {
        while (true) {
            emit(Unit)
            delay(5000)
        }
    }
    private val searchQuery = MutableStateFlow("")

    init {
        subscribeCurrenciesFind()

        viewModelScope.launch {
            getAllTicker()

            getFavouriteCurrenciesUseCase().collectLatest { favourite ->
                println("AASDASDASDASDASDASDASD ${favourite.toString()}")
                _state.update {
                    it.copy(favouriteCurrencies = favourite)
                }
            }
        }
    }

    private fun subscribeCurrenciesFind() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { symbol ->
                    if (symbol.isBlank()) {
                        _state.update { it.copy(findResultCurrencies = emptyList()) }
                        return@collectLatest
                    }

                    val result = findCurrenciesBySymbolUseCase(symbol)
                    _state.update {
                        it.copy(findResultCurrencies = result)
                    }
                }
        }
    }

    fun emit(event: HomeScreenEvent) {
        when(event){
            is HomeScreenEvent.FindBySymbol -> {
                viewModelScope.launch {
                    searchQuery.value = event.symbol
                }
            }

            is HomeScreenEvent.AddToFavourite -> {
                viewModelScope.launch {
                    markCurrencyAsFavouriteUseCase(event.currency)
                }
                viewModelScope.launch {
                    subScribeMultiple(_state.value.favouriteCurrencies)
                }
            }

            is HomeScreenEvent.EnterScreen -> {
                viewModelScope.launch {
                    subScribeMultiple(_state.value.favouriteCurrencies)
                }
            }
        }
    }
}