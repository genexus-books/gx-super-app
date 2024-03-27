# Single Sign-On (SSO) - Android implementation

To implement SSO in Native Android Super App, follow these steps:

1. Ensure at Mini App Center that:
    - Super App is marked with `Security = True`.
    - Mini App Version is marked with `Integrated Security = True`.
2. Modify your project in order to load Mini Apps with SSO:
    - Add `LoadingSecurityOptions` to Mini App load. Look for [MainViewModel.kt](/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/viewmodel/main/MainViewModel.kt) and make the following changes:

    ```kotlin
        import com.genexus.android.core.superapps.LoadingOptions
        import com.genexus.android.core.superapps.LoadingSecurityOptions

        fun loadMiniApp(miniApp: MiniApp) {
            val options = if (!miniApp.isSecure)
                null
            else {
                val superAppToken = "Retrieve Super App Token and use it here"
                val miniAppTokenRetrievalUrl = Uri.parse("Configure Mini App Access Token retrieval URL here or in superapp.json")
                val securityOptions = LoadingSecurityOptions.Builder()
                    .withAuthTokenCheckUrl(miniAppTokenRetrievalUrl) // This line is not needed if URL is set in superapp.json
                    .withSuperAppToken(superAppToken)
                    .build()
                LoadingOptions.Builder().withSecurityOptions(securityOptions).build()
            }

            Services.SuperApps.load(miniApp, options)
            …
    }
    ```
    - URL to retrive Mini App Access Token also can be set in [superapp.json](https://github.com/genexus-colab/gx-super-app/blob/main/Android/MiniAppCaller/app/src/main/res/raw/superapp_json) configuration file by adding the key `GXSSOURLGetMiniAppAccessToken`.

    ```json
    "GXSSOURLGetMiniAppAccessToken":"Your Mini App Token Retrieval Url"
    ```

    - If both are configured, <u>URL loaded in code takes precedence</u>.

For general information on Single Sign-On in GeneXus Super Apps Technologies, please refer to this [doc](../docs/SingleSignOn.md).
