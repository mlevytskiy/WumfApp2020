package com.core.wumfapp2020.memory.impl

import android.util.Log
import io.objectbox.converter.PropertyConverter

private const val DIMEN = "|"

class HomeAppsSourceConverter : PropertyConverter<HomeAppsSource, String> {

    override fun convertToDatabaseValue(entityProperty: HomeAppsSource?): String {
        Log.i("testr", "to database entityProperty.country=" + entityProperty?.countryMCC)
        entityProperty?.let {
            return "" + entityProperty.type + DIMEN + entityProperty.countryMCC + DIMEN + entityProperty.countryName
        }?: kotlin.run {
            return ""
        }
    }

    override fun convertToEntityProperty(databaseValue: String?): HomeAppsSource {
        return if (databaseValue.isNullOrEmpty()) {
            HomeAppsSource(TYPE_IN_THE_WORLD, 0, "")
        } else {
            val result = databaseValue.split(DIMEN)
            HomeAppsSource(type = result[0].toInt(), countryMCC = result[1].toInt(), countryName = result[2])
        }
    }

}