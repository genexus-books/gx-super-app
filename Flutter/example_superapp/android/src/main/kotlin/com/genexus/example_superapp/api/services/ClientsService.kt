package com.genexus.example_superapp.api.services

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp

object ClientsService {

	private const val SDT_CLIENT = "Client"
	private const val SDT_CLIENT_NAME = "name"
	private const val SDT_CLIENT_LAST_NAME = "lastName"
	private const val SDT_CLIENT_EMAIL = "email"
	private const val SDT_CLIENT_ID = "clientId"

	fun toEntity(map: Map<String, String>): Entity {
		val entity = if (Services.Application.miniApp?.type == MiniApp.Type.Native) {
			EntityFactory.newSdt(SDT_CLIENT)
		} else {
			EntityFactory.newEntity()
		}
		
		return entity.apply {
			setProperty(SDT_CLIENT_ID, map[SDT_CLIENT_ID])
			setProperty(SDT_CLIENT_NAME, map[SDT_CLIENT_NAME])
			setProperty(SDT_CLIENT_LAST_NAME, map[SDT_CLIENT_LAST_NAME])
			setProperty(SDT_CLIENT_EMAIL, map[SDT_CLIENT_EMAIL])
		}
	}

	fun toMap(entity: Entity): HashMap<String, String> {
		entity.definition.name
		return hashMapOf(
			SDT_CLIENT_ID to entity.getProperty(SDT_CLIENT_ID) as String,
			SDT_CLIENT_NAME to entity.getProperty(SDT_CLIENT_NAME) as String,
			SDT_CLIENT_LAST_NAME to entity.getProperty(SDT_CLIENT_LAST_NAME) as String,
			SDT_CLIENT_EMAIL to entity.getProperty(SDT_CLIENT_EMAIL) as String
		)
	}
}
