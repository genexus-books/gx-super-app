# GeneXus Super App Render

Given an input in gxsd format, this component allows you to execute a native application within the Host that calls it.
That input has all the information regarding the screens and the Mini App logic. The Super App Render (aka Flexible Client) reads this information and does, on the one hand, the native rendering of the screens and, on the other hand interprets the logic's execution. 

## What is the gxsd format?

It's simply a compressed format whose content is the Mini App's Metadata in json format. This gxsd contains the Mini App's version. This information allows the Super App Render to take different actions. 

## How do you get the gxsd?

This metadata is the result of developing and generating a mobile app using GeneXus technology.
