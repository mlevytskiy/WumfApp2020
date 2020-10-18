package com.core.wumfapp2020.util

import com.app.api.api.WumfApi
import com.core.wumfapp2020.api.Friend
import com.core.wumfapp2020.api.GetFriendsRequest
import com.core.wumfapp2020.memory.FriendsRepository
import krafts.alex.tg.TgClient
import org.drinkless.td.libcore.telegram.TdApi
import retrofit2.await

object FriendsUtils {

    suspend fun syncFriends(friendsRepository: FriendsRepository? = null, wumfApi: WumfApi, client: TgClient): List<TdApi.User> {
        val contacts = client.getContacts()
        val request = GetFriendsRequest(contacts.userIds.map { it })
        val response = wumfApi.getFriends(friendsRequest = request).await()
        val users = getFullInfoFriends(friends = response.users, client = client)
        friendsRepository?.setWumfContacts(users)
        return users
    }

    suspend fun getUsersInfo(users: IntArray, wumfApi: WumfApi, client: TgClient) : List<TdApi.User> {
        val request = GetFriendsRequest(users.map { it })
        val response = wumfApi.getFriends(friendsRequest = request).await()
        val users = getFullInfoFriends(friends = response.users, client = client)
        return users
    }

    suspend fun getFullInfoFriends(friends: List<Friend>, client: TgClient): ArrayList<TdApi.User> {
        val users = ArrayList<TdApi.User>()
        friends.forEach {friend ->
            try {
                val user = client.getUser(friend.id)
                user.restrictionReason = friend.apps
                users.add(user)
                try {
                    user.profilePhoto?.small?.id?.let {
                        user.profilePhoto?.small?.local = client.downloadFile(it).local
                    }
                } catch (e: Exception) {
                    //ignore
                }
            } catch (ex: Exception) {
                //ignore
            }
        }
        return users
    }

}