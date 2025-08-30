package com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vandrushko.feature_crypto_currency.R
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.components.CryptoCurrency
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.components.SortToggleButton
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.event.HomeScreenEvent
import com.vandrushko.feature_crypto_currency.presentation.crypto_currency_home_screen.vm.HomeScreenViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun FindCryptoField(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(),
) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val currencies = state.findResultCurrencies

    LaunchedEffect(query) {
        snapshotFlow { query }.debounce(300).distinctUntilChanged().collect { text ->
            if (text.isNotBlank()) {
                viewModel.emit(HomeScreenEvent.FindBySymbol(text))
            }
        }
    }

    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                placeholder = {
                    Text(stringResource(R.string.search_currency))
                },
                suffix = {
                    IconButton(
                        onClick = {
                            query = ""
                        }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                singleLine = true
            )
            SortToggleButton(
                sortOption = state.sortOption,
                onSortChange = {
                    viewModel.emit(HomeScreenEvent.ChangeSortOption(it))
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp, end = 16.dp)
                    .size(24.dp)
            )
        }

        if (query.isNotBlank() && currencies.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.search_results),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(currencies) { currency ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CryptoCurrency(
                                    currency = currency,
                                    onClick = { },
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.emit(HomeScreenEvent.AddToFavourite(currency))
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.add_to_favourite),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}