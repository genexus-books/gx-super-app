package com.genexus.example_superapp

import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.superapps.MiniApp
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class FlutterCallHandler: MethodChannel.MethodCallHandler {

	private val provisioning = ProvisioningAPI()

	override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
		when (call.method) {
			METHOD_GET_MINI_APPS -> {
				val tag = call.argument<String?>(ARGUMENT_TAG) ?: Strings.EMPTY
				provisioning.getMiniApps(tag, result)
			}
			METHOD_GET_MINI_APPS_CACHED -> provisioning.getCachedMiniApps(result)
			METHOD_REMOVE -> {
				val miniAppJson = call.argument<String?>(ARGUMENT_MINI_APP_JSON)
				if (miniAppJson.isNullOrEmpty()) {
					result.error(ProvisioningAPI.ERROR_ARGUMENTS, "MiniApp json cannot be null or empty", null)
					return
				}

				val miniAppInfo = JSONObject(miniAppJson)
				val id = miniAppInfo.optString(FIELD_ID)
				val version = miniAppInfo.optInt(FIELD_VERSION)
				if (id.isNullOrEmpty() || version == -1) { //MiniApp.INVALID_VERSION) {
					result.error(ProvisioningAPI.ERROR_ARGUMENTS, "Cannot delete MiniApp as it contains incorrect information", null)
					return
				}

				provisioning.remove(id, version, result)
			}
			METHOD_LOAD -> {
				val miniAppJson = call.argument<String?>(ARGUMENT_MINI_APP_JSON)
				if (miniAppJson.isNullOrEmpty()) {
					result.error(ProvisioningAPI.ERROR_ARGUMENTS, "MiniApp json cannot be null or empty", null)
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
					this.type = MiniApp.Type.valueOf(miniAppInfo.optString(FIELD_TYPE))
				}

				provisioning.load(miniApp, result)
			}
			else -> {
				result.notImplemented()
			}
		}
	}

	companion object {
		private const val METHOD_GET_MINI_APPS = "getMiniApps"
		private const val METHOD_GET_MINI_APPS_CACHED = "getCachedMiniApps"
		private const val METHOD_LOAD = "load"
		private const val METHOD_REMOVE = "remove"
		private const val ARGUMENT_TAG = "tag"
		private const val ARGUMENT_MINI_APP_JSON = "miniAppJson"

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
