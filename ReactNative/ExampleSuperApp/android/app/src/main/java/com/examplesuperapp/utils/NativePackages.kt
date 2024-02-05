package com.examplesuperapp.utils

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.examplesuperapp.ProvisioningAPI
class NativePackages : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<ProvisioningAPI> {
        return listOf(
            //list here all the native modules present in this project
            ProvisioningAPI(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}