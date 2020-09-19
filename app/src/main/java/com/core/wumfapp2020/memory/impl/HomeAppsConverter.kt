package com.core.wumfapp2020.memory.impl

import com.core.wumfapp2020.memory.App
import io.objectbox.converter.PropertyConverter

private const val DIMEN_1 = "|"
private const val DIMEN_2 = "#"
private const val DIMEN_3 = "^"

class HomeAppsConverter : PropertyConverter<MutableList<App>, String> {

    override fun convertToDatabaseValue(entityProperty: MutableList<App>?): String {
        return when {
            entityProperty.isNullOrEmpty() -> ""
            (entityProperty.size == 1) -> appToString(entityProperty[0])
            else -> entityProperty.map { appToString(it) }.joinToString(separator = DIMEN_1)
        }
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableList<App> {
        return when {
            databaseValue.isNullOrEmpty() -> ArrayList()
            !databaseValue.contains(DIMEN_1) -> mutableListOf(appFromString(databaseValue))
            else -> databaseValue.split(DIMEN_1).map { appFromString(it) }.toMutableList()
        }
    }

    private fun appToString(app: App) : String {
        if (app.whoLikes.isEmpty()) {
            return app.packageName
        }
        return app.packageName + DIMEN_3 + app.whoLikes.joinToString (separator = DIMEN_2)
    }

    private fun appFromString(str: String): App {
        if (!str.contains(DIMEN_3)) {
            return App(str, emptyList())
        }
        val strProperties = str.split(DIMEN_3)
        return App(packageName = strProperties[0], whoLikes = strProperties[1].split(DIMEN_2).map { it.toInt() })
    }

}