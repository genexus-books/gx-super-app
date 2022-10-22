//
//  GXCustomUserControlsMapper.swift
//  ExampleSuperApp
//

import Foundation


/// Custom UI controls definition goes here
@objc(GXCustomUserControlsMapper)
class GXCustomUserControlsMapper: NSObject {
	@objc(userControlClassNameForControlName:)
	func userControlClassName(forControlName name: String) -> String? {
		switch (name) {
		case "Rating": // Map from control name in GX
			return "GXControlStarRating" // to native ObjC class name
		default:
			return nil
		}
	}
}
