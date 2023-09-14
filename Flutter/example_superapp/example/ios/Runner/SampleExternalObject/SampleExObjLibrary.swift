//
//  SampleExObjLibrary.swift
//  ExampleSuperApp
//

import Flutter
import GXCoreBL

class SampleExObjLibrary: NSObject, GXExtensionLibraryProtocol {
	
	let binaryMessenger: FlutterBinaryMessenger
	
	required init(binaryMessenger: FlutterBinaryMessenger) {
		self.binaryMessenger = binaryMessenger
	}
	
	func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		SuperAppAPI.setup(binaryMessenger: self.binaryMessenger)
		GXActionExternalObjectHandler.register(SampleExObjHandler.self,
											   forExternalObjectName: "Payments")
	}
}
