# Crashlytics for iOS

## Setup

It is possible for the Super App to receive Crashlytics reports for crashes that occur during the execution of a Mini App.

In order to achieve this, a couple of steps must be followed:

1. Implement the protocol `GXCrashAnalyticsService` from `GXCoreBL` module. An example implementation is provided for Firebase Crashlytics [GXFirebaseCrashAnalyticsService.swift](GXFirebaseCrashAnalyticsService.swift).
2. Register your crash analytics implementation as part of `GXUIApplicationExecutionEnvironment` initialization or before it. For example, as part of [extension library initialization](../SampleExternalObject/SampleExObjLibrary.swift#L9) add the following:
 
```swift
GXCoreBLServices.registerCrashAnalyticsService(GXFirebaseCrashAnalyticsService.shared)
```

Now, every time a fatal exception is thrown while a Mini App is executing, it will be forwarded to the Crashlytics profile configured for the project along with the Mini App Id and Version so it is easier to find when and under which circumstances the crash happened.

Note the provided example is for Firebase Crashlytics, but the same could applies for other Crash Analytics provider.
