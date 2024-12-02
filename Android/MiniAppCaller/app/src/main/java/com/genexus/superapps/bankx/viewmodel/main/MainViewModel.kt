package com.genexus.superapps.bankx.viewmodel.main

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.common.ApplicationToken
import com.genexus.android.core.superapps.LoadingOptions
import com.genexus.android.core.superapps.LoadingSecurityOptions
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.OnApplicationTokenRequiredCallback
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.superapps.security.MiniAppScopesRequestResult
import com.genexus.android.core.superapps.security.MiniAppTokenRequestResult
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.OnFailureListener
import com.genexus.android.core.tasking.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()
    private var _isRefreshing = MutableStateFlow(false)
    var isRefreshing = _isRefreshing.asStateFlow()

    private val superAppTokenCallback = object : OnApplicationTokenRequiredCallback {
        override fun getApplicationToken(): ApplicationToken {
            val superAppTokenType = "Retrieve Super App token type somehow and set it here" // TODO
            val superAppToken = "Retrieve Super App access token somehow and set it here"   // TODO
            return ApplicationToken(superAppTokenType, superAppToken)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            retrieveMiniApps()
            _isRefreshing.value = false
        }
    }

    private fun retrieveMiniApps() {
        _state.value = State.Loading
        Services.SuperApps.searchByTag("", 0, 10).addOnCompleteListener(object : OnCompleteListener<MiniAppCollection, SearchError> {
            override fun onComplete(task: Task<MiniAppCollection, SearchError>) {
                val miniApps = task.result
                if (!task.isSuccessful || miniApps.isNullOrEmpty()) {
                    _state.value = State.Error("No applications found")
                    return
                }

                _state.value = State.Data(miniApps)
            }
        })
    }

    fun loadMiniApp(miniApp: MiniApp, hasScopesErrorOccurred: Boolean) {
        val options = if (!miniApp.isSecure)
            null
        else {
            val miniAppTokenRetrievalUrl = Uri.parse("Configure Mini App access token retrieval URL here or in superapp.json")  // This line is not needed if URL is set in superapp.json
            val securityOptions = LoadingSecurityOptions.Builder()
                .withAuthTokenCheckUrl(miniAppTokenRetrievalUrl) // This line is not needed if URL is set in superapp.json
                .withAuthTokenRetrievalCallback(superAppTokenCallback)
                .build()
            LoadingOptions.Builder().withSecurityOptions(securityOptions).build()
        }

        Services.SuperApps.load(miniApp, options).addOnFailureListener(object : OnFailureListener<LoadError> {
            override fun onFailure(error: LoadError, extra: Any?) {
                _state.value = when (error) {
                    LoadError.AUTHORIZATION_TOKEN -> {
                        val errorMessage = (extra as MiniAppTokenRequestResult).messages?.errorText 
                            ?: "MiniApp access token not valid"
                        State.Error(errorMessage)
                    }
                    LoadError.AUTHORIZATION_SCOPES ->  {
                        if (!hasScopesErrorOccurred)
                            State.Warning(error, extra)
                        else {
                            val errorMessage = (extra as MiniAppScopesRequestResult).messages?.errorText
                                ?: "Missing scopes have not been accepted"
                            State.Error(errorMessage)
                        }
                    }
                    else -> State.Error("MiniApp loading failed because of '$error'")
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    suspend fun loadSandbox(context: Context) {
        val task = Services.SuperApps.Prototyping.loadSandbox()
        if (task == null) {
            Services.Device.runOnUiThread {
                Toast.makeText(context, "Loading task is null", Toast.LENGTH_SHORT).show()
            }
            return
        }

        task.addOnCompleteListener(object : OnCompleteListener<Boolean, LoadError> {
            override fun onComplete(task: Task<Boolean, LoadError>) {
                if (!task.isSuccessful) {
                    Services.Device.runOnUiThread {
                        Toast.makeText(context, "Loading task failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    init { refresh() }

    sealed class State {
        object Loading: State()
        data class Data(val data: MiniAppCollection): State()
        data class Error(val message: String): State()
        data class Warning(val error: LoadError, val extra: Any?): State()
    }
}
