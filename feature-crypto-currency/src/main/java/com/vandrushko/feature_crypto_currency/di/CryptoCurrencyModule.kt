package com.vandrushko.feature_crypto_currency.di

import android.content.Context
import androidx.room.Room
import com.vandrushko.core.di.remote.RestClient
import com.vandrushko.core.di.remote.WebSocketClient
import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.feature_crypto_currency.data.local.CurrencyDatabase
import com.vandrushko.feature_crypto_currency.data.remote.rest.BinanceRestApi
import com.vandrushko.feature_crypto_currency.data.remote.rest.RestCurrencyMarkerApi
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.WebSocketBinanceApi
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.WebSocketCurrencyMarketApi
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.FindCurrenciesBySymbolUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetAllTickerUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.GetFavouriteCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.MarkCurrencyAsFavouriteUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.RemoveCurrencyFromFavouriteUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.SubscribeMultipleCryptoCurrenciesUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.SubscribeToOneCurrencyUseCase
import com.vandrushko.feature_crypto_currency.domain.crypto_currency_home_screen.usecase.WatchCurrencyHistoryUseCase
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepository
import com.vandrushko.feature_crypto_currency.domain.repository.CryptoCurrencyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoCurrencyModule {

    @Provides
    @Singleton
    fun provideWebSocketCurrencyMarketApi(@WebSocketClient client: HttpClient): WebSocketCurrencyMarketApi =
        WebSocketBinanceApi(client = client, CoroutineScope(Dispatchers.IO))

    @Provides
    @Singleton
    fun provideRestCurrencyMarkerApi(@RestClient httpClient: HttpClient): RestCurrencyMarkerApi =
        BinanceRestApi(
            httpClient = httpClient
        )

    @Provides
    @Singleton
    fun provideCurrencyDatabase(
        @ApplicationContext context: Context
    ): CurrencyDatabase = Room.databaseBuilder(
        context = context, klass = CurrencyDatabase::class.java, name = "currency_db"
    ).build()

    @Provides
    @Singleton
    fun provideCryptoCurrencyRepository(
        db: CurrencyDatabase, api: RestCurrencyMarkerApi, apiWebSockets: WebSocketCurrencyMarketApi,
        notificationsRepository: NotificationsRepository
    ): CryptoCurrencyRepository =
        CryptoCurrencyRepositoryImpl(db = db, apiRest = api, apiWebSockets = apiWebSockets,
            notificationsRepository ,CoroutineScope(Dispatchers.IO))

    @Provides
    @Singleton
    fun provideGetFirstNMessagesUseCase(
        repository: CryptoCurrencyRepository
    ): GetFavouriteCurrenciesUseCase = GetFavouriteCurrenciesUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideGetAllTickerUseCase(
        repository: CryptoCurrencyRepository
    ): GetAllTickerUseCase = GetAllTickerUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideSubscribeMultipleCryptoCurrenciesUseCase(
        repository: CryptoCurrencyRepository
    ): SubscribeMultipleCryptoCurrenciesUseCase =
        SubscribeMultipleCryptoCurrenciesUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideSubscribeWatchCurrencyHistoryUseCase(
        repository: CryptoCurrencyRepository
    ): WatchCurrencyHistoryUseCase =
        WatchCurrencyHistoryUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideMarkCurrencyAsFavourite(
        repository: CryptoCurrencyRepository
    ): MarkCurrencyAsFavouriteUseCase =
        MarkCurrencyAsFavouriteUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideFindCurrenciesBySymbolUseCase(
        repository: CryptoCurrencyRepository
    ): FindCurrenciesBySymbolUseCase =
        FindCurrenciesBySymbolUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideSubscribeToOneCurrency(
        repository: CryptoCurrencyRepository
    ): SubscribeToOneCurrencyUseCase =
        SubscribeToOneCurrencyUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideRemoveCurrencyFromFavouriteUseCase(
        repository: CryptoCurrencyRepository
    ): RemoveCurrencyFromFavouriteUseCase =
        RemoveCurrencyFromFavouriteUseCase(repository = repository)
}