package com.genexus.superapps.bankx.application

import android.app.Application
import android.content.res.Configuration
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
import com.genexus.superapps.bankx.payments.PaymentsModule

class BankingApplication: Application(), IEntityProvider {

	private val applicationHelper = ApplicationHelper(this, this)

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