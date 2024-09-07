import Flutter
import UIKit

public class SuperAppAPI: NSObject {
	
	private static var flutterChannel: FlutterMethodChannel?
	
	private struct Constants {
		static let CHANNEL_NAME = "com.genexus.superapp/SuperAppAPI"
	}

	public static func setup(binaryMessenger: FlutterBinaryMessenger) {
		flutterChannel = FlutterMethodChannel(name: Constants.CHANNEL_NAME, binaryMessenger: binaryMessenger)
	}
	
	public static func callMethod(name: String, arguments: Any?, resultHandler: FlutterResult?) {
		guard let channel = flutterChannel else {
			preconditionFailure("setup(binaryMessenger:) call missing?")
		}
		channel.invokeMethod(name, arguments: arguments, result: resultHandler)
	}
	
	public static func callFlutterUIMethod(name: String, arguments: String, uiContext: UIViewController,
										   gxResultHandler: @escaping (Result<Any?, Error>) -> Void) {
		let flutterViewController: FlutterViewController
		switch name {
		case SampleExObjHandler.Constants.Methods.PAY_UI:
			flutterViewController = FlutterPaymentViewController(data: arguments) { paymentResult in
				gxResultHandler(paymentResult.map { $0 })
			}
		default:
			preconditionFailure("unable to obtain flutterActivityController")
		}
		let navigationController = UINavigationController(rootViewController: flutterViewController)
		uiContext.present(navigationController, animated: true, completion: nil)
	}
}
