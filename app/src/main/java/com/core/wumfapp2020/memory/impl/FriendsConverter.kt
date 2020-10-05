package com.core.wumfapp2020.memory.impl

import com.core.wumfapp2020.memory.App
import io.objectbox.converter.PropertyConverter
import java.lang.StringBuilder

private const val DIMEN_1 = "|"
private const val DIMEN_2 = "#"
private const val DIMEN_3 = "^"

class FriendsConverter : PropertyConverter<MutableList<Friend>, String> {

    override fun convertToEntityProperty(value: String?): MutableList<Friend> {
        return when {
            value.isNullOrEmpty() -> ArrayList()
            !value.contains(DIMEN_1) -> mutableListOf(friendFromString(value))
            else -> value.split(DIMEN_1).map { friendFromString(it) }.toMutableList()
        }
    }

    override fun convertToDatabaseValue(p: MutableList<Friend>?): String {
        p?.let { list->
            if (list.isEmpty()) {
                return ""
            }
            val strBuilder = StringBuilder()
            list.forEach {
                friendToString(it, strBuilder)
                strBuilder.append(DIMEN_1)
            }
            strBuilder.setLength(strBuilder.length - 1)
            return strBuilder.toString()
        } ?:run {
            return ""
        }
    }

    private fun friendFromString(str: String): Friend {
        if (!str.contains(DIMEN_2)) {
            return Friend()
        }
        val strProperties = str.split(DIMEN_2)
        return Friend(
            name = strProperties[0],
            surname = strProperties[1],
            telegramId = strProperties[2].toInt(),
            phoneNumber = strProperties[3],
            photo = strProperties[4],
            apps = if (strProperties[5].isEmpty()) emptyList() else strProperties[5].split(DIMEN_3))
    }

    private fun friendToString(friend: Friend, strBuilder: StringBuilder) {
        strBuilder.append(friend.name).append(DIMEN_2)
            .append(friend.surname).append(DIMEN_2)
            .append(friend.telegramId).append(DIMEN_2)
            .append(friend.phoneNumber).append(DIMEN_2)
            .append(friend.photo).append(DIMEN_2)
            .append(if (friend.apps.isEmpty()) "" else friend.apps.joinToString(separator = DIMEN_3))
    }

}