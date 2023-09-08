//
//  AppDelegate.swift
//  ExampleSuperApp
//

import UIKit
import GXFoundation
import GXUIApplication
import GXObjectsModel
import GXCoreBL
import GXSuperApp

@main
class AppDelegate: NSObject, UIApplicationDelegate {
	
	var window: UIWindow?
	
	func application(_ application: UIApplication, willFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
		/// Optionally turn MiniApps logging on for troubleshooting issues. Log files (.log) are created under GXData directory in the NSDocumentDirectory user directory (excluded from backup).
		/// Note this settings are persistent across app lunches, so you have to manually disable it to stop logging.
		GXMiniAppsManager.logEnabled = true
		/// Adjust log level
		GXMiniAppsManager.setLogLevel(.debug, for: .general)
		/// If log is enabled / disabled after GXUIApplicationExecutionEnvironment initialization, log should be started / ended manually by calling:
		/// GXFoundationServices.loggerService()?.startLogging() / GXFoundationServices.loggerService()?.endLogging()
		
		/// Begin GX initialization as soon as possible in willFinishLaunching
		GXUIApplicationExecutionEnvironment.beginCoreInitialization()
		#if DEBUG
		/// Optionally register a developer info extension to receive extra information while debugging. This should not be set in a production environment.
		GXUtilities.register(DebugDeveloperInfo())
		#endif
		/// Perform host app UI initialization
		window = application.windows.first
		window?.makeKeyAndVisible()
		/// End GX initialization after host UI has been initialized
		let extensionLibraries: [GXExtensionLibraryProtocol] = [
			SampleExObjLibrary()
		]
		GXUIApplicationExecutionEnvironment.endCoreInitialization(extensionLibraries: extensionLibraries)
		return true
	}
}

#if DEBUG
class DebugDeveloperInfo: NSObject {
}

extension DebugDeveloperInfo: GXUtilitiesDeveloperInfoExtension {
	var isDeveloperInfoExtensionDynamic: Bool { false }
	
	var showDeveloperInfo: Bool { true }
}
#endif
