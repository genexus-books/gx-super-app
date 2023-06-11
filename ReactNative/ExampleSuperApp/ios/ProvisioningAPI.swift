import UIKit
import GXCoreBL
import GXSuperApp

@objc(ProvisioningAPI)
class ProvisioningAPI: NSObject, RCTBridgeModule {

  static func moduleName() -> String {
    return "ProvisioningAPI"
  }
  
  @objc
  public func handleMethodCall(_ call: NSDictionary, result: @escaping RCTResponseSenderBlock) {
    guard let method = call["method"] as? String else {
      result([["error": "Invalid method"]])
      return
    }

    switch method {
    case Constants.Methods.GET_MINI_APPS:
      guard let arguments = call["arguments"] as? [String: Any],
            let tag = arguments[Constants.Arguments.TAG] as? String else {
        result([["error": "Invalid arguments"]])
        return
      }
      getMiniApps(tag: tag, result: result)

    case Constants.Methods.GET_MINI_APPS_CACHED:
      getCachedMiniApps(result: result)

    case Constants.Methods.REMOVE:
      guard let arguments = call["arguments"] as? [String: Any],
            let miniAppJson = arguments[Constants.Arguments.MINI_APP_JSON] as? String,
            let miniAppJsonData = miniAppJson.data(using: .utf8),
            let miniAppInfo = try? JSONSerialization.jsonObject(with: miniAppJsonData) as? [String: Any],
            let id = miniAppInfo[Constants.MiniAppFields.ID] as? String else {
        result([["error": "Invalid arguments"]])
        return
      }
      let version = miniAppInfo[Constants.MiniAppFields.VERSION] as? Int
      remove(id: id, version: version, result: result)

    case Constants.Methods.LOAD:
      guard let arguments = call["arguments"] as? [String: Any],
            let miniAppJson = arguments[Constants.Arguments.MINI_APP_JSON] as? String,
            let miniAppJsonData = miniAppJson.data(using: .utf8),
            let miniAppInfo = try? JSONDecoder().decode(GXMiniAppInformation.self, from: miniAppJsonData) else {
        result([["error": "Invalid arguments"]])
        return
      }
      load(miniApp: miniAppInfo, result: result)

    default:
      result([["error": "Invalid method"]])
    }
  }

  
  private func getMiniApps(tag: String, result: @escaping RCTResponseSenderBlock) {
    GXSuperAppProvisioning.miniAppsInfoByTag(tag, start: 0, count: 10) { response in
      switch response {
      case .failure(let error):
        result([["error": "Failed to retrieve MiniApps: \(error)"]])

      case .success(let miniAppsInfo):
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        do {
          let miniAppsInfoJSONData = try encoder.encode(miniAppsInfo)
          let miniAppsInfoJSONString = String(data: miniAppsInfoJSONData, encoding: .utf8)
          result([miniAppsInfoJSONString ?? String()])
        } catch {
          result([["error": "Failed to encode MiniApps to JSON"]])
        }
      }
    }
  }

  private func getCachedMiniApps(result: @escaping RCTResponseSenderBlock) {
    DispatchQueue.gxTryOnBackground {
      return try GXMiniAppsManager.cachedMiniApps()
    } callback: { response in
      switch response {
      case .failure(let error):
        result([["error": "Failed to retrieve cached MiniApps: \(error)"]])

      case .success(let cachedMiniApps):
        let encoder = JSONEncoder()

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
        do {
          let cachedMiniAppsData = try encoder.encode(cachedMiniApps)
          let cachedMiniAppsString = String(data: cachedMiniAppsData, encoding: .utf8)
          result([cachedMiniAppsString ?? String()])
        } catch {
          result([["error": "Failed to encode cached MiniApps to JSON"]])
        }
      }
    }
  }

  private func load(miniApp: GXMiniAppInformation, result: @escaping RCTResponseSenderBlock) {
    GXMiniAppsManager.loadMiniApp(info: miniApp) { error in
      if let error = error {
        result([["error": "Failed to load MiniApp '\(miniApp.id)': \(error.localizedDescription)"]])
      } else {
        result([true])
      }
    }
  }

  private func remove(id: String, version: Int?, result: @escaping RCTResponseSenderBlock) {
    DispatchQueue.gxTryOnBackground {
      return try GXMiniAppsManager.removeCachedMiniApp(id: id, version: version)
    } callback: { response in
      switch response {
      case .failure(let error):
        result([["error": "Failed to remove cached MiniApp: \(error)"]])

      case .success(let deleted):
        result([deleted])
      }
    }
  }

  private struct Constants {
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

    struct MiniAppFields {
      static let ID = "Id"
      static let VERSION = "Version"
    }
  }
}
