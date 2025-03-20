# ExampleSuperApp

This document outlines the setup and troubleshooting steps for the React Native `ExampleSuperApp` project.

---

## Prerequisites

Ensure you have the following tools installed on your system before proceeding:

- [Flutter SDK](https://docs.flutter.dev/get-started/install)
- [Android Studio](https://developer.android.com/studio) (for Android development)
- [Xcode](https://developer.apple.com/xcode/) (for iOS development)
- [CocoaPods](https://cocoapods.org/) (for iOS dependency management)

---

## Getting Started

Follow these steps to set up and run the project:

### iOS

1. Navigate to the project directory:
    ```bash
    cd Flutter/example_superapp
    ```

2. Install project dependencies:
    ```bash
    flutter clean && flutter pub get
    ```

3. Navigate to the iOS directory and install CocoaPods dependencies:
    ```bash
    cd example/ios
    pod install
    ```

4. Open example/ios/Runner.xcworkspace on Xcode and Run.

#### Troubleshooting:
If you encounter the error:
```
    Unable to open base configuration reference file Pods/TargetSupportFiles
```
Clear Xcode's derived data:

```bash
cd ios
rm -rf ~/Library/Developer/Xcode/DerivedData
```

### Android

1. Navigate to the project directory:
    ```bash
    cd Flutter/example_superapp
    ```

2. Install project dependencies:
    ```bash
    flutter clean && flutter pub get
    ```

3. Open example/android/ on Android Studio and Run.
