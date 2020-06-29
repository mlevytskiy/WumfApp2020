package com.app.api.api

import androidx.annotation.Keep

class CheckRegistrationRequest(@Keep val userId: String)

class CheckRegistrationResponse(@Keep val hasInDb: Boolean)