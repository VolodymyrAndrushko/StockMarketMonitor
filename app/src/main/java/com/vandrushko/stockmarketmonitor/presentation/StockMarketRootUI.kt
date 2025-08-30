package com.vandrushko.stockmarketmonitor.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.vandrushko.core.presentation.notifications.NotificationScreen
import com.vandrushko.core.presentation.notifications.vm.NotificationsViewModel
import com.vandrushko.core.presentation.settings.SettingsScreen
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.CryptoCurrencyHomeScreen
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.FindCryptoField
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.DetailedGraphScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.GraphDetailedScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.MainScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.Notification
import com.vandrushko.stockmarketmonitor.presentation.navigation.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CryptoAppUIRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(MainScreen)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    NotificationHandler(scope, snackbarHostState, backStack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<MainScreen> {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FindCryptoField()
                            CryptoCurrencyHomeScreen(
                                onCurrencyClick = {
                                    println("DetailedGraphEvent GraphDetailedScreen")
                                    backStack.add(GraphDetailedScreen(it))
                                },
                            )
                        }
                    }
                    entry<Settings> {
                        SettingsScreen()
                    }
                    entry<Notification> {
                        NotificationScreen()
                    }
                    entry<GraphDetailedScreen> { currency ->
                        DetailedGraphScreen(currency = currency.currency, onClose = {
                            backStack.removeAll { it is GraphDetailedScreen }
                        })
                    }
                })
            StockMarketNavigationBar(Modifier.align(Alignment.BottomCenter), { route ->
                val lastEntry = backStack.last()
                if (lastEntry::class != route::class) {
                    backStack.removeAll { it::class == route::class }
                    backStack.add(route)
                }
            })
        }
    }
}

@Composable
fun NotificationHandler(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    backStack: NavBackStack,
    notificationViewModel: NotificationsViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        scope.launch {
            notificationViewModel.onNewNotification.collect {
                if (backStack.last() !is Notification) {
                    val result = snackbarHostState.showSnackbar(
                        message = it.message, actionLabel = "Open", duration = SnackbarDuration.Long
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        backStack.add(Notification)
                    }
                }
            }
        }
    }

}
