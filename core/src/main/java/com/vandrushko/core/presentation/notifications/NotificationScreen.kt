package com.vandrushko.core.presentation.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vandrushko.core.R
import com.vandrushko.core.presentation.notifications.component.NotificationItem
import com.vandrushko.core.presentation.notifications.event.NotificationsEvent
import com.vandrushko.core.presentation.notifications.vm.NotificationsViewModel
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    if (state.notifications.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_notifications))
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(
                state.notifications, key = { _, item -> item.id }) { index, notification ->

                LaunchedEffect(notification.id) {
                    if (!notification.isViewed) {
                        coroutineScope.launch {
                            viewModel.emit(NotificationsEvent.SetNotificationAsViewed(notification))
                        }
                    }
                }

                NotificationItem(
                    index = index,
                    notification = notification,
                    onDelete = { viewModel.emit(NotificationsEvent.DeleteNotification(notification)) })
            }

            item {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}