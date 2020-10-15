package com.core.wumfapp2020.api

import androidx.annotation.Keep

class GetFriendsRequest(@Keep val userIds: List<Int>)

class GetFriendsResponse(@Keep val users: List<Friend>)