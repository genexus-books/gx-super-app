# Crashlytics for Android

## Setup

It is possible for the Super App to receive Crashlytics reports for crashes that occur during the execution of a Mini App.

In order to achieve this, a couple of steps must be followed:

1. Make the Super App depend on the `FirebaseCrashlytics` library, which is part of the [GeneXus Libraries](/Android/GeneXus%20Libraries) set provided, through the application project's [build.gradle](/Android/MiniAppCaller/app/build.gradle) file.
2. Register the `FirebaseCrashlyticsModule` in the `ApplicationHelper` instance. For example, for the [BankingApplication](/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/application/BankingApplication.kt) sample class we would do the following:
 
```kotlin
applicationHelper.registerModule(FirebaseCrashlyticsModule())
```

Now, every time a fatal exception is thrown while a Mini App is executing, it will be forwarded to the Crashlytics profile configured for the project along with the Mini App Name, Id and Version so it is easier to find when and under which circumstances the crash happened.

## Disabling the default mechanism

In case the developer does not want to make use of the default reporting mechanism, they can disable it by adding the key below to the [superapp.json](https://github.com/genexus-colab/gx-super-app/blob/main/Android/MiniAppCaller/app/src/main/res/raw/superapp_json) configuration file:

```json
"GXSuperAppCatchUnhandledExceptions":"false"
```

After doing this, they will have to hook into the Mini App's lifecycle by providing their `LifecycleListeners.MiniApp` implementation through the following method:

```kotlin
Services.Application.lifecycle.registerMiniApplicationLifecycleListener(listener: LifecycleListeners.MiniApp)
```

particularly overriding the `onMiniAppException(miniApp: MiniApp, t: Throwable)` method, where they will handle the Throwable as they wish.

It is good to note that this last step must be donde **after** the `ApplicationHelper.onCreate` method finished executing as it is the one in charge of setting up every service under the `Services` class.
