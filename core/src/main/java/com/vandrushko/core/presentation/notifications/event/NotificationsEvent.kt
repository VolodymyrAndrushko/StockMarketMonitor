package com.vandrushko.core.presentation.notifications.event

import com.vandrushko.core.domain.notifications.model.Notification

sealed interface NotificationsEvent {
    data class DeleteNotification(val notification: Notification): NotificationsEvent
    data class SetNotificationAsViewed(val notification: Notification): NotificationsEvent
}