//
//  SuperAppAPI.swift
//  ExampleSuperApp
//

import Foundation
import React

@objc(SuperAppAPI)
class SuperAppAPI: NSObject, RCTBridgeModule {

  static func moduleName() -> String! {
    return "SuperAppAPI"
  }

  static func requiresMainQueueSetup() -> Bool {
    return true
  }

  @objc
  func constantsToExport() -> [AnyHashable: Any] {
    return [:] // Add any constants you want to export
  }
}
