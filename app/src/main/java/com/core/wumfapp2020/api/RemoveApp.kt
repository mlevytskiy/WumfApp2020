package com.app.api.api

import androidx.annotation.Keep

class RemoveAppRequest(@Keep val app: String)

class RemoveAppResponse(@Keep val apps: String)