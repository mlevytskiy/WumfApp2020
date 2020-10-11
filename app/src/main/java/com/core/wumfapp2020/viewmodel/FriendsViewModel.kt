package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.app.api.api.WumfApi
import com.core.wumfapp2020.api.GetFriendsRequest
import com.core.wumfapp2020.fragment.FriendsFragmentDirections
import com.core.wumfapp2020.memory.FriendsRepository
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.memory.impl.Friend
import com.core.wumfapp2020.util.FriendsUtils
import com.google.android.play.core.splitinstall.SplitInstallManager
import krafts.alex.tg.TgClient
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.AppProvider
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val manager: SplitInstallManager,
                                           private val sharedViewModel: SharedViewModel,
                                           userInfoRepository: UserInfoRepository,
                                           private val friendsRepository: FriendsRepository,
                                           private val appProvider: AppProvider,
                                           private val wumfApi: WumfApi,
                                           private val tdClient: TgClient): AnyFragmentBaseViewModel() {

    private val directions = FriendsFragmentDirections.Companion

    val inProgress = ObservableBoolean(false)
    val friends = ObservableArrayList<ItemFriendViewModel>()

    init {
        fillFriendsListFromMemory()
        startBgJob {
            fillFriendsApps()
        }
    }

    fun fillFriendsListFromMemory() {
        friends.clear()
        val friendsList = friendsRepository.current()?.friends?.map {
            ItemFriendViewModel(friend = it, openFriendPhoto = {}, openFriendDetail = {
                navigate(directions.actionFriendsToFriendDetail(friend = it))
            })
        } ?: ArrayList()
        friends.addAll(friendsList)
    }

    suspend fun fillFriendsApps() {
        friends.forEach {
            it.apps.forEach { itemApp->
                val appContainer = appProvider.getAppContainer(itemApp.appId)
                itemApp.tmpAppContainer = appContainer
            }
            it.apps.forEach {itemApp->
                itemApp.appContainer.set(itemApp.tmpAppContainer)
                itemApp.tmpAppContainer = null
            }
        }
    }


    fun loadData() {
        asyncCall {
            FriendsUtils.syncFriends(friendsRepository = friendsRepository, wumfApi = wumfApi, client = tdClient)
            fillFriendsListFromMemory()
            fillFriendsApps()
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