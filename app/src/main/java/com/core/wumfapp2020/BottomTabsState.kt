package com.core.wumfapp2020

sealed class BottomTabsState

object GoneBottomTabsState : BottomTabsState()
data class VisibleBottomTabsState(val selectedTab: Int): BottomTabsState()