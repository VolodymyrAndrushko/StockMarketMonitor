package com.vandrushko.core.domain

import com.vandrushko.core.data.local.CoreDatabase
import com.vandrushko.core.data.local.notifications.entity.toNotification
import com.vandrushko.core.domain.notifications.model.Notification
import com.vandrushko.core.domain.notifications.model.toNotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class NotificationsRepositoryImpl(
    private val database: CoreDatabase
) : NotificationsRepository {
    private val _notificationAdded = MutableSharedFlow<Notification>()
    override val notificationAdded = _notificationAdded.asSharedFlow()

    override suspend fun saveNotificationLocally(notification: Notification) {
        database.notificationsDao.insert(notification = notification.toNotificationEntity())
        _notificationAdded.emit(notification)
    }

    override suspend fun deleteNotification(notification: Notification) {
        database.notificationsDao.delete(notification.id)
    }

    override fun getAllNotifications(): Flow<List<Notification>> {
        return database.notificationsDao.getAll().map { it.map { it.toNotification() } }
    }

    override suspend fun setAsViewed(notification: Notification) {
        database.notificationsDao.updateIsViewed(notification.id)
    }
}