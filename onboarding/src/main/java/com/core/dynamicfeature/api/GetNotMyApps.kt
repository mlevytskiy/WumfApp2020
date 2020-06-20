package com.app.api.api

import androidx.annotation.Keep

class GetNotMyAppsRequest(@Keep val inCountry: Boolean = false, @Keep val country: String = "",
                          @Keep val allWorld: Boolean = false, @Keep val friends: List<Int>,
                          @Keep val amongFriends: Boolean = false)

class GetNotMyAppsResponse(@Keep val apps: List<App>)

class App(@Keep val packageName: String, @Keep val whoLikes: List<Int>)