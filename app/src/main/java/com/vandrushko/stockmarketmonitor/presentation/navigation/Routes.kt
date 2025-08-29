package com.vandrushko.stockmarketmonitor.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import kotlinx.serialization.Serializable

@Serializable
object MainScreen : NavKey
@Serializable
object Settings : NavKey
@Serializable
object Notification : NavKey
@Serializable
data class GraphDetailedScreen(val currency: Currency) : NavKey