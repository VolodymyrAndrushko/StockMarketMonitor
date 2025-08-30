package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.vandrushko.core.presentation.ui.theme.StockMarketMonitorTheme
import com.vandrushko.feature_crypto_currency.R
import com.vandrushko.feature_crypto_currency.domain.model.SortOptions

@Composable
fun SortToggleButton(
    sortOption: SortOptions,
    onSortChange: (SortOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = sortOption != SortOptions.DEFAULT,
        onCheckedChange = {
            val next = when (sortOption) {
                SortOptions.DEFAULT -> SortOptions.L_TO_H
                SortOptions.L_TO_H -> SortOptions.H_TO_L
                SortOptions.H_TO_L -> SortOptions.DEFAULT
            }
            onSortChange(next)
        },
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.small
            )
    ) {
        val iconColor = when (sortOption) {
            SortOptions.DEFAULT -> Color.Gray
            SortOptions.L_TO_H -> Color.Red
            SortOptions.H_TO_L -> Color.Green
        }
        val scaleX = if (sortOption == SortOptions.L_TO_H) -1f else 1f

        Icon(
            painterResource(R.drawable.ic_sort_arrows_com),
            contentDescription = "Sort by price",
            tint = iconColor,
            modifier = Modifier.scale(scaleX = scaleX, scaleY = 1f)
        )
    }
}

@Preview
@Composable
private fun SortToggleButtonPreview() {
    StockMarketMonitorTheme {
        Column {
            SortToggleButton(
                sortOption = SortOptions.DEFAULT, onSortChange = {},
            )
            SortToggleButton(
                sortOption = SortOptions.H_TO_L, onSortChange = {},
            )
            SortToggleButton(
                sortOption = SortOptions.L_TO_H, onSortChange = {},
            )
        }
    }
}