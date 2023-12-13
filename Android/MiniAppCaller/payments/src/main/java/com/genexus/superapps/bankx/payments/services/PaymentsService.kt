package com.genexus.superapps.bankx.payments.services

import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import java.util.UUID

object PaymentsService {
    private const val SDT_PAYMENT_INFORMATION = "PaymentInformation"
    private const val SDT_PAYMENT_INFORMATION_ITEM_BRAND = "brand"
    private const val SDT_PAYMENT_INFORMATION_ITEM_AFFINITY = "affinity"
    private const val SDT_PAYMENT_INFORMATION_ITEM_TYPE = "type"

    fun pay(amount: Double, miniAppId: String?): String {
        val sb = StringBuilder(amount.toString()).apply {
            append("-")
            append(UUID.randomUUID().toString())
            append("-")
            if (!miniAppId.isNullOrEmpty())
                append(miniAppId)
        }

        return sb.toString()
    }

    fun getPaymentInformationList(clientInformation: Entity): EntityList {
        val clientId = clientInformation.optStringProperty(ClientsService.SDT_CLIENT_ID)
        val paymentInformationList = EntityList()
        paymentInformationList.itemType = Expression.Type.SDT
        for (i in 1..3) {
            val paymentInfoItem = EntityFactory.newSdt(SDT_PAYMENT_INFORMATION)
            paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_BRAND, "Brand $i for $clientId")
            paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY, "Affinity $i for $clientId")
            paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_TYPE, "Type $i for $clientId")
            paymentInformationList.add(paymentInfoItem)
        }
        return paymentInformationList
    }

    fun getPaymentInfoAffinity(paymentInfo: Entity): String {
        return paymentInfo.optStringProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY)
    }
}