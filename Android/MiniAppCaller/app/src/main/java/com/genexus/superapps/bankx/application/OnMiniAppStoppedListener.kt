package com.genexus.superapps.bankx.application

import com.genexus.android.core.application.LifecycleListeners
import com.genexus.android.core.base.metadata.GenexusApplication
import com.genexus.android.core.superapps.MiniApp

abstract class OnMiniAppStoppedListener: LifecycleListeners.MiniApp {
    override fun onMiniAppCreated(miniApp: MiniApp) {}
    override fun onMiniAppStarted(miniApp: MiniApp) {}
    override fun onMiniAppResumed(miniApp: MiniApp) {}
    override fun onMiniAppPaused(miniApp: MiniApp, superApp: GenexusApplication) {}
    override fun onMiniAppException(miniApp: MiniApp, t: Throwable) {}
}
