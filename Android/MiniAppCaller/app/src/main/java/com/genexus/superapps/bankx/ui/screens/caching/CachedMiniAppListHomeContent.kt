package com.genexus.superapps.bankx.ui.screens.caching

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.genexus.android.core.application.LifecycleListeners
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.superapps.bankx.ui.MiniAppListItem
import com.genexus.superapps.bankx.ui.screens.States.ErrorState
import com.genexus.superapps.bankx.ui.screens.States.LoadingState
import com.genexus.superapps.bankx.viewmodel.caching.CacheViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CachedMiniAppListHomeContent(model: CacheViewModel) {
    registerCallback(model)
    when (val state = model.state.collectAsState().value) {
        is CacheViewModel.State.Loading -> {
            LoadingState()
        }
        is CacheViewModel.State.Error -> {
            ErrorState(text = state.message)
        }
        is CacheViewModel.State.Data -> {
            val miniApps = remember { state.data }
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(items = miniApps) {
                    Surface(modifier = Modifier.animateItemPlacement()) {
                        MiniAppListItem(miniApp = it) { model.deleteMiniApp(it) }
                    }
                }
            }
        }
    }
}

private fun registerCallback(model: CacheViewModel) {
    Services.Application.lifecycle.registerMiniApplicationLifecycleListener(object : LifecycleListeners.MiniApp {
        override fun onMiniAppStopped(miniApp: MiniApp) { model.refresh() }
        override fun onMiniAppException(miniApp: MiniApp, t: Throwable) { }
        override fun onMiniAppStarted(miniApp: MiniApp) { }
    })
}
