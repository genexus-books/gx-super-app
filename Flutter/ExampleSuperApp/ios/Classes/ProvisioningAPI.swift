import Flutter
import UIKit
import GXCoreBL
import GXSuperApp

class ProvisioningAPI {
	public func handleMethodCall(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
		switch call.method {
		case Constants.Methods.GET_MINI_APPS:
			let tag = (call.arguments as? [String: Any])?[Constants.Arguments.TAG] as? String ?? ""
			getMiniApps(tag: tag, result: result)
		case Constants.Methods.GET_MINI_APPS_CACHED:
			getCachedMiniApps(result: result)
		case Constants.Methods.REMOVE:
			guard let miniAppJson = (call.arguments as? [String: Any])?[Constants.Arguments.MINI_APP_JSON] as? String,
				  !miniAppJson.isEmpty, let miniAppJsonData = miniAppJson.data(using: .utf8),
				  let miniAppInfo = try? JSONSerialization.jsonObject(with: miniAppJsonData) as? [String: Any] else {
				let error = FlutterError(code: Constants.Errors.ARGUMENTS, message: "MiniApp json cannot be null or empty", details: nil)
				result(error)
				return
			}
			guard let id = miniAppInfo[Constants.MiniAppFields.ID] as? String else {
				let error = FlutterError(code: Constants.Errors.ARGUMENTS, message: "Cannot delete MiniApp as it contains incorrect information", details: nil)
				result(error)
				return
			}
			let version = miniAppInfo[Constants.MiniAppFields.VERSION] as? Int
			remove(id: id, version: version, result: result)
		case Constants.Methods.LOAD:
			guard let miniAppJson = (call.arguments as? [String: Any])?[Constants.Arguments.MINI_APP_JSON] as? String,
				  !miniAppJson.isEmpty, let miniAppJsonData = miniAppJson.data(using: .utf8),
				  let miniAppInfo = try? JSONDecoder().decode(GXMiniAppInformation.self, from: miniAppJsonData) else {
				let error = FlutterError(code: Constants.Errors.ARGUMENTS, message: "MiniApp json cannot be null or empty", details: nil)
				result(error)
				return
			}
			load(miniApp: miniAppInfo, result: result)
			break
		default:
			break
		}
	}
	
	private func getMiniApps(tag: String, result: @escaping FlutterResult) {
		GXSuperAppProvisioning.miniAppsInfoByTag(tag, start: 0, count: 10) {
			switch $0 {
			case .failure(let error):
				let flutterError = FlutterError(code: Constants.Errors.GET,
												message: "Couldn't retrieve MiniApps from server, cause '\(error)'",
												details: nil)
				result(flutterError)
			case .success(let miniAppsInfo):
				guard let miniAppsInfoJSONData = try? JSONEncoder().encode(miniAppsInfo),
					  let miniAppsInfoJSONString = String(data: miniAppsInfoJSONData, encoding: .utf8) else {
					let flutterError = FlutterError(code: Constants.Errors.GET,
													message: "Couldn't encode MiniApps to json",
													details: nil)
					result(flutterError)
					return
				}
				result(miniAppsInfoJSONString)
			}
		}
	}
	
	private func getCachedMiniApps(result: @escaping FlutterResult) {
		DispatchQueue.gxTryOnBackground(execute: {
			return try GXMiniAppsManager.cachedMiniApps()
		}, callback: {
			switch $0 {
			case .failure(let error):
				let flutterError = FlutterError(code: Constants.Errors.GET,
												message: "Couldn't retrieve cached MiniApps from server, cause '\(error)'",
												details: nil)
				result(flutterError)
			case .success(let cachedMiniApps):
				let encoder = JSONEncoder()
				encoder.dateEncodingStrategy = .iso8601
				encoder.keyEncodingStrategy = .custom({ codingPath in
					guard codingPath.count > 0 else {
						fatalError("Invalid coding key")
					}
					let key = codingPath.last!
					enum MappedCodingKeys: String, CodingKey {
						case miniAppId = "Id"
						case miniAppVersion = "Version"
					}
					switch key.stringValue {
					case "MiniAppId":
						return MappedCodingKeys.miniAppId
					case "MiniAppVersion":
						return MappedCodingKeys.miniAppVersion
					default:
						return key
					}
				})
				guard let cachedMiniAppsJSONData = try? encoder.encode(cachedMiniApps),
					  let cachedMiniAppsJSONString = String(data: cachedMiniAppsJSONData, encoding: .utf8) else {
					let flutterError = FlutterError(code: Constants.Errors.GET,
														 message: "Couldn't encode cached MiniApps to json",
														 details: nil)
					result(flutterError)
					return
				}
				result(cachedMiniAppsJSONString)
			}
		})
	}
	
	private func load(miniApp: GXMiniAppInformation, result: @escaping FlutterResult) {
		GXMiniAppsManager.loadMiniApp(info: miniApp) { error in
			if let error = error {
				let flutterError = FlutterError(code: Constants.Errors.LOAD,
												message: "Couldn't load MiniApp '\(miniApp.id)', cause '\((error as NSError).localizedDescription)'",
												details: nil)
				result(flutterError)
			}
			else {
				result(true)
			}
		}
	}
	
	private func remove(id: String, version: Int?, result: @escaping FlutterResult) {
		DispatchQueue.gxTryOnBackground(execute: {
			return try GXMiniAppsManager.removeCachedMiniApp(id: id, version: version)
		}, callback: {
			switch $0 {
			case .failure(let error):
				let flutterError = FlutterError(code: Constants.Errors.GET,
												message: "Couldn't retrieve cached MiniApps from server, cause '\(error)'",
												details: nil)
				result(flutterError)
			case .success(let deleted):
				result(deleted)
			}
		})
	}
	
	private struct Constants {
		static let CHANNEL_NAME = "example_superapp"
		struct Methods {
			static let GET_MINI_APPS = "getMiniApps"
			static let GET_MINI_APPS_CACHED = "getCachedMiniApps"
			static let LOAD = "load"
			static let REMOVE = "remove"
		}
		struct Arguments {
			static let TAG = "tag"
			static let MINI_APP_JSON = "miniAppJson"
		}
		struct Errors {
			static let ARGUMENTS = "1"
			static let GET = "2"
			static let LOAD = "3"
		}
		struct MiniAppFields {
			static let ID = "Id"
			static let VERSION = "Version"
		}
	}
}
