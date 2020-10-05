package com.core.wumfapp2020.viewmodel

import com.core.wumfapp2020.memory.impl.Friend
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import wumf.com.appsprovider2.AppContainer

class FriendDetailViewModel @AssistedInject constructor(@Assisted val friend: Friend): AnyFragmentBaseViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(friend: Friend): FriendDetailViewModel
    }

}