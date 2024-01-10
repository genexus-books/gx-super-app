package com.examplesuperapp.application

import com.genexus.android.core.providers.EntityDataProvider

class AppEntityDataProvider: EntityDataProvider() {
    init {
        AUTHORITY = "com.genexus.superapps.bankx.appentityprovider"
        URI_MATCHER = buildUriMatcher()
    }
}