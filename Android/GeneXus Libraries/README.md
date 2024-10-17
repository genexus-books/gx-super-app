# Super App Render Android Libraries

The [GeneXus Super App Render](../../SuperAppRender.md) comprises a set of libraries that are fetched from a public repository during the build process. These libraries are dynamically retrieved and integrated into the SDK to enhance the functionality of the Super App Render module.


## Available Channels and Repository URLs for Dependency Retrieval
To integrate the necessary dependencies into your project, the following channels are available along with their respective repository URLs. Utilize the provided URL in your configuration to retrieve these dependencies.


### Officially Releases (Maven Central)
Dependencies for the "Release" channel can be fetched from the Maven Central repository.
This Release channel contains [officially released versions](https://wiki.genexus.com/commwiki/wiki?58156,GeneXus+Super+App+Render+Releases), including hotfixes. Examples: 2.3.0 (upgrade release), 2.3.1 (hotfix for version 2.3)

### Officially Releases (Azure Artifacts)
Dependencies for officially released versions (including hotfixes) can also be retrieved from Azure Artifacts repository.

Repository URLs: 
- https://pkgs.dev.azure.com/genexuslabs/155eaada-eb3c-418f-9c98-dcbcffffae50/_packaging/android-releases/maven/v1
- https://pkgs.dev.azure.com/genexuslabs/3361ab3b-96bc-4a69-a37a-f2b255ff2f35/_packaging/releases/maven/v1

### Pre-releases: RC & Beta versions (Azure Artifacts)
This channel contains release candidates (rc/stable) and beta versions (beta/trunk).  For example, it includes versions like 2.4-beta01, 2.4-rc01, etc.

Repository URLs: 
- https://pkgs.dev.azure.com/genexuslabs/155eaada-eb3c-418f-9c98-dcbcffffae50/_packaging/android-prereleases/maven/v1
- https://pkgs.dev.azure.com/genexuslabs/3361ab3b-96bc-4a69-a37a-f2b255ff2f35/_packaging/snapshots/maven/v1


Ensure you update your [settings.gradle](../MiniAppCaller/settings.gradle) file by including the specific URLs from the repositories corresponding to the channel of interest.

## Minimum Set of Libraries Required
The minimum set of Super App Render's libraries for any Super App project includes the following: 
- com.genexus.android:FlexibleClient
- com.genexus.android:SqlDroidBase
- com.genexus.android:CoreExternalObjects
- com.genexus.android:CoreUserControls
- com.genexus.android:SmartGridLib
- com.genexus.android:SuperAppsLib

## Managing Dependencies
In case a Mini App requires a specific Super App feature (for instance, access to the camera), you need to include this new dependency in your [build.gradle](../MiniAppCaller/app/build.gradle) with the corresponding Super App Render library. Ensure that you update this configuration file appropriately to enable the desired functionalities within your Super App.
