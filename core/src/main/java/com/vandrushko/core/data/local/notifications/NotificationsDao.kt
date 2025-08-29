package com.vandrushko.core.data.local.notifications

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vandrushko.core.data.local.notifications.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Query("DELETE FROM NotificationEntity WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("UPDATE NotificationEntity SET isViewed = 1 WHERE id = :id")
    suspend fun updateIsViewed(id: Int)

    @Query("SELECT * FROM NotificationEntity ORDER BY timestamp DESC")
    fun getAll(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM NotificationEntity WHERE isViewed = 0")
    suspend fun uncheckedCountPresent(): Int
}