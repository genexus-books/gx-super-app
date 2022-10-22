package com.genexus.superapps.bankx.payments

import android.content.Context
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.externalapi.ExternalApiDefinition

class PaymentsModule : GenexusModule {
    override fun initialize(context: Context) {
        ExternalApiFactory.addApi(ExternalApiDefinition(PaymentsApi.NAME, PaymentsApi::class.java, false))
    }
}
