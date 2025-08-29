package com.vandrushko.core.domain.settings

import com.vandrushko.core.domain.settings.model.UpdatePeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepositoryImpl: SettingsRepository {
    private val _graphUpdatePeriod= MutableStateFlow(UpdatePeriod.TWO_SECOND)
    override val graphUpdatePeriod: StateFlow<UpdatePeriod> = _graphUpdatePeriod.asStateFlow()


    override fun setUpdatePeriod(updatePeriod: UpdatePeriod) {
        _graphUpdatePeriod.update { updatePeriod }
    }
}