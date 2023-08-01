# Enable Mini App Logging for iOS

The Super App may store Mini App logging messages. 
To do this, enable Mini-Apps login in the Super App, as shown in the [App example](ExampleSuperApp/AppDelegate.swift#L19) 

```swift
GXMiniAppsManager.logEnabled = true
/// Adjust log level
GXMiniAppsManager.setLogLevel(.debug, for: .general)
```

Note this settings are persistent across app lunches, so you have to manually disable it to stop logging.
Log files (.log) are created under 'GXData' directory in the NSDocumentDirectory user directory (excluded from backup).

If log is enabled / disabled after GXUIApplicationExecutionEnvironment initialization, log should be started / ended manually by calling:
```swift
GXFoundationServices.loggerService()?.startLogging()
```
or
```swift
GXFoundationServices.loggerService()?.endLogging()
```
