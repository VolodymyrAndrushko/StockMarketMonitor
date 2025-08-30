package com.vandrushko.core.presentation.notifications.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vandrushko.core.domain.notifications.usecase.DeleteNotificationUseCase
import com.vandrushko.core.domain.notifications.usecase.GetAllNotificationsUseCase
import com.vandrushko.core.domain.notifications.usecase.NotificationAddedFlowUseCase
import com.vandrushko.core.domain.notifications.usecase.SetNotificationAsViewedUseCase
import com.vandrushko.core.presentation.notifications.event.NotificationsEvent
import com.vandrushko.core.presentation.notifications.state.NotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val deleteNotification: DeleteNotificationUseCase,
    private val setNotificationAsViewed: SetNotificationAsViewedUseCase,
    private val getAllNotifications: GetAllNotificationsUseCase,
    notificationAddedFlow: NotificationAddedFlowUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAllNotifications().collectLatest { notifications ->
                _state.update {
                    it.copy(
                        notifications = notifications
                    )
                }
            }
        }
    }

    val onNewNotification = notificationAddedFlow()
    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    fun emit(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.DeleteNotification -> {
                viewModelScope.launch {
                    deleteNotification(event.notification)
                }
            }

            is NotificationsEvent.SetNotificationAsViewed -> {
                viewModelScope.launch {
                    setNotificationAsViewed(event.notification)
                }
            }
        }
    }
}