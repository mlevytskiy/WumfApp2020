package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.Friend
import com.core.wumfapp2020.memory.impl.FriendsConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class FriendsCollection {

    @Id(assignable = true)
    var id: Long = ID

    @Convert(converter = FriendsConverter::class, dbType = String::class)
    var friends: MutableList<Friend> = ArrayList()

    companion object {
        @JvmField val ID = 4L
    }

}