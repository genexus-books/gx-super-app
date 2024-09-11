package com.genexus.superapps.bankx.payments.services

import android.util.Log
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import java.util.UUID

object PaymentsService {
    private const val SDT_PAYMENT_INFORMATION = "PaymentInformation"
    private const val SDT_PAYMENT_INFORMATION_ITEM_AFFINITY = "affinity"
    private const val SDT_PAYMENT_INFORMATION_ITEM_BRAND = "brand"
    private const val SDT_PAYMENT_INFORMATION_ITEM_TYPE = "type"

    fun pay(amount: Double): String {
        return amount.toString() + "-" + UUID.randomUUID().toString()
    }

    fun getPaymentInformationList(): EntityList {
        val paymentInformationList = EntityList()
        paymentInformationList.itemType = Expression.Type.SDT

        var paymentInfoItem = EntityFactory.newSdt(SDT_PAYMENT_INFORMATION)
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY, "Volar")
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_BRAND, "VISA")
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_TYPE, "Crédito")
        paymentInformationList.add(paymentInfoItem)

        paymentInfoItem = EntityFactory.newSdt(SDT_PAYMENT_INFORMATION)
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY, "Volar")
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_BRAND, "VISA")
        paymentInfoItem.setProperty(SDT_PAYMENT_INFORMATION_ITEM_TYPE, "Débito")
        paymentInformationList.add(paymentInfoItem)

        return paymentInformationList
    }

    fun getPaymentInfoAffinity(paymentInfo: Entity): String {
        return paymentInfo.optStringProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY)
    }
}