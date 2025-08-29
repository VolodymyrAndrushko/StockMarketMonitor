package com.vandrushko.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vandrushko.core.data.local.notifications.NotificationsDao
import com.vandrushko.core.data.local.notifications.entity.NotificationEntity


@Database(
    entities = [NotificationEntity::class], version = 1
)
abstract class CoreDatabase : RoomDatabase() {
    abstract val notificationsDao: NotificationsDao
}