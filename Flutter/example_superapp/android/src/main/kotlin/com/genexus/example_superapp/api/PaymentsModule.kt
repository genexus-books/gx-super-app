package com.genexus.example_superapp.api

import android.content.Context
import com.genexus.android.core.base.metadata.superapp.api.SuperAppApi
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.framework.GenexusModule

class PaymentsModule: GenexusModule {
	override fun initialize(appContext: Context?) {
		val def = ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi::class.java, false)
		Services.SuperApps.Api = SuperAppApi(def)
	}
}
