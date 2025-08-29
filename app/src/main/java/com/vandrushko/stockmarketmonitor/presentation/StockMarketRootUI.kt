package com.vandrushko.stockmarketmonitor.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.vandrushko.core.presentation.notifications.NotificationScreen
import com.vandrushko.core.presentation.settings.SettingsScreen
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.CryptoCurrencyHomeScreen
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.FindCryptoField
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.DetailedGraphScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.GraphDetailedScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.MainScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.Notification
import com.vandrushko.stockmarketmonitor.presentation.navigation.Settings

@Composable
fun CryptoAppUIRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(MainScreen)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                backStack = backStack, entryProvider = entryProvider {
                    entry<MainScreen> {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FindCryptoField()
                            CryptoCurrencyHomeScreen(onCurrencyClick = {
                                backStack.add(GraphDetailedScreen(it))
                            })
                        }
                    }
                    entry<Settings> {
                        SettingsScreen()
                    }
                    entry<Notification> {
                        NotificationScreen()
                    }
                    entry<GraphDetailedScreen> { currency ->
                        DetailedGraphScreen(currency = currency.currency)
                    }
                }
            )
            StockMarketNavigationBar(Modifier.align(Alignment.BottomCenter), { route ->
                val lastEntry = backStack.last()
                if (lastEntry::class != route::class) { // Compare types
                    backStack.removeAll { it::class == route::class }
                    backStack.add(route)
                }
            })
        }
    }
}