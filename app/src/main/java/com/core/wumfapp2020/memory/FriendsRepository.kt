package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.BaseRepository
import io.objectbox.BoxStore
import io.objectbox.kotlin.query

class FriendsRepository(boxStore: BoxStore): BaseRepository<FriendsCollection>(boxStore, FriendsCollection::class.java) {

    override fun currentT(): FriendsCollection? {
        val friendsCollection = box.query {
            equal(FriendsCollection_.id, FriendsCollection.ID)
        }.findFirst()
        return friendsCollection
    }

    override fun initCache() {
        cached = currentT() ?:run {
            FriendsCollection()
        }
    }

}