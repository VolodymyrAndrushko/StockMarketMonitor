package com.vandrushko.stockmarketmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vandrushko.core.presentation.ui.theme.StockMarketMonitorTheme
import com.vandrushko.stockmarketmonitor.presentation.CryptoAppUIRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockMarketMonitorTheme {
                CryptoAppUIRoot()
            }
        }
    }
}