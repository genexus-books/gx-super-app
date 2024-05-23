# Introduction

This document explains how to develop and integrate the functionality provided by the Super App and then integrated into the Mini Apps by using an External Object.

# Definition of the External Object

The definition of the External Object used in this document is the same as [the one described for Android](../../Android/MiniAppCaller/README.md#interfaz-en-kb-genexus-external-object ).

Some things to take into consideration about this definition:

- The name of the External Object is "Payments". It's important to use the same name in the implementation. 
- The External Object provides two methods: PayWithUI and PayWithoutUI, which receive an integer (amount) and return a _string_ (payment identifier)

# Implementation for iOS

For the implementation in iOS, you need to follow this steps: 
1. Implement the External Object that complies with the established signature. 
2. Implement an _extension library_ that registers the External Object, and that must be loaded by the Super App. 
3. Modify the _app delegate_ to load the _extension library_.

## External Object implementation

The External Object implementation is found in the [SampleExObjHandler.swift](SampleExObjHandler.swift) file. It begins by importing `GXCoreBL`, which contains the base class to implement the External Object. 

```
import GXCoreBL
```

Then the class `SampleExObjHandler` is declared, which inherits from `GXActionExternalObjectHandler`:

```
@objc public class SampleExObjHandler: GXActionExternalObjectHandler {
```

It's important to `override` the `handleActionExecutionUsingMethodHandlerSelectorNamePrefix` function returning `true` to use the naming convention, so that the Flexible Client can find the External Object's _method handlers_ correctly:

```
public override static func handleActionExecutionUsingMethodHandlerSelectorNamePrefix() -> Bool {
    return true
}
```

The _method handlers_ implementation must be prefixed with "gxActionExObjMethodHandler_" and then continue with the name of the method as defined in the External Object. In case the method has parameters, they are received in an _array_ of type`Any` that must be processed to obtain the parameters as expected. 

Therefore, the declaration of the methods is as follows: 

```
@objc public func gxActionExObjMethodHandler_PayWithUI(_ parameters: [Any]) {...}
@objc public func gxActionExObjMethodHandler_PayWithoutUI(_ parameters: [Any]) {...}
```
Both methods are similar in terms of the parameter they receive and the result they return. Note that Both methods start by checking the number of parameters.

```
if let error = validateNumber(ofParametersReceived: UInt(parameters.count), expected: 1) {
	onFinishedExecutingWithError(error)
	return
}
```

Also note that if there is an error, the `onFinishedExecutingWithError(_:)` method is called. 

**IMPORTANT**: It is mandatory to call `onFinishedExecutingWithError(_:)` to finish the method's execution, or its equivalent `onFinishedExecutingWithSuccess()` if it was successful. This allows the execution of the commands that follow in the Mini App programming. If these methods are not called, the execution will never end.

After that, both methods try to get the payment amount by calling the `paymentAmout(from:)` auxiliary function. It turns the parameter of type `Any` into an `Int`, which is what the method expects in the definition of the External Object. 

If this value cannot be obtained from the parameter, then the execution ends with an error:

```
let amount: Int
do {
	try amount = paymentAmout(from: parameters[0])
}
catch {
	onFinishedExecutingWithError(error)
	return
}
```

The payment with UI function also tries to get the `presentingController`, in order to display its corresponding UI. This is done in the same `do-catch` as the reading the import. If it can´t get it, then it also fails:

```
try presentingController = presentingViewController()
```

The `presentingViewController()` function is in charge of getting the required View Controller and must be implemented as follows: 

```
private func presentingViewController() throws -> UIViewController {
    guard let presentingController = gxActionHandlerUserInterfaceController?.actionHandlerUserInterfaceController,
            presentingController.presentedViewController == nil else {
        throw NSError.fatalGXError(withDeveloperDescription: "No valid prensenting view controller found for payment UI.")
    }
    return presentingController
}
```
The method without UI is the simplest. Once the amount is obtained, it is just printed in the console and a return value is assigned by calling the  `setReturnValue(_:)` function. In this case and as an example, the returned value is a `GUID` turned into a `String`.

```
print("Received a payment of $\(amount)")
setReturnValue(GXUUID.create().toString())
onFinishedExecutingWithSuccess()
```
As previously stated, the `onFinishedExecutingWithSuccess()` must be called before finishing the execution.

In the case of the payment with UI, the sample creates a `UIAlertController`, presents it using the `presentingController` and waits for the button "Done" to be pressed to return the value and finish the execution. 

```
let doneAlertAction = UIAlertAction(title: "Done", style: .default, handler: { _ in
	alertController.dismiss(animated: true) {
		self.setReturnValue(GXUUID.create().toString())
		self.onFinishedExecutingWithSuccess()
	}
})
```

The payment with UI sample shows how the asynchronous case should be handled. Note that even though the execution of the _method handler_ implementation ends after calling the `presentingController.present(alertController, animated: true, completion: nil)`, the method doesn't finish for the Mini App until the `onFinishedExecutingWithSuccess()` is called. 

## Implementation of the _extension library_

The class that implements the _extension library_ is found in the file [SampleExObjLibrary.swift](SampleExObjLibrary.swift), which contains the following:

```
import GXCoreBL

class SampleExObjLibrary: NSObject, GXExtensionLibraryProtocol {
	func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXMiniAppsManager.registerSuperAppAPI(SampleExObjHandler.self, forExternalObjectName: "Payments")
	}
}
```

Note that as for the External Obect, an `import GXCoreBL` is done, and also note that the class must implement the `GXExtensionLibraryProtocol` protocol. 

The rest of the code is the implementation of the `initializeExtensionLibrary(withContext:)` function. In this case, it just registers the External Object.

Note that the External Object is registered with the same name with which it was defined, "Payments" in this example.

## Loading the _extension library_

Finally, it's necessary that the _app delegate_ loads the _extension library_ so that it registers the External Object. This is done with the `endCoreInitialization(extensionLibraries:)` method of the `GXUIApplicationExecutionEnvironment` class.   

Note that it's possible that the variant of the method without parameters is called in the example:

```
GXUIApplicationExecutionEnvironment.endCoreInitialization()
```

If that's the case, it must be replaced with: 

```
let extLibraries: [GXExtensionLibraryProtocol] = [SampleExObjLibrary()]
GXUIApplicationExecutionEnvironment.endCoreInitialization(extensionLibraries: extLibraries)
```


### Obtaining the caller's Mini App identifier

In certain scenarios, obtaining the current Mini App identifier is useful for discerning the caller of the Super App API method and displaying relevant information about the invoking Mini App.
This code retrieves the current Mini App identifier:

```
private func miniAppId(from gxModel: GXModel) throws -> String {
    guard let miniAppId = gxModel.appModel.appMiniAppId else {
        throw NSError.defaultGXError(withLocalizedDescription: "Invalid context without MiniApp Id.")
    }
    return miniAppId
}
```

To invoke this method, use the following code:

```
guard let gxModel = self.executingGXModel else { return }
let miniAppId: String
do {
    try miniAppId = self.miniAppId(from: gxModel)
}
```

# Conclusion

By following the aforementioned steps, the External Object implementation is available for the Mini Apps.
