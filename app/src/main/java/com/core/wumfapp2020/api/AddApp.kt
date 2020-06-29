package com.app.api.api

import androidx.annotation.Keep

class AddAppRequest(@Keep val app: String)

class AddAppResponse(@Keep val apps: String)