package com.core.wumfapp2020.memory.impl

import io.objectbox.converter.PropertyConverter
import java.lang.StringBuilder

private const val DIMEN = "|"

class RegistrationInfoConverter : PropertyConverter<RegistrationInfo, String> {

    override fun convertToDatabaseValue(entityProperty: RegistrationInfo?): String {
        if (entityProperty == null) {
            return ""
        }
        val strBuilder = StringBuilder()
        strBuilder.append(entityProperty.photo).append(DIMEN)
        strBuilder.append(entityProperty.name).append(DIMEN)
        strBuilder.append(entityProperty.telegramId).append(DIMEN)
        strBuilder.append(entityProperty.hasRegistration).append(DIMEN)
        strBuilder.append(entityProperty.isRegWumfChecked)
        return strBuilder.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): RegistrationInfo {
        if (databaseValue.isNullOrEmpty()) {
            return RegistrationInfo()
        }
        val result = RegistrationInfo()
        val arr = databaseValue.split(DIMEN)
        result.photo = arr[0]
        result.name = arr[1]
        try {
            result.telegramId = arr[2].toInt()
        } catch (e: Exception) {
            result.telegramId = 0
        }
        result.hasRegistration = arr[3].toBoolean()
        result.isRegWumfChecked = arr[4].toBoolean()
        return result
    }

}