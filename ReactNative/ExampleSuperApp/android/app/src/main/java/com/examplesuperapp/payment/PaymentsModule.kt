package com.examplesuperapp.payment

import android.content.Context
import com.genexus.android.core.base.metadata.superapp.api.SuperAppApi
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.externalapi.ExternalApiDefinition

class PaymentsModule : GenexusModule {
    override fun initialize(context: Context) {
        val def = ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi::class.java, false)
        Services.SuperApps.Api = SuperAppApi(def)
    }
}
