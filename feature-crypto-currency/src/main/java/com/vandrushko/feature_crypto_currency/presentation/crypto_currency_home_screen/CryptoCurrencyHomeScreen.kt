package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vandrushko.feature_crypto_currency.R
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.components.CryptoCurrency
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event.HomeScreenEvent
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.state.HomeScreenState
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.vm.HomeScreenViewModel

@Composable
fun CryptoCurrencyHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(),
    onCurrencyClick: (Currency) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(true) {
        viewModel.emit(HomeScreenEvent.EnterScreen)
    }
    CryptoCurrencyHomeScreenInner(state = state, onCurrencyClick = onCurrencyClick)

    DisposableEffect(true) {
        onDispose {
            viewModel.emit(HomeScreenEvent.StopWatch)
        }
    }
}

@Composable
private fun CryptoCurrencyHomeScreenInner(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onCurrencyClick: (currency: Currency) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            val items = state.favouriteCurrencies
            if (items.isNotEmpty()) {
                itemsIndexed(
                    state.favouriteCurrencies.distinctBy { it.currencyName },
                    key = { _, item -> item.currencyName }) { index, currency ->
                    CryptoCurrency(
                        currency,
                        onClick = {
                            onCurrencyClick(it)
                        }
                    )
                }
            } else {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Spacer(Modifier.height(240.dp))
                        Text(
                            text = stringResource(R.string.there_is_no_added_currencies_yet),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CryptoCurrencyHomeScreenInnerPreview() {
    CryptoCurrencyHomeScreenInner(
        state = HomeScreenState(
            false,
            listOf(),
            listOf(),

            ), onCurrencyClick = {}
    )
}