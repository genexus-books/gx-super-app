//
//  GXFirebaseCrashAnalyticsService.swift
//

import GXCoreBL
import FirebaseCore
import FirebaseCrashlytics

class GXFirebaseCrashAnalyticsService: NSObject {
	
	static let shared: GXFirebaseCrashAnalyticsService = .init()
	
	private override init() {
		super.init()
	}
}

extension GXFirebaseCrashAnalyticsService: GXCoreBL.GXCrashAnalyticsService {
	private var crashlytics: FirebaseCrashlytics.Crashlytics {
		FirebaseCrashlytics.Crashlytics.crashlytics()
	}
	
	var didCrashOnPreviousExecution: Bool {
		crashlytics.didCrashDuringPreviousExecution()
	}
	
	func setCustomValue(_ value: String, forKey key: String) {
		crashlytics.setCustomValue(value, forKey: key)
	}
	
	func setCustomKeysAndValues(_ valuesByKey: [String: String]) {
		crashlytics.setCustomKeysAndValues(valuesByKey)
	}
	
	func removeCustomValue(forKey key: String) {
		crashlytics.setCustomValue(nil, forKey: key)
	}
	
	func removeCustomValues(forKeys keys: [String]) {
		var valuesByKeyToRemove: [String: Any] = .init(minimumCapacity: keys.count)
		keys.forEach { valuesByKeyToRemove[$0] = NSNull() }
		crashlytics.setCustomKeysAndValues(valuesByKeyToRemove)
	}
	
	func setUserId(_ userId: String) {
		crashlytics.setUserID(userId)
	}
	
	func removeUserId() {
		crashlytics.setUserID(nil)
	}
	
	func log(message: String) {
		crashlytics.log(message)
	}
	
	func record(error: Error) {
		crashlytics.record(error: error)
	}
}
