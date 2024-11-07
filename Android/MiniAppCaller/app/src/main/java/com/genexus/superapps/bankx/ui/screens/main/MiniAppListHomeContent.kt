package com.genexus.superapps.bankx.ui.screens.main

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.security.MiniAppScopesRequestResult
import com.genexus.superapps.bankx.BuildConfig
import com.genexus.superapps.bankx.Flavor
import com.genexus.superapps.bankx.ui.MiniAppListItem
import com.genexus.superapps.bankx.ui.screens.scopes.RequestScopesContract
import com.genexus.superapps.bankx.ui.screens.States.ErrorState
import com.genexus.superapps.bankx.ui.screens.States.LoadingState
import com.genexus.superapps.bankx.viewmodel.main.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MiniAppListHomeContent(model: MainViewModel) {
    val context = LocalContext.current
    var currentlyLoadingMiniApp: MiniApp? = null
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted)
            CoroutineScope(Dispatchers.Default).launch { model.loadSandbox(context) }
        else
            Toast.makeText(context, "Camera permission is required for QR", Toast.LENGTH_LONG).show()
    }
    
    val scopeRequestLauncher = rememberLauncherForActivityResult(
        RequestScopesContract()
    ) { accepted: Boolean ->
        val miniApp = currentlyLoadingMiniApp
        if (!accepted || miniApp == null)
            Toast.makeText(context, "Missing scopes rejected", Toast.LENGTH_LONG).show()
        else {
            model.loadMiniApp(miniApp)
            currentlyLoadingMiniApp = null
        }
    }

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
                val miniApps = state.data
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        items = miniApps,
                        itemContent = {
                            MiniAppListItem(miniApp = it) {
                                currentlyLoadingMiniApp = it
                                model.loadMiniApp(it)
                            }
                        }
                    )
                }
            }
            is MainViewModel.State.Warning -> {
                val miniApp = currentlyLoadingMiniApp
                if (state.error != LoadError.AUTHORIZATION_SCOPES || miniApp == null) {
                    ErrorState(text = "Unknown warning state")
                } else {
                    val result = state.extra as? MiniAppScopesRequestResult
                    val scopes = result?.scopes
                    if (scopes.isNullOrEmpty()) {
                        ErrorState(text = result?.messages?.errorText ?: "Unknown missing scopes")
                    } else {
                        val input = RequestScopesContract.Input(miniApp.id, scopes)
                        scopeRequestLauncher.launch(input)
                    }
                }
            }
        }

        if (BuildConfig.FLAVOR == Flavor.SANDBOX && Services.SuperApps.Prototyping.isEnabled()) {
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    elevation = FloatingActionButtonDefaults.elevation(),
                    modifier = Modifier.padding(all = 16.dp).align(alignment = Alignment.BottomEnd),
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                ) {
                    Icon(Icons.Filled.Search, "Read QR")
                }
            }
        }
    }
}
