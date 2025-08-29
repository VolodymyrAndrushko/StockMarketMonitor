package com.vandrushko.core.domain.settings

import com.vandrushko.core.domain.settings.model.UpdatePeriod
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val graphUpdatePeriod: StateFlow<UpdatePeriod>

    fun setUpdatePeriod(updatePeriod: UpdatePeriod)
}