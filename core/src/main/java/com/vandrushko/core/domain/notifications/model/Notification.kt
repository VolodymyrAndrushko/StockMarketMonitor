package com.vandrushko.core.domain.notifications.model

import com.vandrushko.core.data.local.notifications.entity.NotificationEntity

data class Notification(
    val id: Int = 0,
    val message: String,
    val timestamp: Long,
    val isViewed: Boolean = false
)

fun Notification.toNotificationEntity(): NotificationEntity =
    NotificationEntity(
        id = id, message = message, timestamp = timestamp, isViewed = false
    )