package com.vandrushko.core.domain.notifications.usecase

import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.notifications.model.Notification

class CreateAndSaveLocalNotificationUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(notification: Notification) {
        repository.saveNotificationLocally(notification)
    }
}