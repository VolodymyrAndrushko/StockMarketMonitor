package com.vandrushko.core.data.local.notifications.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vandrushko.core.domain.notifications.model.Notification

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    val timestamp: Long,
    val isViewed: Boolean = false
)

fun NotificationEntity.toNotification(): Notification =
    Notification(
        id = id, message = message, timestamp = timestamp, isViewed = isViewed
    )
