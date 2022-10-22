package com.genexus.superapps.bankx.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.genexus.superapps.bankx.ui.MiniAppListItem
import com.genexus.superapps.bankx.ui.screens.States.ErrorState
import com.genexus.superapps.bankx.ui.screens.States.LoadingState
import com.genexus.superapps.bankx.viewmodel.main.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MiniAppListHomeContent(model: MainViewModel) {
    val isRefreshing by model.isRefreshing.collectAsState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { model.refresh() },
    ) {
        when (val state = model.state.collectAsState().value) {
            is MainViewModel.State.Loading -> {
                LoadingState()
            }
            is MainViewModel.State.Error -> {
                ErrorState(text = state.message)
            }
            is MainViewModel.State.Data -> {
                val miniApps = remember { state.data }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        items = miniApps,
                        itemContent = { MiniAppListItem(miniApp = it) { model.loadMiniApp(it) } }
                    )
                }
            }
        }
    }
}
