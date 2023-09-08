package com.genexus.example_superapp.api

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule

class PaymentsModule: GenexusModule {
	override fun initialize(appContext: Context?) {
		ExternalApiFactory.addApi(ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi::class.java, false))
	}
}
