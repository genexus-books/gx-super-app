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

    suspend fun getClientInformation(clientId: String?): Entity {
        delay(1000)
        return suspendCoroutine {
            val clientInformation = EntityFactory.newSdt(SDT_CLIENT)
            clientInformation.setProperty(SDT_CLIENT_ID, clientId)
            clientInformation.setProperty(SDT_CLIENT_NAME, "Juan")
            clientInformation.setProperty(SDT_CLIENT_LAST_NAME, "Perez")
            clientInformation.setProperty(SDT_CLIENT_EMAIL, "jperez@example.com")
            it.resume(clientInformation)
        }
    }
}