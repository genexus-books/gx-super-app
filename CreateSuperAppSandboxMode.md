# Super App Sandbox Mode for Mini Apps Test and Review

## Introduction

A Mini App is an app that runs within the domain of a Super App. As such, the Mini App will probably have points of contact where it will call on services or screens specific to the Super App through the API that the Super App exposes to the Mini Apps (e.g. Payment).

During the Mini App development process, this integration with the Super App can be emulated to make the development more agile and independent from the Super App (mock API). At some point, it will be necessary to test the Mini App in a scenario closer to reality (homologation/production).

This implies that both the Mini App developer/tester and the person responsible for reviewing and approving the Mini App will need to load the Mini App within the main application, in order to perform the necessary validations and have access to all the functionalities that the Super App provides in an isolated and controlled environment.

This can be achieved by running the Super App in Sandbox mode.

By using the Sandbox mode, developers can ensure that their Mini Apps are fully functional and that reviewers can certify that they meet the requirements before being released to the end user. On the other hand, the Super App’s owner will be able to make available functionalities that only apply to it.


## Description

In production, the way to load a Mini App within the Super App is through the services provided by GeneXus Render to access the Mini App Center and obtain the list of approved and published Mini Apps for that Super App.

By using the Sandbox mode, you will be able to load and test a Mini App within the Super App before it is published.

There are two instances where this becomes necessary:

- During the development of the Mini App: the developer needs to test the Mini App with the functionalities of the Super App (integration test).
- During the review process: the Super App reviewer performs the corresponding validations before approving the Mini App and making it available to users.

GeneXus Render includes a module (SuperAppSandbox) that allows the Super App developer to provide the possibility of loading a Mini App directly, without going through the review process.

**Important**: This module should not be included in the production version of the Super App.

The steps to enable this functionality are listed below:

1. Include the 'Sandbox module' in the test/homologation version of the Super App
2. Add the 'LoadSandbox()' action in the UI to be able to load a Mini App
3. Optionally, the Super App developer can have a conditional code in the API exposed to the Mini App according to the environment (sandbox/production) using the function/method 'isSandboxEnvironment'.


### Step 1: Include the Sandbox module

This module is part of [GeneXus Super App Render]((/SuperAppRender.md)). 

**It should only be included as a dependency in the test version of the Super App and not in the production version, to prevent Mini Apps from being loaded without going through the approval process.**

For Apple: 'GXSuperAppSandbox.xcframework'
For Android: 'SuperAppsSandboxLib'

### Step 2: Load Mini App with LoadSandbox()

An action must be added at the UI level in the Super App so that the developer/reviewer can directly load the Mini App. This is done through the LoadSandbox() method included in the GeneXus Super App Render API.

This function displays a UI screen to scan the QR code that contains a URL with the Mini App information.

The URL returns the Mini App Information in a special format and can be obtained from two places, depending on who is testing the Mini App:

- For the developer who wants to test the Mini App in their development environment, the QR code will be provided by GeneXus on the home page (DeveloperMenu). (Coming soon) 
- For the reviewer, the QR code can be obtained from the Mini App Center.

Once the QR code is scanned, the Mini App is loaded into the Super App and testing can begin.

Notes:

- Mini Apps that are loaded from the development environment are not signed with the Super App's private key.
- As it is signed, a load from the Mini App Center can only be done from a Super App with its corresponding public key to that Super App version.

Here's an example of how to add this functionality on each platform to avoid this test functionality being visible in the production version.

#### Apple

In a native app with Apple, create a target to run in Sandbox mode.

![image](https://user-images.githubusercontent.com/33960187/236041221-e60282e9-13ca-4d7b-8dce-15a40eaee7a0.png)

In the [example code](https://github.com/genexus-colab/gx-super-app/blob/0448cfb714f2fb2bd55b00087e1ceabcfe581ed2/iOS/ExampleSuperApp/ProvisioningViewController.swift#L190), you can see how the action that invokes LoadSandbox is conditioned to only be visible when running for the Sandbox target. This is done through the use of Custom Flags:

![image](https://user-images.githubusercontent.com/33960187/236041329-adc044ac-f926-4923-8423-3ab5aa4d83a3.png)

#### Android

Define two flavors, "normal" and "sandbox" as Build Variant. In this way, the Super App declares the SuperAppsSandboxLib module as a dependency only for the "sandbox" flavor.

![image](https://user-images.githubusercontent.com/33960187/236041405-917a856a-e76b-4a31-81ac-34bfb0dfe87b.png)

When the "Active Build Variant" is configured with the "sandbox" flavor, the application can display a FloatingActionButton that allows it to scan the QR code screen to load a Mini App through its QR code.

![image](https://user-images.githubusercontent.com/33960187/236041504-61bd8494-d248-46d2-b42c-221cc80fe430.png)

See the [example code](https://github.com/genexus-colab/gx-super-app/blob/bc191281437d2571608da7cd040f093fb8cb5f12/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/ui/screens/main/MiniAppListHomeContent.kt#L76).

### Step 3: Conditional code based on execution mode

The Super App developer can distinguish which environment the Super App is running on, whether it is in sandbox or production mode. This allows providing specific functionalities according to the mode in the API exposed to Mini Apps.

The GeneXus Render has a method (IsSandboxEnvironment: boolean) to be able to query the execution mode.

The IsSandboxEnvironment method returns True if the class that implements the Sandbox functionality exists, and this will only be available if the corresponding module was included as a dependency (Step 1).

#### Apple
[See example](https://github.com/genexus-colab/gx-super-app/blob/0448cfb714f2fb2bd55b00087e1ceabcfe581ed2/iOS/SampleExternalObject/SampleExObjHandler.swift#L34)

#### Android
In Android, the isSandboxEnvironment method of the ISuperApps interface must be used (implemented under Services.SuperApps)

![image](https://user-images.githubusercontent.com/33960187/236041606-2d166a48-90eb-4554-b82d-aceb3b9ecfe5.png)

[See example](https://github.com/genexus-colab/gx-super-app/blob/8440b384ff2d05979bb04235cb170c0d254b6823/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/ui/screens/main/MiniAppListHomeContent.kt#L76)

Note that in this example there is a double check, to avoid showing this option in the production environment. The Services.SuperApps.Prototyping.isEnabled condition checks if the sandbox module (SuperAppsSandboxLib) is referenced. The flavor.SANDBOX checks that it is running in this "variant" mode (in case the module has been installed incorrectly for all flavors)
