package com.genexus.example_superapp.api.services

import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import kotlin.collections.HashMap

object PaymentsService {
	private const val SDT_PAYMENT_INFORMATION = "PaymentInformation"
	private const val SDT_PAYMENT_INFORMATION_ITEM_BRAND = "brand"
	private const val SDT_PAYMENT_INFORMATION_ITEM_AFFINITY = "affinity"
	private const val SDT_PAYMENT_INFORMATION_ITEM_TYPE = "type"

	fun toEntityList(list: List<Map<String, String>>): EntityList {
		val entityList = EntityList().apply { itemType = Expression.Type.SDT }
		if (list.isEmpty())
			return entityList

		for (map in list) {
			val entity = if (Services.Application.miniApp?.type == MiniApp.Type.Native) {
                EntityFactory.newSdt(SDT_PAYMENT_INFORMATION)
            } else {
                EntityFactory.newEntity()
            }
			
			entity.apply {
				setProperty(SDT_PAYMENT_INFORMATION_ITEM_BRAND, map[SDT_PAYMENT_INFORMATION_ITEM_BRAND])
				setProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY, map[SDT_PAYMENT_INFORMATION_ITEM_AFFINITY])
				setProperty(SDT_PAYMENT_INFORMATION_ITEM_TYPE, map[SDT_PAYMENT_INFORMATION_ITEM_TYPE])
			}

			entityList.addEntity(entity)
		}

		return entityList
	}

	fun toMap(entity: Entity): HashMap<String, String> {
		return hashMapOf(
			SDT_PAYMENT_INFORMATION_ITEM_BRAND to entity.getProperty(SDT_PAYMENT_INFORMATION_ITEM_BRAND) as String,
			SDT_PAYMENT_INFORMATION_ITEM_AFFINITY to entity.getProperty(SDT_PAYMENT_INFORMATION_ITEM_AFFINITY) as String,
			SDT_PAYMENT_INFORMATION_ITEM_TYPE to entity.getProperty(SDT_PAYMENT_INFORMATION_ITEM_TYPE) as String
		)
	}
}
