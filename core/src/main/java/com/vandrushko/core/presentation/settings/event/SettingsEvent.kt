package com.vandrushko.core.presentation.settings.event

import com.vandrushko.core.domain.settings.model.UpdatePeriod

sealed interface SettingsEvent {
    data class ChangeGraphUpdatePeriod(val updatePeriod: UpdatePeriod) : SettingsEvent
}