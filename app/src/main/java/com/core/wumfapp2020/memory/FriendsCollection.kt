package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.Friend
import com.core.wumfapp2020.memory.impl.FriendsConverter
import com.core.wumfapp2020.memory.impl.MyAppsConverter
import com.core.wumfapp2020.memory.impl.MyFriendIdsConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class FriendsCollection {

    @Id(assignable = true)
    var id: Long = ID

    @Convert(converter = FriendsConverter::class, dbType = String::class)
    var friends: MutableList<Friend> = ArrayList()


    @Convert(converter = MyFriendIdsConverter::class, dbType = String::class)
    var allContacts: MutableList<String> = ArrayList()

    companion object {
        @JvmField val ID = 4L
    }

}