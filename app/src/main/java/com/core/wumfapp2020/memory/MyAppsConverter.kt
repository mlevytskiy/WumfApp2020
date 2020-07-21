package com.core.wumfapp2020.memory

import io.objectbox.converter.PropertyConverter

private const val DIMEN = "|"

class MyAppsConverter : PropertyConverter<MutableList<String>, String> {

    override fun convertToDatabaseValue(entityProperty: MutableList<String>?): String {
        return when {
            entityProperty.isNullOrEmpty() -> ""
            (entityProperty.size == 1) -> entityProperty[0]
            else -> entityProperty.joinToString(separator = DIMEN)
        }
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableList<String> {
        return when {
            databaseValue.isNullOrEmpty() -> ArrayList()
            !databaseValue.contains(DIMEN) -> mutableListOf(databaseValue)
            else -> ArrayList(databaseValue.split(DIMEN))
        }
    }

}