# Single Sign-On (SSO) - iOS implementation

To implement SSO in Native iOS Super App, follow these steps:

1. Ensure at Mini App Center that:
    - Super App is marked with `Security = True`.
    - Mini App Version is marked with `Integrated Security = True`.

2. Include [GXGAMUI](https://github.com/GeneXus-SwiftPackages/GXGAMUI.git) framework

3. Add `SSO_ENABLED` compiler flag:
    - In Xcode:
        Go to your project's Build Settings, find the Other Swift Flags section, and add `-DSSO_ENABLED.`

    - Via command line:
        Use the -D flag when compiling with xcodebuild or similar tools:
        ```bash
            xcodebuild ... -DSSO_ENABLED
        ```

4. Modify your project in order to load Mini Apps with SSO:
    - Look for [AppDelegate.swift](/iOS/ExampleSuperApp/AppDelegate.swift) and make the following changes:

        - Import `GXGAM` and `GXGAMUI`
        - Register Super App Access Token Provider:

        ```swift
            #if SSO_ENABLED
            /// Include GXGAM & GXGAMUI modules if supporting SSO
            import GXGAM
            import GXGAMUI
            #endif // SSO_ENABLED

            #if SSO_ENABLED
            GXMiniAppsManager.registerSuperAppAccessTokenProvider { miniAppId, completion in
                /// This closure is called when the super app access token is required for requesting an authorization token for the mini-app with the given id.
                /// Configuring GXSSOURLGetMiniAppAccessToken in Info.plist is required (GXSSOURLCheckMiniAppScope is optional).
                let superAppToken: GXMiniAppsManager.SuperAppAccessToken? = (
                    token: "Retrieve Super App access token somehow and set it here",
                    type: "Retrieve Super App token type somehow and set it here"
                )
                completion(.success(superAppToken))
            }
            #endif // SSO_ENABLED
        ```
    
    - Look for [ProvisioningViewController.swift](/iOS/ExampleSuperApp/ProvisioningViewController.swift) and make the following changes:

        - Modify loadMiniApp function:

        ```swift
            private func loadMiniApp(_ miniApp: GXMiniAppInformation, from miniAppCell: MiniAppTableViewCell? = nil) {
                guard loadingMiniApp == nil else {
                    if loadingMiniApp?.id != miniApp.id {
                        print("Mini app \(loadingMiniApp!.id) is still loading...")
                    }
                    return
                }
                loadingMiniApp = miniApp
                if let cell = miniAppCell ?? visibleCell(for: miniApp.id) {
                    updateCellLoadingIndicator(cell)
                }

                /// Call GXMiniProgramLoader loadMiniProgram method to load a mini app. Use completion handler to handle error or to provide feedback.
                /// Loading the mini app will hide UIApplication.shared.keyWindow, and it will be restored when the mini app exits to the host super app.

                GXMiniAppsManager.loadMiniApp(info: miniApp) { [weak self] error in
                    DispatchQueue.main.async {
                        guard let sself = self, sself.loadingMiniApp?.id == miniApp.id else { return }
                        sself.loadingMiniApp = nil
                        if let cell = sself.visibleCell(for: miniApp.id) {
                            sself.updateCellLoadingIndicator(cell)
                        }
                        if let error {
                            #if SSO_ENABLED
                            if case .authorizationSSO(let ssoError) = error {
                                /// Handle SSO error.
                                switch ssoError {
                                case .scopeMissing(let scopes):
                                    /// Handling this error is required if GXSSOURLCheckMiniAppScope was configured (which returned is_allowed = false resulting in this error).
                                    sself.handleMissingScopes(scopes, miniApp: miniApp)
                                    return
                                default:
                                    /// Handle other errors as needed.
                                    break
                                }
                            }
                            #endif // SSO_ENABLED
                            print("There was an error loading mini app \(miniApp.id): \(error.localizedDescription)")
                        }
                    }
                }
	        }
        ```

        - Implement handleMissingScopes function:
        
        ```swift
            #if SSO_ENABLED
            private func handleMissingScopes(_ scopes: String, miniApp: GXMiniAppInformation) {
                ///	An option is to ask the user to accept the scopes
                let msg = "Mini App '\(miniApp.name)' needs your permission to access the following scopes: \(scopes)"
                let alertController = UIAlertController(title: nil, message: msg, preferredStyle: .alert)
                alertController.addAction(.init(title: "Accept", style: .default, handler: { [weak self] _ in
                    /// Update where needed for GXSSOURLCheckMiniAppScope to return is_allowed = true on the next call, and then call GXMiniAppsManager.loadMiniApp(info: miniApp) again.
                    guard let sself = self, sself.loadingMiniApp == nil else {
                        return
                    }
                    sself.loadMiniApp(miniApp)
                }))
                alertController.addAction(.init(title: "Reject", style: .cancel))
                present(alertController, animated: true)
            }
            #endif // SSO_ENABLED
        ```

For general information on Single Sign-On in GeneXus Super Apps Technologies, please refer to this [document](../docs/SingleSignOn.md).
