package com.core.wumfapp2020.memory.impl

const val TYPE_IN_THE_WORLD = 0
const val TYPE_IN_MY_COUNTRY = 1
const val TYPE_IN_ANOTHER_COUNTRY = 2
const val TYPE_AMONG_FRIENDS = 3
class HomeAppsSource(val type: Int, val countryMCC: Int, val countryName: String)