# ExampleSuperApp

This document outlines the setup and troubleshooting steps for the React Native `ExampleSuperApp` project.

---

## Prerequisites

Ensure you have the following tools installed on your system before proceeding:

- [Node.js](https://nodejs.org/)
- [npm](https://www.npmjs.com/)
- [Xcode](https://developer.apple.com/xcode/) (for iOS development)
- [Android Studio](https://developer.android.com/studio) (for Android development)
- [CocoaPods](https://cocoapods.org/) (for iOS dependency management)

---

## Getting Started

Follow these steps to set up and run the project:

### iOS

1. Navigate to the project directory:
    ```bash
    cd ReactNative/ExampleSuperApp
    ```

2. Install project dependencies:
    ```bash
    npm install
    ```

3. Navigate to the ios directory and install CocoaPods dependencies:
    ```bash
    cd ios
    pod install
    ```

4. Return to the project root and Run the app on iOS:
    ```bash
    cd ..
    npm run ios
    ```

5. Open ios/ExampleSuperApp.xcworkspace on Xcode and Run.

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
    cd ReactNative/ExampleSuperApp
    ```

2. Install project dependencies:
    ```bash
    npm install
    ```

3. Run the app on Android:
    ```bash
    npm run android
    ```
    
    #### Troubleshooting:
    If you do not see the expected changes in the React Native code after running the Android app, bundle the React Native assets manually:

    ```bash
    react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
    ```
