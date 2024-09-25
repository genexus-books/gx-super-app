import Flutter
import Foundation

class FlutterPaymentViewController: FlutterViewController {
	
	private var data: String
	private var gxResultHandler: ((Result<String, Error>) -> Void)?
	
	private lazy var flutterChannel = FlutterMethodChannel(name: StaticVars.CHANNEL, binaryMessenger: self.engine!.binaryMessenger)
	
	init(data: String, gxResultHanlder: @escaping (Result<String, Error>) -> Void) {
		self.data = data
		self.gxResultHandler = gxResultHanlder
		guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else {
			preconditionFailure("unable to obtain AppDelegate")
		}
		let newEngine = appDelegate.engines.makeEngine(withEntrypoint: "showPayWithUI", libraryURI: nil)
		super.init(engine: newEngine, nibName: nil, bundle: nil)
	}
	
	required init(coder aDecoder: NSCoder) {
		fatalError("init(coder:) has not been implemented")
	}
	
	deinit {
		flutterChannel.setMethodCallHandler(nil)
		if let gxResultHandler {
			gxResultHandler(.failure(NSError.userCancelledError()))
		}
	}
	
	override func viewDidLoad() {
		super.viewDidLoad()
		flutterChannel.invokeMethod("init", arguments: ["data" : data])
		flutterChannel.setMethodCallHandler { [weak self] call, result in
			self?.handleMethodCall(call, result: result)
		}
	}
	
	private func handleMethodCall(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
		guard call.method != "getInformation" else {
			let args = call.arguments as? [String: Any]
			guard let data = args?["data"] as? String else {
				let errDesc = "Invalid \(call.method) arguments: \(String(describing: call.arguments))"
				result(FlutterError(code: "INVALID_ARGUMENTS", message: errDesc, details: nil))
				return
			}
			switch data {
			case "session":
				SuperAppAPI.callMethod(name: SampleExObjHandler.Constants.Methods.GET_SESSION_INFO, arguments: nil, resultHandler: result)
			default:
				let errDesc = "Invalid \(call.method) argument: \(data)"
				result(FlutterError(code: "INVALID_ARGUMENTS", message: errDesc, details: nil))
			}
			return
		}
		guard let gxResultHandler = self.gxResultHandler else {
			result(nil)
			return
		}
		self.gxResultHandler = nil
		dismiss(animated: true) {
			defer { result(nil) }
			switch call.method {
			case "confirmFlutterActivity":
				let resultArgs = call.arguments as? [String: Any]
				guard let result = resultArgs?["data"] as? String else {
					let errDesc = "Invalid result arguments: \(String(describing: call.arguments))"
					gxResultHandler(.failure(NSError.defaultGXError(withDeveloperDescription: errDesc)))
					return
				}
				gxResultHandler(.success(result))
			case "closeFlutterActivity":
				gxResultHandler(.failure(NSError.userCancelledError()))
			default:
				gxResultHandler(.failure(NSError.defaultGXError(withDeveloperDescription: "Unknown method: \(call.method)")))
			}
		}
	}
	
	enum StaticVars {
		static let CHANNEL = "com.genexus.superapp/paymentConfirm"
	}
}
