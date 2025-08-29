package com.vandrushko.core.presentation.settings.vm

import androidx.lifecycle.ViewModel
import com.vandrushko.core.domain.settings.usecase.RefreshTimeStateUseCase
import com.vandrushko.core.domain.settings.usecase.SetRefreshTimeUseCase
import com.vandrushko.core.presentation.settings.event.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val refreshTimeState: RefreshTimeStateUseCase,
    private val setTimeRefreshPeriod: SetRefreshTimeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(refreshTimeState().value)
    val state = _state.asStateFlow()

    fun emit(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ChangeGraphUpdatePeriod -> {
                setTimeRefreshPeriod(event.updatePeriod)
                _state.update { event.updatePeriod }
            }
        }
    }
}