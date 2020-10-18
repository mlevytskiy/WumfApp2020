package com.core.wumfapp2020.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import com.app.api.api.WumfApi
import com.core.wumfapp2020.memory.UserInfoRepository
import com.core.wumfapp2020.memory.impl.Friend
import com.core.wumfapp2020.util.FriendsUtils
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import krafts.alex.tg.TgClient
import org.drinkless.td.libcore.telegram.TdApi
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.AppProvider

class PeopleWhoLikesViewModel @AssistedInject constructor(@Assisted val app: AppContainer,
                                                          @Assisted usersIds: IntArray,
                                                          private val manager: SplitInstallManager,
                                                          val sharedViewModel: SharedViewModel,
                                                          userInfoRepository: UserInfoRepository,
                                                          private val repository: UserInfoRepository,
                                                          tdClient: TgClient, wumfApi: WumfApi,
                                                        private val appProvider: AppProvider): AnyFragmentBaseViewModel() {

//    private val directions = PreOnBoardingFragmentDirections.Companion

    val users = ObservableArrayList<FriendsViewModel.ItemFriendViewModel>()

    val inProgress = ObservableBoolean(false)

    init {
        startBgJob {
            val userInfos = FriendsUtils.getUsersInfo(users = usersIds, wumfApi = wumfApi, client = tdClient)
            fillUsersList(userInfos)
            fillUsersApps()
        }
    }

    private fun fillUsersList(usersInfos: List<TdApi.User>) {
        users.clear()
        val friendsList = usersInfos.map {
            FriendsViewModel.ItemFriendViewModel(friend = convert(it), openFriendPhoto = {}, openFriendDetail = {
//                navigate(directions.actionFriendsToFriendDetail(friend = it))
            })
        }
        users.addAll(friendsList)
    }

    private fun convert(user: TdApi.User): Friend {
        return Friend(
            name = user.firstName,
            surname = user.lastName,
            photo = user.profilePhoto?.small?.local?.path,
            telegramId = user.id,
            phoneNumber = user.phoneNumber,
            apps = if (user.restrictionReason.isEmpty()) emptyList()
            else if (user.restrictionReason.contains(","))
                user.restrictionReason.split(",").filter { it.isNotEmpty() }
            else listOf(user.restrictionReason))
    }

    private suspend fun fillUsersApps() {
        users.forEach {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(app: AppContainer, usersIds: IntArray): PeopleWhoLikesViewModel
    }

}