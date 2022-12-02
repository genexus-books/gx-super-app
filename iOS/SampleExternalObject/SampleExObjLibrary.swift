//
//  SampleExObjLibrary.swift
//  ExampleSuperApp
//

import GXCoreBL

class SampleExObjLibrary: NSObject, GXExtensionLibraryProtocol {
	func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXActionExternalObjectHandler.register(SampleExObjHandler.self,
											   forExternalObjectName: "Payments")
	}
}
