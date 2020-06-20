package com.app.api.api

import androidx.annotation.Keep

class LoginRequest(@Keep val userId: String, @Keep val friendsList: String = "", @Keep val passwordHash: String)

data class LoginResponse(@Keep val token: String, @Keep val friendsList: String = "")