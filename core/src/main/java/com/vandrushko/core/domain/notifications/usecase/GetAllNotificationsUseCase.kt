package com.vandrushko.core.domain.notifications.usecase

import com.vandrushko.core.domain.NotificationsRepository
import com.vandrushko.core.domain.notifications.model.Notification
import kotlinx.coroutines.flow.Flow

class GetAllNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    operator fun invoke(): Flow<List<Notification>> = notificationsRepository.getAllNotifications()
}