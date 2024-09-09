import UIKit
import Flutter
import GXFoundation
import GXUIApplication
import GXObjectsModel
import GXCoreBL

@main
@objc class AppDelegate: FlutterAppDelegate {
	
	let engines = FlutterEngineGroup(name: "multiple-flutters", project: nil)
	private var provisioningAPI: ProvisioningAPI?
	
	override func application(_ application: UIApplication,
							  didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
		/// Begin GX initialization as soon as possible in willFinishLaunching
		GXUIApplicationExecutionEnvironment.beginCoreInitialization()
#if DEBUG
		/// Optionally register a developer info extension to receive extra information while debugging. This should not be set in a production environment.
		GXUtilities.register(DebugDeveloperInfo())
#endif
		let controller = window?.rootViewController as! FlutterViewController
		provisioningAPI = ProvisioningAPI(binaryMessenger: controller.binaryMessenger)
		/// End GX initialization after host UI has been initialized
		let extensionLibraries: [GXExtensionLibraryProtocol] = [
			SampleExObjLibrary(binaryMessenger: controller.binaryMessenger)
		]
		GXUIApplicationExecutionEnvironment.endCoreInitialization(extensionLibraries: extensionLibraries)
		return super.application(application, didFinishLaunchingWithOptions: launchOptions)
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
