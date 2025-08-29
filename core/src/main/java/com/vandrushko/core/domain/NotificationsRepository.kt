package com.vandrushko.core.domain

import com.vandrushko.core.domain.notifications.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    suspend fun saveNotificationLocally(notification: Notification)
    suspend fun deleteNotification(notification: Notification)
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun setAsViewed(notification: Notification)
}