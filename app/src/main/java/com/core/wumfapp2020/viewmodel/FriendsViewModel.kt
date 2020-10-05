package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.core.wumfapp2020.fragment.FriendsFragmentDirections
import com.core.wumfapp2020.memory.FriendsRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.memory.impl.Friend
import com.google.android.play.core.splitinstall.SplitInstallManager
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.AppProvider
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val manager: SplitInstallManager,
                                           private val sharedViewModel: SharedViewModel, userInfoRepository: UserInfoRepository,
                                           private val friendsRepository: FriendsRepository,
                                            private val appProvider: AppProvider): AnyFragmentBaseViewModel() {

    private val directions = FriendsFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)
    val friends: List<ItemFriendViewModel>

    init {
        friends = friendsRepository.currentT()?.friends?.map {
            ItemFriendViewModel(friend = it, openFriendPhoto = {}, openFriendDetail = {
                navigate(directions.actionFriendsToFriendDetail(friend = it))
            })
        } ?: ArrayList()
        startBgJob {
            friends.forEach {
                it.apps.forEach {itemApp->
                    val appContainer = appProvider.getAppContainer(itemApp.appId)
                    itemApp.tmpAppContainer = appContainer
                }
                it.apps.forEach {itemApp->
                    itemApp.appContainer.set(itemApp.tmpAppContainer)
                    itemApp.tmpAppContainer = null
                }
            }
        }
    }

    class ItemFriendViewModel(
        val friend: Friend,
        val openFriendDetail: () -> Unit,
        val openFriendPhoto: () -> Unit
    ) {
        val apps: List<ItemAppViewModel> = if (friend.apps.isEmpty()) {
            emptyList()
        } else {
            friend.apps.map { ItemAppViewModel(it, openFriendDetail) }
        }

        fun onClickFriend() {
            openFriendDetail()
        }

        fun onClickPhoto() {
            openFriendPhoto()
        }

    }

    class ItemAppViewModel(val appId: String, val openFriendDetail: () -> Unit) {
        val appContainer: ObservableField<AppContainer?> = ObservableField()
        var tmpAppContainer: AppContainer? = null

        fun onClickFriend() {
            openFriendDetail()
        }
    }

}