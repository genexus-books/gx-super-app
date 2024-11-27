//
//  SampleExObjHandler.swift
//  ExampleSuperApp
//

import Flutter
import GXCoreBL

@objc public class SampleExObjHandler: GXActionExternalObjectHandler {
	
	public override static func handleActionExecutionUsingMethodHandlerSelectorNamePrefix() -> Bool {
		return true
	}
	
	// MARK: Action Handlers
	
	@objc public func gxActionExObjMethodHandler_PayWithUI(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		let amount: Double
		let presentingController: UIViewController
		do {
			try amount = paymentAmout(from: parameters[0])
			try presentingController = presentingViewController()
		}
		catch {
			onFinishedExecutingWithError(error)
			return
		}
		let args = String(format: "%f", amount)
		SuperAppAPI.callFlutterUIMethod(name: Constants.Methods.PAY_UI, arguments: args, uiContext: presentingController) { [weak self] result in
			guard let sself = self else { return }
			do {
				let returnValue = try result.get()
				sself.setReturnValue(returnValue)
				sself.onFinishedExecutingWithSuccess()
			}
			catch {
				sself.onFinishedExecutingWithError(error)
			}
		}
	}
	
	@objc public func gxActionExObjMethodHandler_PayWithoutUI(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		let amount: Double
		do {
			try amount = paymentAmout(from: parameters[0])
		}
		catch {
			onFinishedExecutingWithError(error)
			return
		}
		let args = ["amount": amount]
		SuperAppAPI.callMethod(name: Constants.Methods.PAY_NO_UI, arguments: args) { [weak self] result in
			guard let sself = self else { return }
			guard let returnValue = result as? String else {
				let error: NSError
				if let flutterError = result as? FlutterError {
					error = NSError.defaultGXError(withDeveloperDescription: flutterError.description)
				}
				else {
					error = NSError.defaultGXError()
				}
				sself.onFinishedExecutingWithError(error)
				return
			}
			sself.setReturnValue(returnValue)
			sself.onFinishedExecutingWithSuccess()
		}
	}
	
	/// Method that receives a String and returns a Client SDT
	@objc public func gxActionExObjMethodHandler_GetClientInformation(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		guard let clientID = stringParameter(actionDescParametersArray![0], fromValue: parameters[0]), !clientID.isEmpty else {
			let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "The Client ID must be not empty.")
			onFinishedExecutingWithError(error)
			return
		}
		let args = ["clientId": clientID]
		SuperAppAPI.callMethod(name: Constants.Methods.GET_CLIENT_INFO, arguments: args) { [weak self] result in
			guard let sself = self else { return }
			guard let returnValue = result as? [String: AnyObject] else {
				let error: NSError
				if let flutterError = result as? FlutterError {
					error = NSError.defaultGXError(withDeveloperDescription: flutterError.description)
				}
				else {
					error = NSError.defaultGXError()
				}
				sself.onFinishedExecutingWithError(error)
				return
			}
			let clientSDTTypeName: String = "Client" // as defined in GeneXus API for mini-apps
			let clientSDTReturnValue = GXSDTData.fromStructureDataTypeName(clientSDTTypeName,
																		   modelObject: sself.actionDesc,
																		   value: returnValue,
																		   fieldIsCollection: false)
			sself.setReturnValue(clientSDTReturnValue)
			sself.onFinishedExecutingWithSuccess()
		}
	}
	
	/// Method that receives a Client SDT and returns a PaymentInformation SDT (Collection)
	@objc public func gxActionExObjMethodHandler_GetPaymentInformation(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		guard let clientSDT = sdtParameter(actionDescParametersArray![0], fromValue: parameters[0]),
			  let clientJSONObj = clientSDT.json else {
			let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "Invalid Client parameter.")
			onFinishedExecutingWithError(error)
			return
		}
		let args = ["clientInfo": clientJSONObj]
		SuperAppAPI.callMethod(name: Constants.Methods.PAYMENT_INFORMATION, arguments: args) { [weak self] result in
			guard let sself = self else { return }
			guard let returnValue = result as? [String: AnyObject] else {
				let error: NSError
				if let flutterError = result as? FlutterError {
					error = NSError.defaultGXError(withDeveloperDescription: flutterError.description)
				}
				else {
					error = NSError.defaultGXError()
				}
				sself.onFinishedExecutingWithError(error)
				return
			}
			let paymentInformationSDTTypeName: String = "PaymentInformation" // as defined in GeneXus API for mini-apps
			let paymentInformationSDTReturnValue = GXSDTDataCollection.fromStructureDataTypeName(paymentInformationSDTTypeName,
																								 modelObject: sself.actionDesc,
																								 value: returnValue,
																								 fieldIsCollection: false)
			sself.setReturnValue(paymentInformationSDTReturnValue)
			sself.onFinishedExecutingWithSuccess()
		}
	}
	
	/// Method that receives a PaymentInformation SDT (Collection) and returns a String
	@objc public func gxActionExObjMethodHandler_GetPaymentInfoAffinity(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		guard let paymentInfoSDT = sdtCollectionParameter(actionDescParametersArray![0], fromValue: parameters[0]),
			  paymentInfoSDT.sdtDataCollectionItemsCount > 0,
			  let paymentInfoJSONObj = paymentInfoSDT.json else {
			let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "Invalid Payment Information parameter.")
			onFinishedExecutingWithError(error)
			return
		}
		let args = ["paymentInfo": paymentInfoJSONObj]
		SuperAppAPI.callMethod(name: Constants.Methods.GET_PAYMENT_AFFINITY, arguments: args) { [weak self] result in
			guard let sself = self else { return }
			guard let returnValue = result as? String else {
				let error: NSError
				if let flutterError = result as? FlutterError {
					error = NSError.defaultGXError(withDeveloperDescription: flutterError.description)
				}
				else {
					error = NSError.defaultGXError()
				}
				sself.onFinishedExecutingWithError(error)
				return
			}
			sself.setReturnValue(returnValue)
			sself.onFinishedExecutingWithSuccess()
		}
	}
	
	// MARK: Private
	
	struct Constants {
		struct Methods {
			static let PAY_NO_UI = "PayWithoutUI"
			static let PAY_UI = "PayWithUI"
			static let GET_SESSION_INFO = "GetSessionInformation"
			static let GET_CLIENT_INFO = "GetClientInformation"
			static let PAYMENT_INFORMATION = "GetPaymentInformation"
			static let GET_PAYMENT_AFFINITY = "GetPaymentInfoAffinity"
		}
	}

	private func paymentAmout(from parameter: Any) throws -> Double {
		guard let amount = (parameter as? NSNumber)?.doubleValue, amount > 0 else {
			throw NSError.defaultGXError(withLocalizedDescription: "The amount must be greater than 0.")
		}
		return amount
	}

	private func presentingViewController() throws -> UIViewController {
		guard let presentingController = gxActionHandlerUserInterfaceController?.actionHandlerUserInterfaceController,
			  presentingController.presentedViewController == nil else {
			throw NSError.fatalGXError(withDeveloperDescription: "No valid prensenting view controller found for payment UI.")
		}
		return presentingController
	}
}
