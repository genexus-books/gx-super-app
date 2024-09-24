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
		guard let gxResultHandler = self.gxResultHandler else {
			result(nil)
			return
		}
        if (call.method == "getInformation" ){
            let args = call.arguments as? [String: Any]
            guard let data = args?["data"] as? String else {
                let errDesc = "Invalid result arguments: \(String(describing: call.arguments))"
                return
            }
            switch data {
                case "session":
                    self.getSessionInformation { info in
                        if let info = info {
                            result(info)
                        } else {
                            result(FlutterError(code: "UNAVAILABLE", message: "Data not available.", details: nil))
                        }
                    }
                default:
                    result(nil)
            }
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
    
    private func getSessionInformation(completion: @escaping (String?) -> Void) {
		SuperAppAPI.callMethod(name: SampleExObjHandler.Constants.Methods.GET_SESSION_INFO, arguments: nil) { result in
			guard let returnValue = result as? String else {
				let error: NSError
				if let flutterError = result as? FlutterError {
					error = NSError.defaultGXError(withDeveloperDescription: flutterError.description)
				}
				else {
					error = NSError.defaultGXError()
				}
				return
			}
			completion(returnValue)
		}
	}

    enum StaticVars {
        static let CHANNEL = "com.genexus.superapp/paymentConfirm"
    }
  }
