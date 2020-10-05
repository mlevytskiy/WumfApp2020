package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.BaseRepository
import com.core.wumfapp2020.memory.impl.Friend
import io.objectbox.BoxStore
import io.objectbox.kotlin.query
import org.drinkless.td.libcore.telegram.TdApi

class FriendsRepository(boxStore: BoxStore): BaseRepository<FriendsCollection>(boxStore, FriendsCollection::class.java) {

    override fun currentT(): FriendsCollection? {
        return box.query {
            equal(FriendsCollection_.id, FriendsCollection.ID)
        }.findFirst()
    }

    fun setAllContacts(allContacts: List<Int>) {
        cached?.allContacts = allContacts.map { it.toString() }.toMutableList()
        save()
    }

    fun setWumfContacts(contacts: List<TdApi.User>) {
        cached?.friends = contacts.map { convert(it) }.toMutableList()
        save()
    }

    private fun convert(user: TdApi.User): Friend {
        return Friend(
            name = user.firstName,
            surname = user.lastName,
            photo = user.profilePhoto?.small?.local?.path,
            telegramId = user.id,
            phoneNumber = user.phoneNumber,
            apps = if (user.restrictionReason.isEmpty()) emptyList() else user.restrictionReason.split(","))
    }

    override fun initCache() {
        cached = currentT() ?:run {
            FriendsCollection()
        }
    }

}