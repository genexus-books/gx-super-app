//
//  SampleExObjLibrary.swift
//  ExampleSuperApp
//

import GXCoreBL
import GXSuperApp

class SampleExObjLibrary: NSObject, GXExtensionLibraryProtocol {
	func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXMiniAppsManager.registerSuperAppAPI(SampleExObjHandler.self, forExternalObjectName: "Payments")
	}
}
