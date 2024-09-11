package com.genexus.superapps.bankx.payments.services

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ClientsService {

    private const val SDT_CLIENT = "Client"
    private const val SDT_CLIENT_NAME = "name"
    private const val SDT_CLIENT_LAST_NAME = "lastName"
    private const val SDT_CLIENT_EMAIL = "email"
    const val SDT_CLIENT_ID = "clientId"

    suspend fun getClientInformation(): Entity {
        delay(1000)
        return suspendCoroutine {
            val clientInformation = EntityFactory.newSdt(SDT_CLIENT)
            clientInformation.setProperty(SDT_CLIENT_ID, "274c3916-6e31-40f9-b0d5-532585c3c3mu")
            clientInformation.setProperty(SDT_CLIENT_NAME, "Test")
            clientInformation.setProperty(SDT_CLIENT_LAST_NAME, "MiniApp")
            clientInformation.setProperty(SDT_CLIENT_EMAIL, "test-miniapp@mail.com")
            it.resume(clientInformation)
        }
    }
}