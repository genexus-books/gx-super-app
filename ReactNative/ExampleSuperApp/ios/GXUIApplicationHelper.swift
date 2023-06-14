//
//  GXUIApplicationHelper.swift
//  ExampleSuperApp
//

import UIKit
import GXFoundation
import GXUIApplication
import GXObjectsModel
import GXCoreBL

@objc(GXUIApplicationHelper)
class GXUIApplicationHelper: NSObject {
  @objc static let shared = GXUIApplicationHelper()

  @objc func initializeGX() {
    /// Begin GX initialization as soon as possible in willFinishLaunching
    GXUIApplicationExecutionEnvironment.beginCoreInitialization()
    #if DEBUG
    /// Optionally register a developer info extension to receive extra information while debugging. This should not be set in a production environment.
    GXUtilities.register(DebugDeveloperInfo())
    #endif

    /// End GX initialization after host UI has been initialized
    let extensionLibraries: [GXExtensionLibraryProtocol] = [
      SampleExObjLibrary()
    ]
    GXUIApplicationExecutionEnvironment.endCoreInitialization(extensionLibraries: extensionLibraries)
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
