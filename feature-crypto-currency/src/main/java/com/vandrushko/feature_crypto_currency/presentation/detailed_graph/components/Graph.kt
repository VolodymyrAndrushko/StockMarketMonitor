package com.vandrushko.feature_crypto_currency.presentation.detailed_graph.components

import android.graphics.Color
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.vandrushko.feature_crypto_currency.R
import com.vandrushko.feature_crypto_currency.domain.model.Currency

@Composable
fun LineChartView(currencies: List<Currency>, darkTheme: Boolean = isSystemInDarkTheme()) {
    val now = System.currentTimeMillis()
    val fiveMinutesAgo = now - 5 * 60 * 1000

    val last5Min = currencies.filter { it.timestamp >= fiveMinutesAgo }

    val prices = last5Min.map {
        Entry(
            ((it.timestamp - fiveMinutesAgo) / 1000f), it.currentPrice.toFloat(), it
        )
    }
    val noDataText = stringResource(R.string.no_graph_data_available)

    val graphColor = if (darkTheme) Color.WHITE else Color.BLACK

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                axisRight.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.textColor = graphColor
                xAxis.textColor = graphColor

                marker = object : MarkerView(context, R.layout.marker_layout) {
                    private val tvY: TextView = findViewById(R.id.markerTextY)
                    private val tvX: TextView = findViewById(R.id.markerTextX)
                    override fun refreshContent(e: Entry?, highlight: Highlight?) {
                        if (e != null) {
                            val currency = e.data as? Currency
                            tvY.text = (currency?.currentPrice ?: e.y).toString()
                            tvX.text = currency?.lastTimeUpdate ?: ""
                        }
                        super.refreshContent(e, highlight)
                    }

                    override fun getOffset(): MPPointF {
                        return MPPointF(-(width / 2f), -height.toFloat())
                    }
                }
            }
        },
        update = { chart ->
            if (prices.isNotEmpty()) {
                val minPrice = prices.minOf { it.y }
                val maxPrice = prices.maxOf { it.y }

                val padding = (maxPrice - minPrice) * 0.05f
                chart.axisLeft.axisMinimum = minPrice - padding
                chart.axisLeft.axisMaximum = maxPrice + padding

                val dataSet = LineDataSet(prices, "Price").apply {
                    color = graphColor
                    valueTextColor = graphColor
                    lineWidth = 1f
                    setDrawCircles(false)
                    setDrawValues(false)
                }

                chart.data = LineData(dataSet)
                chart.invalidate()
            } else {
                chart.clear()
                chart.setNoDataText(noDataText)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .border(2.dp, color = MaterialTheme.colorScheme.primary)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun LineChartViewPreview() {
    Column {

        LineChartView(
            currencies = listOf(
                Currency(
                    currencyName = "Aldo Hudson",
                    currentPriceText = "0.3333",
                    currentPrice = 0.3333,
                    priceChangePercent = 20.21,
                    priceChangeLast = 22.23,
                    quoteVolume = 24.25,
                    lastTimeUpdate = "unum",
                    timestamp = 4950,
                    priceChangePercentText = "docendi"
                )
            )
        )
        LineChartView(
            currencies = listOf(
                Currency(
                    currencyName = "Aldo Hudson",
                    currentPriceText = "0.3333",
                    currentPrice = 0.3333,
                    priceChangePercent = 20.21,
                    priceChangeLast = 22.23,
                    quoteVolume = 24.25,
                    lastTimeUpdate = "unum",
                    timestamp = 4950,
                    priceChangePercentText = "etiam",
                )
            )
        )
    }
}