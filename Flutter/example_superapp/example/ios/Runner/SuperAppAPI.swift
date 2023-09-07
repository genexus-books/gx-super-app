import Flutter
import UIKit

public class SuperAppAPI: NSObject {
	
	private static var flutterChannel: FlutterMethodChannel!
	
	private struct Constants {
		static let CHANNEL_NAME = "example_superapp"
	}
	
	public static func setup(binaryMessenger: FlutterBinaryMessenger) {
		flutterChannel = FlutterMethodChannel(name: Constants.CHANNEL_NAME, binaryMessenger: binaryMessenger)
		let provisioning = ProvisioningAPI()
		flutterChannel.setMethodCallHandler { call, result in
			provisioning.handleMethodCall(call, result: result)
		}
	}
	
	public static func callMethod(name: String, arguments: Any?, resultHandler: FlutterResult?) {
		flutterChannel.invokeMethod(name, arguments: arguments, result: resultHandler)
	}
	
	public static func callFlutterUIMethod(name: String, arguments: Any?, uiContext: UIViewController, resultHandler: @escaping FlutterResult) {
		let app = UIApplication.shared
		let appDelegate = app.delegate as! AppDelegate
		lazy var invalidUIContextError = FlutterError(code: "INVALID_UI_CONTEXT", message: nil, details: nil)
		guard let rootFlutterWindow = appDelegate.flutterRootController?.viewIfLoaded?.window, !rootFlutterWindow.isKeyWindow else {
			resultHandler(invalidUIContextError)
			return
		}
		guard let returnToWindow = uiContext.viewIfLoaded?.window else {
			resultHandler(invalidUIContextError)
			return
		}
		/// Sample implementation just hides the window until completed. A better implementation could present a new FlutterViewController from uiContext instead.
		rootFlutterWindow.makeKeyAndVisible()
		flutterChannel.invokeMethod(name, arguments: arguments) { [weak returnToWindow] result in
			if rootFlutterWindow.isKeyWindow, let returnToWindow = returnToWindow {
				returnToWindow.makeKeyAndVisible()
				rootFlutterWindow.isHidden = true
			}
			resultHandler(result)
		}
	}
}
