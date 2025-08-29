package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vandrushko.core.data.utils.formatTimestampToHHMMSS
import com.vandrushko.feature_crypto_currency.domain.model.Currency
import java.text.DecimalFormat

@Composable
fun CryptoCurrency(currency: Currency,  modifier: Modifier = Modifier, onClick: (currency: Currency)-> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClick(currency) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = currency.currencyName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Last update: ${currency.lastTimeUpdate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${currency.currentPrice}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            val changeColor = if (currency.priceChangePercent >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)

            Text(
                text = "${DecimalFormat("0.#####").format(currency.priceChangePercent)}%",
                style = MaterialTheme.typography.bodySmall,
                color = changeColor
            )
        }
    }
}

@Preview
@Composable
private fun CryptoCurrencyPreview() {
    CryptoCurrency(Currency(
        currencyName = "Parker Kirk",
        currentPriceText = "0.00000916",
        currentPrice = 0.00000916,
        priceChangePercent = 0.00003,
//        priceChangePercent = 2.963917525773198,
        priceChangeLast = 22.23,
        quoteVolume = 24.25,
        lastTimeUpdate = formatTimestampToHHMMSS(System.currentTimeMillis()),
        timestamp = System.currentTimeMillis()
    ), onClick = {})
}