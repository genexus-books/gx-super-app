//
//  SampleExObjHandler.swift
//  ExampleSuperApp
//

import GXCoreBL
import GXSuperApp

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
		
		var alertTitle = "Paying with UI"
		if GXMiniAppsManager.isSandboxEnvironment {
			/// GXMiniAppsManager.isSandboxEnvironment could be used to distinguish between test and production environments within the same API implementation. Note mini apps can't distinguish if it's running in a sandox environemnt or not.
			alertTitle = "SANDBOX: " + alertTitle
		}
		
		
		let alertController = UIAlertController(title: alertTitle, message: "Received a payment of $\(amount)", preferredStyle: .alert)

		let doneAlertAction = UIAlertAction(title: "Done", style: .default, handler: { _ in
			alertController.dismiss(animated: true) {
				self.setReturnValue(GXUUID.create().toString())
				self.onFinishedExecutingWithSuccess()
			}
		})

		alertController.addAction(doneAlertAction)
		
		presentingController.present(alertController, animated: true, completion: nil)
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
		
		var message = "Received a payment of $\(amount)"
		if GXMiniAppsManager.isSandboxEnvironment {
			/// GXMiniAppsManager.isSandboxEnvironment could be used to distinguish between test and production environments within the same API implementation. Note mini apps can't distinguish if it's running in a sandox environemnt or not.
			message = "SANDBOX: " + message
		}
		
		print("\(message)")

		setReturnValue(GXUUID.create().toString())
		onFinishedExecutingWithSuccess()
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
		
		/**
			Client {
				name: String
				lastName: String
				email: String
				clientId: String
			}
		 */
		let dummyClientData = [
			"name": "Test Client First Name",
			"lastName": "Test Client Last Name",
			"email": "test@example.com",
			"clientId": "testClientID"
		] as [String: AnyObject]
		let clientSDTTypeName: String = "Client" // as defined in GeneXus API for mini-apps
		let clientSDTReturnValue = GXSDTData.fromStructureDataTypeName(clientSDTTypeName,
																	   modelObject: actionDesc,
																	   value: dummyClientData,
																	   fieldIsCollection: false)
		setReturnValue(clientSDTReturnValue)
		onFinishedExecutingWithSuccess()
	}
	
	/// Method that receives a Client SDT and returns a PaymentInformation SDT (Collection)
	@objc public func gxActionExObjMethodHandler_GetPaymentInformation(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		
		/**
			Client {
				name: String
				lastName: String
				email: String
				clientId: String
			}
		 */
		guard let clientSDT = sdtParameter(actionDescParametersArray![0], fromValue: parameters[0]),
			let clientId = clientSDT.valueForFieldSpecifier("clientId") as? String else {
			let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "Invalid Client parameter.")
			onFinishedExecutingWithError(error)
			return
		}
		
		/**
			PaymentInformation {
				[
					brand: String
					affinity: String
					type: String
				]
			}
		 */
		let dummyPaymentInfoData = [1, 2, 3].map { i in
			[
				"brand": "Brand \(i) for \(clientId)",
				"affinity": "Affinity \(i) for \(clientId)",
				"type": "Type \(i) for \(clientId)"
			]
		}
		let paymentInformationSDTTypeName: String = "PaymentInformation" // as defined in GeneXus API for mini-apps
		let paymentInformationSDTReturnValue = GXSDTDataCollection.fromStructureDataTypeName(paymentInformationSDTTypeName,
																							 modelObject: actionDesc,
																							 value: dummyPaymentInfoData,
																							 fieldIsCollection: false)
		setReturnValue(paymentInformationSDTReturnValue)
		onFinishedExecutingWithSuccess()
	}
	
	/// Method that receives a PaymentInformation SDT (Collection) and returns a String
	@objc public func gxActionExObjMethodHandler_GetPaymentInfoAffinity(_ parameters: [Any]) {
		if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
			onFinishedExecutingWithError(error)
			return
		}
		
		/**
			PaymentInformation {
				[
					brand: String
					affinity: String
					type: String
				]
			}
		 */
		guard let paymentInfoSDT = sdtCollectionParameter(actionDescParametersArray![0], fromValue: parameters[0]),
			  paymentInfoSDT.sdtDataCollectionItemsCount > 0 else {
			let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "Invalid Payment Information parameter.")
			onFinishedExecutingWithError(error)
			return
		}
		
		let itemsCount = paymentInfoSDT.sdtDataCollectionItemsCount
		var affinityReturnValue = ""
		for itemIndex in 0..<itemsCount {
			guard let _ = paymentInfoSDT.valueForItemAtIndex(itemIndex, fieldSpecifier: "brand") as? String,
				  let affinity = paymentInfoSDT.valueForItemAtIndex(itemIndex, fieldSpecifier: "affinity") as? String,
				  let _ = paymentInfoSDT.valueForItemAtIndex(itemIndex, fieldSpecifier: "type") as? String else {
				let error = invalidParameterErrorValid(forMethod: nil, at: 0, developerDescription: "Invalid Payment Information parameter.")
				onFinishedExecutingWithError(error)
				return
			}
			affinityReturnValue = affinity /// Just pick the last for dummy implementation
		}
		setReturnValue(affinityReturnValue)
		onFinishedExecutingWithSuccess()
	}
	
	// MARK: Private
	
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
