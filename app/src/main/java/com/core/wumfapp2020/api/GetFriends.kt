package com.core.wumfapp2020.api

class GetFriendsRequest(val userIds: List<Int>)

class GetFriendsResponse(val users: List<Friend>)