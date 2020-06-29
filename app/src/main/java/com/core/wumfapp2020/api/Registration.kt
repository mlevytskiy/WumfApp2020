package com.app.api.api

import androidx.annotation.Keep

class RegistrationRequest(@Keep val userId: String, @Keep val friendsList: String = "",
                          @Keep val createdPasswordHash: String, @Keep val displayName: String,
                          @Keep val country: String)

class RegistrationResponse(@Keep val token: String, @Keep val friendsList: String = "")