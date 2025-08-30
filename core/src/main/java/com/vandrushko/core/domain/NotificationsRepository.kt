package com.vandrushko.core.domain

import com.vandrushko.core.domain.notifications.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface NotificationsRepository {
    val notificationAdded: SharedFlow<Notification>
    suspend fun saveNotificationLocally(notification: Notification)
    suspend fun deleteNotification(notification: Notification)
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun setAsViewed(notification: Notification)
}