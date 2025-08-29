package com.vandrushko.core.domain.settings.usecase

import com.vandrushko.core.domain.settings.SettingsRepository
import com.vandrushko.core.domain.settings.model.UpdatePeriod

class SetRefreshTimeUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(updatePeriod: UpdatePeriod) {
        repository.setUpdatePeriod(updatePeriod)
    }
}