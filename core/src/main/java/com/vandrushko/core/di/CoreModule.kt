package com.vandrushko.core.di

import android.content.Context
import androidx.room.Room
import com.vandrushko.core.data.local.CoreDatabase
import com.vandrushko.core.di.remote.RestClient
import com.vandrushko.core.di.remote.WebSocketClient
import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.NotificationsRepositoryImpl
import com.vandrushko.core.domain.notifications.usecase.CreateAndSaveLocalNotificationUseCase
import com.vandrushko.core.domain.notifications.usecase.DeleteNotificationUseCase
import com.vandrushko.core.domain.notifications.usecase.GetAllNotificationsUseCase
import com.vandrushko.core.domain.notifications.usecase.NotificationAddedFlowUseCase
import com.vandrushko.core.domain.notifications.usecase.SetNotificationAsViewedUseCase
import com.vandrushko.core.domain.settings.SettingsRepository
import com.vandrushko.core.domain.settings.SettingsRepositoryImpl
import com.vandrushko.core.domain.settings.usecase.RefreshTimeStateUseCase
import com.vandrushko.core.domain.settings.usecase.SetRefreshTimeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    @WebSocketClient
    fun provideWebSocketsClient(): HttpClient = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
            pingIntervalMillis = 20_000
        }
    }

    @Provides
    @Singleton
    @RestClient
    fun provideRestClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                })
        }
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(): SettingsRepository = SettingsRepositoryImpl()

    @Provides
    @Singleton
    fun provideGetRefreshTimeUseCase(
        repository: SettingsRepository
    ): RefreshTimeStateUseCase = RefreshTimeStateUseCase(repository)

    @Provides
    @Singleton
    fun provideSetRefreshTimeUseCase(
        repository: SettingsRepository
    ): SetRefreshTimeUseCase = SetRefreshTimeUseCase(repository)

    @Provides
    @Singleton
    fun provideCoreDatabase(
        @ApplicationContext context: Context
    ): CoreDatabase = Room.databaseBuilder(
        context = context, klass = CoreDatabase::class.java, name = "core_db"
    ).build()

    @Provides
    @Singleton
    fun provideNotificationsRepository(
        database: CoreDatabase
    ): NotificationsRepository = NotificationsRepositoryImpl(
        database = database
    )

    @Provides
    @Singleton
    fun provideCreateAndSaveLocalNotificationUseCase(repository: NotificationsRepository): CreateAndSaveLocalNotificationUseCase =
        CreateAndSaveLocalNotificationUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideGetAllNotificationsUseCase(repository: NotificationsRepository): GetAllNotificationsUseCase =
        GetAllNotificationsUseCase(notificationsRepository = repository)

    @Provides
    @Singleton
    fun provideDeleteNotificationUseCase(repository: NotificationsRepository): DeleteNotificationUseCase =
        DeleteNotificationUseCase(notificationsRepository = repository)

    @Provides
    @Singleton
    fun provideSetNotificationAsViewedUseCase(repository: NotificationsRepository): SetNotificationAsViewedUseCase =
        SetNotificationAsViewedUseCase(notificationsRepository = repository)

    @Provides
    @Singleton
    fun provideNotificationAddedFlowUseCase(repository: NotificationsRepository): NotificationAddedFlowUseCase =
        NotificationAddedFlowUseCase(notificationsRepository = repository)
}