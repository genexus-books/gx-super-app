# Super App Render Android Libraries

The [GeneXus Super App Render](../../SuperAppRender.md) comprises a set of libraries that are fetched from a public repository during the build process. These libraries are dynamically retrieved and integrated into the SDK to enhance the functionality of the Super App Render module.


## Available Branches and Repository URLs for Dependency Retrieval
To integrate the necessary dependencies into your project, the following branches are available along with their respective repository URLs:

### Release (Maven)
Dependencies for the "Release" branch can be fetched from the Maven Central repository.

### Preview (Azure Artifacts)
The "Preview" branch's dependencies are hosted on Azure Artifacts. Utilize the provided Azure Artifacts URL in your configuration to retrieve these dependencies.

Repository URLs: 
- https://pkgs.dev.azure.com/genexuslabs/155eaada-eb3c-418f-9c98-dcbcffffae50/_packaging/android-releases/maven/v1
- https://pkgs.dev.azure.com/genexuslabs/3361ab3b-96bc-4a69-a37a-f2b255ff2f35/_packaging/releases/maven/v1

### Beta (Azure Artifacts)
Dependencies for the "Beta" branch are also available on Azure Artifacts. Use the designated Azure Artifacts URL in your configuration to access these dependencies.

Repository URLs: 
- https://pkgs.dev.azure.com/genexuslabs/155eaada-eb3c-418f-9c98-dcbcffffae50/_packaging/android-preleases/maven/v1
- https://pkgs.dev.azure.com/genexuslabs/3361ab3b-96bc-4a69-a37a-f2b255ff2f35/_packaging/snapshots/maven/v1


Ensure you update your [settings.gradle](../MiniAppCaller/settings.gradle) file by including the specific URLs from the repositories corresponding to the branch of interest.

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
