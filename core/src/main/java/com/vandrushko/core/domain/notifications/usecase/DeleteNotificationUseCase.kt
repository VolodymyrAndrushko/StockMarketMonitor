package com.vandrushko.core.domain.notifications.usecase

import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.notifications.model.Notification

class DeleteNotificationUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(notification: Notification) {
        notificationsRepository.deleteNotification(notification)
    }
}