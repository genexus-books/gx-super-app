# GeneXus Super App Render

Super App Render is the component that empowers you to transform your app into a Super App, facilitating the dynamic loading, rendering, and on-demand display of Mini Apps. Through these Mini Apps, you can seamlessly incorporate products and services, whether they are offered by third parties or are your own, into the native application. Users can download these Mini Apps within the Super App using various search mechanisms.

Additionally, it establishes a communication channel that allows Mini Apps to securely access Super App services, including payment services and single sign-on, ensuring a smooth and frictionless user experience.

## How it works?

Given a Mini App metadata (an input in gxsd format), this component allows you to execute a native application within the host app (Super App).
That input has all the information regarding the screens and the Mini App logic. The Super App Render (aka Flexible Client) reads this information and does, on the one hand, the native rendering of the screens and, on the other hand interprets the logic's execution. 

## What is the gxsd format?

It's simply a compressed format whose content is the Mini App's Metadata in json format. This gxsd contains the Mini App's version. This information allows the Super App Render to take different actions. 

## How do you get the gxsd?

This metadata is the result of developing and generating a mobile app using GeneXus technology.

## How to install the Super App Render?
There is an SDK for each platform: [Apple frameworks](iOS/GeneXus%20Frameworks/README.md) and [Android libraries](Android/GeneXus%20Libraries/README.md)
