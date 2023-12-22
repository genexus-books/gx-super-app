package com.examplesuperapp

import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class ProvisioningAPI(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()
    private var _isRefreshing = MutableStateFlow(false)
    var isRefreshing = _isRefreshing.asStateFlow()
    override fun getName(): String = MODULE_NAME

    @ReactMethod
    fun handleMethodCall(call: ReadableMap, result: Callback) {
        val method = call.getString("method")
        if (method == null) {
            result.invoke(listOf(mapOf("error" to "Invalid method")))
            return
        }

        when (method) {
            Constants.Methods.GET_MINI_APPS -> {
                val arguments = call.getMap("arguments")
                val tag = arguments?.getString(Constants.Arguments.TAG)

                if (tag == null) {
                    result.invoke(listOf(mapOf("error" to "Invalid arguments")))
                    return
                }

                getMiniApps(tag, result)
            }

            Constants.Methods.GET_MINI_APPS_CACHED -> getCachedMiniApps(result)

            Constants.Methods.LOAD -> {
                val arguments = call.getMap("arguments")
                val miniAppJson = arguments?.getString(Constants.Arguments.MINI_APP_JSON)
                if (miniAppJson.isNullOrEmpty()) {
                    result.invoke(listOf(mapOf("error" to  "MiniApp json cannot be null or empty")))
                    return
                }

                val miniAppInfo = JSONObject(miniAppJson)
                val id = miniAppInfo.optString(FIELD_ID)
                val version = miniAppInfo.optInt(FIELD_VERSION)

                val miniApp = MiniApp(id, version).apply {
                    this.appEntry = miniAppInfo.optString(FIELD_ENTRY_POINT)
                    this.apiUri = miniAppInfo.optString(FIELD_SERVICES_URL)
                    this.metadataRemoteUrl = miniAppInfo.optString(FIELD_METADATA)
                    this.signature = miniAppInfo.optString(FIELD_SIGNATURE)
                    this.name = miniAppInfo.optString(FIELD_NAME)
                    this.iconUrl = miniAppInfo.optString(FIELD_ICON)
                    this.bannerUrl = miniAppInfo.optString(FIELD_BANNER)
                    this.cardUrl = miniAppInfo.optString(FIELD_CARD)
                    this.description = miniAppInfo.optString(FIELD_DESCRIPTION)
                }

                load(miniApp, result)
            }

            else -> {
                result.invoke(listOf(mapOf("error" to "Invalid method")))
            }
        }
    }
    private fun getMiniApps(tag: String, result: Callback) {
        Services.SuperApps.searchByTag(tag, 0, 10).addOnCompleteListener(
            object : OnCompleteListener<MiniAppCollection, SearchError> {
                override fun onComplete(task: Task<MiniAppCollection, SearchError>) {
                    if (!task.isSuccessful) {
                        _state.value = State.Error("Couldn't retrieve MiniApps from server, \" +\n" +
                                "                                \"cause '${task.error.toString()}'")
                        return
                    }

                    val miniApps = task.result
                    if (miniApps == null) {
                        _state.value = State.Error("Couldn't retrieve MiniApps from server, \" +\n" +
                                "                                \"cause '${task.error.toString()}'")
                        return
                    }

                    val json = toJson(miniApps)
                    _state.value = State.Data(miniApps)
                    result(json)
                }
            }
        )
    }

    private fun getCachedMiniApps(result: Callback) {
        val collection = Services.SuperApps.getCachedMiniApps()
        val json = toJson(collection)
        result(json)
    }

    private fun load(miniApp: MiniApp, result: Callback) {
        Services.SuperApps.load(miniApp).addOnCompleteListener(
            object : OnCompleteListener<Boolean, LoadError> {
                override fun onComplete(task: Task<Boolean, LoadError>) {
                    if (!task.isSuccessful) {
                       _state.value = State.Error("Couldn't load MiniApp '${miniApp.id}', \" +\n" +
                                "                                \"cause '${task.error.toString()}'")
                        return
                    }

                    result(true)
                }
            }
        )
    }

    private fun remove(id: String, version: Int?, result: (Any)) {

    }

    private fun toJson(miniApps: List<MiniApp>): String {
        val collection = Services.Serializer.createCollection()
        if (miniApps.isNotEmpty()) {
            for (miniApp in miniApps) {
                val node = Services.Serializer.createNode().apply {
                    put(FIELD_ID, miniApp.id)
                    put(FIELD_NAME, miniApp.name)
                    put(FIELD_DESCRIPTION, miniApp.description)
                    put(FIELD_ICON, miniApp.iconUrl)
                    put(FIELD_BANNER, miniApp.bannerUrl)
                    put(FIELD_CARD, miniApp.cardUrl)
                    put(FIELD_METADATA, miniApp.metadataRemoteUrl)
                    put(FIELD_ENTRY_POINT, miniApp.appEntry)
                    put(FIELD_SERVICES_URL, miniApp.apiUri)
                    put(FIELD_SIGNATURE, miniApp.signature)
                    put(FIELD_VERSION, miniApp.version)
                }
                collection.put(node)
            }
        }

        return collection.toString()
    }
    private object Constants {
        object Methods {
            const val GET_MINI_APPS = "getMiniApps"
            const val GET_MINI_APPS_CACHED = "getCachedMiniApps"
            const val LOAD = "load"
            const val REMOVE = "remove"
            const val ARGUMENT_TAG = "tag"
            const val MINI_APP_JSON = "miniAppJson"
        }

        object Arguments {
            const val TAG = "tag"
            const val MINI_APP_JSON = "miniAppJson"
        }

        object MiniAppFields {
            const val ID = "Id"
            const val VERSION = "Version"
        }
    }
    sealed class State {
        object Loading: State()
        data class Data(val data: MiniAppCollection): State()
        data class Error(val message: String): State()
    }
    companion object {
        const val MODULE_NAME = "ProvisioningAPI"
        private const val FIELD_ID = "Id"
        private const val FIELD_NAME = "Name"
        private const val FIELD_DESCRIPTION = "Description"
        private const val FIELD_ICON = "Icon"
        private const val FIELD_BANNER = "Banner"
        private const val FIELD_CARD = "Card"
        private const val FIELD_METADATA = "Metadata"
        private const val FIELD_ENTRY_POINT = "EntryPoint"
        private const val FIELD_SERVICES_URL = "ServicesURL"
        private const val FIELD_SIGNATURE = "Signature"
        private const val FIELD_VERSION = "Version"
        private const val FIELD_TYPE = "Type"
    }
}
