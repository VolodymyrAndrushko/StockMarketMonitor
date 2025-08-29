package com.vandrushko.core.domain.settings.usecase

import com.vandrushko.core.domain.settings.SettingsRepository
import com.vandrushko.core.domain.settings.model.UpdatePeriod
import kotlinx.coroutines.flow.StateFlow


class RefreshTimeStateUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(): StateFlow<UpdatePeriod> = repository.graphUpdatePeriod
}