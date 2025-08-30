package com.vandrushko.feature_crypto_currency.presentation.detailed_graph

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vandrushko.core.presentation.ui.theme.StockMarketMonitorTheme
import com.vandrushko.feature_crypto_currency.R
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.components.LineChartView
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.event.DetailedGraphEvent
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.model.DetailedGraphState
import com.vandrushko.feature_crypto_currency.presentation.detailed_graph.vm.DetailedGraphViewModel
import java.text.DecimalFormat

@Composable
fun DetailedGraphScreen(
    modifier: Modifier = Modifier,
    currency: Currency,
    detailGraphViewModel: DetailedGraphViewModel = viewModel(),
    onClose: () -> Unit
) {
    val state = detailGraphViewModel.state.collectAsState().value

    LaunchedEffect(currency) {
        detailGraphViewModel.emit(DetailedGraphEvent.EnterScreen)
        detailGraphViewModel.emit(DetailedGraphEvent.WatchCurrencyHistory(currency))
    }

    DetailedGraphScreenInner(
        state = state, currency = currency, onRemoveFromFavourite = {
            detailGraphViewModel.emit(DetailedGraphEvent.RemoveFromFavourite(currency))
            onClose()
        })
    DisposableEffect(Unit) {
        onDispose {
            detailGraphViewModel.emit(DetailedGraphEvent.StopWatch)
        }
    }
}

@Composable
private fun DetailedGraphScreenInner(
    modifier: Modifier = Modifier,
    state: DetailedGraphState,
    currency: Currency,
    onRemoveFromFavourite: () -> Unit
) {
    Box(
        modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = currency.currencyName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.lastCurrency?.currentPriceText ?: currency.currentPriceText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(
                            R.string.last_update,
                            state.lastCurrency?.lastTimeUpdate ?: currency.lastTimeUpdate
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val priceChangePercent = state.lastCurrency?.priceChangePercent
                    if (priceChangePercent != null) {
                        val changeColor =
                            if (priceChangePercent >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                        Text(
                            text = "${DecimalFormat("0.#####").format(state.lastCurrency?.priceChangePercent ?: currency.priceChangePercent)}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = changeColor
                        )
                    }
                }
            }
            LineChartView(currencies = state.currencies)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(
                            R.string.last_five_minutes
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.update_period, state.updatePeriod.seconds),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onRemoveFromFavourite() },
                    text = stringResource(R.string.remove_from_favourite),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun DetailedGraphScreenInnerPreview() {
    StockMarketMonitorTheme {
        DetailedGraphScreenInner(
            state = DetailedGraphState(
                currencies = listOf(), lastCurrency = Currency(
                    currencyName = "Queen Mejia",
                    currentPriceText = "pharetra",
                    currentPrice = 34.35,
                    priceChangePercent = 36.37,
                    priceChangeLast = 38.39,
                    quoteVolume = 40.41,
                    lastTimeUpdate = "mauris",
                    timestamp = 8497, priceChangePercentText = "detraxit",

                    )
            ), onRemoveFromFavourite = {}, currency = Currency(
                currencyName = "Hazel Montoya",
                currentPriceText = "urna",
                currentPrice = 56.57,
                priceChangePercent = 58.59,
                priceChangeLast = 60.61,
                quoteVolume = 62.63,
                lastTimeUpdate = "amet",
                timestamp = 5697,
                priceChangePercentText = "montes"
            )
        )
    }
}