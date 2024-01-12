# Requirements to turn your app into a Super App

Steps to take to turn an App into a Super App:

1. Integrate the GeneXus Super App Render (aka Flexible Client). It's the main component and is in charge of doing the **Mini App render**
2. Provide the communication interface in the Super App, which will be called from the integrated Mini Apps

For Mini App Test & Review:

3. Provide a Super App with Sandbox Mode

For Production Environment:

4. Integrate the **module to access the Mini Apps Center**, so as to get the Mini Apps and be able to load a Mini App
5. Design the UX for the discovery of those Mini Apps

## Integrate the [Super App Render](/docs/SuperAppRender.md) library.

This component is what allows an App to become a Super App.

## Provide a **communication interface** to be called from the integrated Mini Apps. 

A Mini App can require certain actions from its Super App. To do this, these services are implemented in the Super App (which can be with or without UI) and then its API is exposed to the Mini Apps. 

As an example, let's suppose there's a Super App associated with a financial institution where the users register their different means of payment, among other things.

The Super App offers its users a variety of services (shopping in stores, paying for transport, etc.) through different Mini Apps. The user carries out the purchase process from the Mini App, but at the moment of paying they will be redirected to the Super App so as to choose the payment method. Once the payment is completed, the result of the operation is returned to the Miini App. In this way, the user not only does not have to enter their payment methods in each of the services, but also the Super App does not share that information with the mini apps.

There are 2 examples of how to integrate the Super App Render component into a native application in this repository:

- [iOS example](/iOS/SampleExternalObject/README.md)
- [Android example](/Android/MiniAppCaller/README.md)
- [Android Flutter example](/Flutter/example_superapp/example/android/README.md)


## Provide a Super App with Sandbox Mode
In production, the way to load a Mini App within the Super App is through the services provided by GeneXus Render to access the Mini App Center and obtain the list of approved and published Mini Apps for that Super App (Step 4).

Using the [Sandbox mode](CreateSuperAppSandboxMode.md), your Mini App providers will be able to Test a Mini App, and you will be able to Review a Mini App within the Super App before it is published.


## Access to the Mini Apps Center to get the Mini Apps and UX design for their discovery.

To get the list of Mini Apps available for the Super App, the communication API with the Mini Apps Center of the [Super App Render](/SuperAppRender.md) is used.

- [iOS example](/iOS/README.md)
- [Android example](/Android/README.md)
- [iOS Flutter example](/Flutter/example_superapp/ios/README.md)
- [Android Flutter example](/Flutter/example_superapp/android/README.md)
- [iOS React Native example](/ReactNative/ExampleSuperApp/ios/README.md)

To learn how to turn an application created with GeneXus into a Super App read [How to Create a Super App?](https://wiki.genexus.com/commwiki/wiki?50906,How+to+create+a+Super+App%3F).
