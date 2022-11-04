package com.genexus.example_superapp

import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.Task
import io.flutter.plugin.common.MethodChannel

class ProvisioningAPI: IProvisioning {

	override fun getMiniApps(tag: String, result: MethodChannel.Result) {
		Services.SuperApps.searchByTag(tag, 0, 10).addOnCompleteListener(
			object : OnCompleteListener<MiniAppCollection, SearchError> {
				override fun onComplete(task: Task<MiniAppCollection, SearchError>) {
					if (!task.isSuccessful) {
						result.error(ERROR_GET, "Couldn't retrieve MiniApps from server, " +
								"cause '${task.error.toString()}' ", null)
						return
					}

					val miniApps = task.result
					if (miniApps == null) {
						result.error(ERROR_GET, "Couldn't retrieve MiniApps from server, " +
								"cause '${task.error.toString()}'", null)
						return
					}

					val json = toJson(miniApps)
					result.success(json)
				}
			}
		)
	}

	override fun getCachedMiniApps(result: MethodChannel.Result) {
		val collection = Services.SuperApps.getCachedMiniApps()
		val json = toJson(collection)
		result.success(json)
	}

	override fun load(miniApp: MiniApp, result: MethodChannel.Result) {
		Services.SuperApps.load(miniApp).addOnCompleteListener(
			object : OnCompleteListener<Boolean, LoadError> {
				override fun onComplete(task: Task<Boolean, LoadError>) {
					if (!task.isSuccessful) {
						result.error(ERROR_LOAD, "Couldn't load MiniApp '${miniApp.id}', " +
								"cause '${task.error.toString()}'", null)
						return
					}

					result.success(true)
				}
			}
		)
	}

	override fun remove(id: String, version: Int, result: MethodChannel.Result) {
		val deleted = Services.SuperApps.removeMiniApp(id, version)
		result.success(deleted)
	}

	private fun toJson(miniApps: List<MiniApp>): String {
		val collection = Services.Serializer.createCollection()
		if (miniApps.isNotEmpty()) {
			for (miniApp in miniApps) {
				val node = Services.Serializer.createNode().apply {
					put(MiniApp.FIELD_ID, miniApp.id)
					put(MiniApp.FIELD_NAME, miniApp.name)
					put(MiniApp.FIELD_DESCRIPTION, miniApp.description)
					put(MiniApp.FIELD_ICON, miniApp.iconUrl)
					put(MiniApp.FIELD_BANNER, miniApp.bannerUrl)
					put(MiniApp.FIELD_CARD, miniApp.cardUrl)
					put(MiniApp.FIELD_METADATA, miniApp.metadataRemoteUrl)
					put(MiniApp.FIELD_ENTRY_POINT, miniApp.appEntry)
					put(MiniApp.FIELD_SERVICES_URL, miniApp.apiUri)
					put(MiniApp.FIELD_SIGNATURE, miniApp.signature)
					put(MiniApp.FIELD_VERSION, miniApp.version)
				}
				collection.put(node)
			}
		}

		return collection.toString()
	}

	companion object {
		const val ERROR_ARGUMENTS = "1"
		const val ERROR_GET = "2"
		const val ERROR_LOAD = "3"
	}
}
