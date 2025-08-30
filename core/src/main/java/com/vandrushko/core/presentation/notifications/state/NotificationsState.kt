package com.vandrushko.core.presentation.notifications.state

import com.vandrushko.core.domain.notifications.model.Notification

data class NotificationsState(
    val notifications: List<Notification> = listOf(), val lastNotification: Notification? = null
)
