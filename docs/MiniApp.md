# Mini App 

A Mini App is an app that will be executed within a Super App domain.
It's composed of a metadata (a .gxsd file) that results from the process of generating an app with GeneXus.

## Steps for creating a Mini App

### Enroll & Provisioning 

First, you have to create the app in the Mini App Center. To do this, you have to send the Mini App developer's contact information (organization, name, e-mail) and the Mini App's information to the Super App creator. 

Mini App's required information:

- **Identifier:** it must be unique and have a reverse DNS format (example: com.example.myminiapp)
- **Name** 
- **Description** 
- **Icon:** an image that represents your Mini App in the different Super App's sections, for example when the entire list of available Mini Apps is displayed.
- **Card Image:** it's an alternative image, to be shown in a widget or detailed view of the Mini App.
- **Banner Image:** it's an alternative image, to be displayed in the promoted content section.

A Mini App will be available only for a given Super App. If you want the same Mini App to be available for more than one Super App, you have to create separate Mini Apps, one for each Super App. 

### Development 

GeneXus is the low-code tool used to design, create and maintain the Mini Apps. 
The development process of a Mini App is the same as for any other mobile app developed with GeneXus. 
To integrate the Mini App to the Super App, the following considerations must be taken into account:

- Super App API 
	- The Mini App has interaction points with the Super App through the API of the services it offers. You need to request that API and integrate it into the GeneXus Knowledge Base (KB) as an External Object.

- Install the GeneXusMiniApps Module
	- This module is directly installed into the KB from the Manage Module References. It provides the MiniApp.Exit() method, that enables you to close a Mini App and return to the Super App. This option should be in the Mini App's appbar.

- The Mini App is loaded dynamically from the Super App, so some restrictions apply regarding any other GeneXus app:
	- It must be an Online app.
	- The use of User Controls and Extensions will be limited by those declared in the Super App. 
	
### Prototyping and Testing

The developed Mini App will be integrated with the Super App's own services. Hence, it's necessary to have a sandbox version of that Super App to prototype the app during the development stage.

That Super App can be made available through platform test mechanisms (links to an Android APK, Apple Testflight, MS App Center, etc.).

### Deploy and Publish

Once the Mini App's development is done, the next stage is to submit the Mini App for review.
Steps to follow:
 
 1. Deploy the Mini App's services (backend) into Production.
 2. Get the Mini App's metadata (.gxsd file)
 These steps can be performed through the [Application Deployment Tool](https://wiki.genexus.com/commwiki/servlet/wiki?32092,Toc%3AApplication+Deployment+tool). To generate the metadata, the "Enable KBN" property must be enabled.
 
 3. Send the metadata to the Super App owner with the following information to be published on the Mini App Center.
	- Backend Services URL 
	- App Name 
	- App Main Object Type (Menu | Panel)
4. The Mini App's review by the Super App owner starts here.
5. This process may require sending new versions of the Mini App. Once it's completed, the Mini App is in Ready state. 

**Important:** the backend services' version (step 1) must always match the metadata's version (step 2) sent to the Mini App Center.

## Additional Resources

You might also be interested in:

- [Mini App Object](https://wiki.genexus.com/commwiki/wiki?58037,Category%3AMini+App+object)
- [Mini App Development Process](https://wiki.genexus.com/commwiki/wiki?58172,Mini+App+Development+Process)
