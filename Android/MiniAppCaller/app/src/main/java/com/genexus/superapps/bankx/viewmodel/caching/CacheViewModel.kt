package com.genexus.superapps.bankx.viewmodel.caching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCachedCollection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CacheViewModel: ViewModel() {
    sealed class State {
        object Loading: State()
        data class Data(val data: MiniAppCachedCollection): State()
        data class Error(val message: String): State()
    }

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            retrieveMiniApps()
        }
    }

    private suspend fun retrieveMiniApps() {
        delay(250)
        val miniApps = getCachedMiniApps()
        if (miniApps.isEmpty()) {
            _state.value = State.Error("No applications found")
            return
        }

        _state.value = State.Data(miniApps)
    }

    fun deleteMiniApp(miniApp: MiniApp) {
        val id = miniApp.id
        if (id.isNullOrEmpty())
            return

        viewModelScope.launch {
            if (Services.SuperApps.removeMiniApp(id, miniApp.version)) {
                _state.value = State.Loading
                retrieveMiniApps()
            }
        }
    }

    private fun getCachedMiniApps(): MiniAppCachedCollection {
        return Services.SuperApps.getCachedMiniApps()
    }
}
