package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.state

import androidx.compose.runtime.Stable
import com.vandrushko.core.domain.settings.model.UpdatePeriod
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.domain.model.SortOptions

@Stable
data class HomeScreenState(
    val isFinding: Boolean = false,
    val findResultCurrencies: List<Currency> = listOf(),
    val favouriteCurrencies: List<Currency> = listOf(),
    val updatePeriod: UpdatePeriod = UpdatePeriod.TWO_SECOND,
    val sortOption: SortOptions = SortOptions.DEFAULT
)
