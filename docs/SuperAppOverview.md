# GeneXus Super App Technologies

Native mobile applications are well received in our daily life, but there are still many things that can be done to improve the user's experience.

Before users get the service from a native mobile application, they often have to go through the process of downloading → installing → registering the application. 

In addition, they can only keep a limited number of native mobile applications on their phones due to constraints of storage capability.  What’s more, it's not easy to share data between different native mobile apps.

The evolution of these applications is, in general, very complicated, as their design was done thinking about a single solution.

The Web is the ideal platform to avoid these problems, but it's by far still imperfect.

Besides the fact that Web applications can´t take advantage of the capabilities provided by the native operating system, it's usually difficult to design a Web application whose performance can actually match or surpass a similar native mobile application.

On a native mobile device, users get services or content outside the browser very frequently. Naturally, they would like all their applications to be consistent with user accounts, login status, and user interaction across the whole system.

Moreover, sometimes users may want to share some data with an application if they really trust it. However, for some frequently requested information, such as the personal phone number of the current device or the contact list, there isn't a good way for the users to grant permissions on the Web.

In the middle of both solutions, Super Apps were born in China. They are basically service aggregators. WeChat, Taobao, and Baidu were the first aggregators that transformed their applications into Super Apps, opening their platforms so that third parties could include new services within their ecosystem.

GeneXus follows this strategy. Any organization has the possibility of making its own Super App, hence creating new digital ecosystems that offer a variety of services to its community. These services are displayed dynamically through Mini Apps and may be developed by the Super App’s organization itself, or by third parties.

## What are Super Apps and Mini Apps?

### Super Apps

A Super App is a **native** mobile application that can host small applications inside, as a way to provide **a variety of services**. 

It can be described as an ecosystem of services that coexist within the same application (the Super App domain) but that are not necessarily related to each other.

These small applications that can be used within a Super App have the following characteristics:

  - They are hosted by the Super App. 
  - They consume services exposed by the Super App. E.g. Login, Payments, etc.
  - They are dynamically loaded in the Super App according to the user’s request.

These small applications that live inside a Super App are called Mini Apps.

### Mini Apps

A Mini App is a **singular**, small application that solves **a specific problem** within a Super App domain.

Compared to a regular application, they need to be understood very fast, especially for infrequent use. 
It is recommended to offer a user experience as simple as possible and try not to replicate a fully blown application.

Unlike an application downloaded from the platform’s store, the Mini App doesn't take up extra storage space on the device. They are loaded dynamically according to user request and from different discovery mechanisms such as search, QR codes, user location, etc.

A Mini App is also a native mobile application, but you don’t need to compile and publish it in the platform’s stores.

## Why should you consider creating a Super App

Building this kind of Super App and its Mini Apps ecosystem has several business advantages.

### For end-users

  - Instant access to contextualized services
  - Less friction 
      - No download, registration, etc. is required to accomplish their purpose.
  - Security by default
      - The user would only have to register sensitive information once in the Super App and not in each Mini App.

### For Super App Owners

  - Provide more services through partners (for example: services of a municipality, health services of a government, services of a housing complex, financial institution or any company, payment services of various entities, among others).
- It allows an incremental development of their business solution. 

### For Mini App Owners

  - Place your application in an already established ecosystem.
  - Simplified development: as it's integrated into a Super App, many of the services considered sensitive (login, payments) are delegated to it.
  - Productivity: It's just building a GeneXus application.
  - Speed of deployment.

## How does a Super App work?

For a Super App to work, it needs a component that enables other applications to load dynamically.

GeneXus has an essential technology to give any native mobile application the possibility of dynamically loading Mini Apps. This component is called [Super App Render](SuperAppRender.md) (a.k.a. GeneXus Flexible Client) and is available for both the Android and Apple platforms.

What the Super App Render does is to render pure native mobile applications, similarly to what the OS does. They are not applications running inside a browser.

If you already have an application that hasn't been developed with GeneXus, but that you would like to turn into a Super App, you could incorporate the Super App Render technology in your current application (as it is also licensed separately).

Thus, you can also turn any native mobile application into a Super App, obtaining all its advantages: dynamic application loading, integration with partners, incremental development, and many more. 

# Key elements when building a Super App

There are three key components to consider when building a Super App:

- The [Super App development](HowToTransformYourAppToASuperApp.md)
- The [Mini Apps development](MiniApp.md)
- The [Mini App Center](Provisioning.md) or Mini App provisioning

The following diagram shows the overall architecture:
![SuperAppArchitecture](https://user-images.githubusercontent.com/33960187/177836808-6db764b5-b7b3-4ccb-9c94-0142228785c5.png)

A Super App can be created from scratch, but also any application can be converted to a Super App with the inclusion of the Super App Render. The Super App Render is responsible for invoking and presenting to the user the Mini App on Super App screen. All this without previously installing the Mini App.

When a Mini App is selected, it takes control of the screen and allows the user to perform certain actions. The Mini App can interact with the Super App and get important information about the user or context. Upon finishing, the Mini App is closed, and control is returned to the Super App.

The Mini App Center is fully controlled by the Super App owner, being able to allow or revoke credentials to developers at any given time. While a developer remains authorized, it is fully autonomous in the development and uploading of Mini Apps.
