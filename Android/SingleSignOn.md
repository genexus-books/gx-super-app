# Single Sign-On (SSO) - Android implementation

To implement SSO in Native Android Super App, follow these steps:

1. Ensure at Mini App Center that:
    - Super App is marked with `Security = True`.
    - Mini App Version is marked with `Integrated Security = True`.
2. Modify your project in order to load Mini Apps with SSO:
    Look for [MainViewModel.kt](/Android/MiniAppCaller/app/src/main/java/com/genexus/superapps/bankx/viewmodel/main/MainViewModel.kt) and make the following changes:
    
    - Add `superAppTokenCallback` function to get Application Token. 
     ```kotlin
        import com.genexus.android.core.superapps.OnApplicationTokenRequiredCallback

        private val superAppTokenCallback = object : OnApplicationTokenRequiredCallback {
            override fun getApplicationToken(): ApplicationToken {
                val superAppTokenType = "Retrieve Super App token type somehow and set it here" // TODO
                val superAppToken = "Retrieve Super App access token somehow and set it here"   // TODO
                return ApplicationToken(superAppTokenType, superAppToken)
            }
        }
    ```
    
    - Add `LoadingSecurityOptions` to Mini App load:

    ```kotlin
        import com.genexus.android.core.superapps.LoadingOptions
        import com.genexus.android.core.superapps.LoadingSecurityOptions
        import com.genexus.android.core.superapps.security.MiniAppScopesRequestResult
        import com.genexus.android.core.superapps.security.MiniAppTokenRequestResult

        fun loadMiniApp(miniApp: MiniApp, hasScopesErrorOccurred: Boolean) {
            val options = if (!miniApp.isSecure)
                null
            else {
                val miniAppTokenRetrievalUrl = Uri.parse("Configure Mini App access token retrieval URL here or in superapp.json")  // This line is not needed if URL is set in superapp.json
                val securityOptions = LoadingSecurityOptions.Builder()
                    .withAuthTokenCheckUrl(miniAppTokenRetrievalUrl) // This line is not needed if URL is set in superapp.json
                    .withAuthTokenRetrievalCallback(superAppTokenCallback)
                    .build()
                LoadingOptions.Builder().withSecurityOptions(securityOptions).build()
            }

            Services.SuperApps.load(miniApp, options).addOnFailureListener(object : OnFailureListener<LoadError> {
                override fun onFailure(error: LoadError, extra: Any?) {
                    _state.value = when (error) {
                        LoadError.AUTHORIZATION_TOKEN -> {
                            val errorMessage = (extra as MiniAppTokenRequestResult).messages?.errorText 
                                ?: "MiniApp access token not valid"
                            State.Error(errorMessage)
                        }
                        LoadError.AUTHORIZATION_SCOPES ->  {
                            if (!hasScopesErrorOccurred)
                                State.Warning(error, extra)
                            else {
                                val errorMessage = (extra as MiniAppScopesRequestResult).messages?.errorText
                                    ?: "Missing scopes have not been accepted"
                                State.Error(errorMessage)
                            }
                        }
                        else -> State.Error("MiniApp loading failed because of '$error'")
                    }
                }
            })
        }
    ```
    - URL to retrive Mini App Access Token also can be set in [superapp.json](https://github.com/genexus-colab/gx-super-app/blob/main/Android/MiniAppCaller/app/src/main/res/raw/superapp_json) configuration file by adding the key `GXSSOURLGetMiniAppAccessToken`.

    ```json
    "GXSSOURLGetMiniAppAccessToken":"Your Mini App Token Retrieval Url"
    ```

    - If both are configured, <u>URL loaded in code takes precedence</u>.

For general information on Single Sign-On in GeneXus Super Apps Technologies, please refer to this [doc](../docs/SingleSignOn.md).
