package com.examplesuperapp

import android.app.Application
import android.content.res.Configuration
import com.examplesuperapp.application.AppEntityDataProvider
import com.examplesuperapp.application.AppEntityService
import com.examplesuperapp.utils.NativePackages
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader
import com.genexus.android.controls.grids.smart.SmartGridModule
import com.genexus.android.core.application.ApplicationHelper
import com.genexus.android.core.base.services.IEntityProvider
import com.genexus.android.core.base.services.LogLevel
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalobjects.CoreExternalObjectsModule
import com.genexus.android.core.providers.EntityDataProvider
import com.genexus.android.core.services.EntityService
import com.genexus.android.core.usercontrols.CoreUserControlsModule
import com.genexus.android.superapps.SuperAppsModule
import com.examplesuperapp.payment.PaymentsModule

class MainApplication : Application(), IEntityProvider, ReactApplication {
  private val applicationHelper = ApplicationHelper(this, this)
  override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
          PackageList(this).packages.apply {
            // Packages that cannot be autolinked yet can be added manually here, for example:
            // packages.add(new MyReactNativePackage());
            add(NativePackages())
          }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
      }

  override val reactHost: ReactHost
    get() = getDefaultReactHost(applicationContext, reactNativeHost)

  override fun onCreate() {
    super.onCreate()
    //Modules provided by GeneXus
    applicationHelper.registerModule(CoreExternalObjectsModule())
    applicationHelper.registerModule(CoreUserControlsModule())
    applicationHelper.registerModule(SmartGridModule())
    applicationHelper.registerModule(SuperAppsModule())

    //Module provided by the native SuperApp
    applicationHelper.registerModule(PaymentsModule())

    applicationHelper.onCreate()

    //Turn MiniApps logging on
    Services.Log.setLevel(LogLevel.DEBUG)


    SoLoader.init(this, false)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    applicationHelper.onConfigurationChanged(newConfig)
  }

  override fun getEntityServiceClass(): Class<out EntityService> {
    return AppEntityService::class.java
  }

  override fun getProvider(): EntityDataProvider {
    return AppEntityDataProvider()
  }
}
