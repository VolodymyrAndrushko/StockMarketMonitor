package com.vandrushko.feature_crypto_currency.presentation.detailed_graph.model

import androidx.compose.runtime.Stable
import com.vandrushko.core.domain.settings.model.UpdatePeriod
import com.vandrushko.feature_crypto_currency.domain.model.Currency

@Stable
data class DetailedGraphState(
    val currencies: List<Currency> = listOf(),
    val lastCurrency: Currency? = null,
    val updatePeriod: UpdatePeriod = UpdatePeriod.FIVE_SECOND,
)
