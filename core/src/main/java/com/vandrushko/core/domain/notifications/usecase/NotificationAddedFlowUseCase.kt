package com.vandrushko.core.domain.notifications.usecase

import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.notifications.model.Notification
import kotlinx.coroutines.flow.SharedFlow

class NotificationAddedFlowUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    operator fun invoke(): SharedFlow<Notification> = notificationsRepository.notificationAdded
}