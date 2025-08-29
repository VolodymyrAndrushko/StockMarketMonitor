package com.vandrushko.stockmarketmonitor.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.vandrushko.stockmarketmonitor.R
import com.vandrushko.stockmarketmonitor.presentation.navigation.MainScreen
import com.vandrushko.stockmarketmonitor.presentation.navigation.Notification
import com.vandrushko.stockmarketmonitor.presentation.navigation.Settings

@Composable
fun StockMarketNavigationBar(
    modifier: Modifier = Modifier, onNavigation: (NavKey) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 28.dp,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_home),
                modifier = Modifier.clickable {
                    onNavigation(MainScreen)
                },
                contentDescription = stringResource(R.string.main_menu),
                tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                painter = painterResource(R.drawable.ic_notifications),
                modifier = Modifier.clickable {
                    onNavigation(Notification)
                },
                contentDescription = stringResource(R.string.main_menu),
                tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                modifier = Modifier.clickable {
                    onNavigation(Settings)
                },
                contentDescription = stringResource(R.string.main_menu),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}