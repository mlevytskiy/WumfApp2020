package com.core.wumfapp2020.memory

import io.objectbox.converter.PropertyConverter
import java.lang.StringBuilder

class RegistrationInfoConverter : PropertyConverter<RegistrationInfo, String> {

    override fun convertToDatabaseValue(entityProperty: RegistrationInfo?): String {
        if (entityProperty == null) {
            return ""
        }
        val strBuilder = StringBuilder()
        strBuilder.append(entityProperty.photo).append("|")
        strBuilder.append(entityProperty.name).append("|")
        strBuilder.append(entityProperty.telegramId).append("|")
        strBuilder.append(entityProperty.hasRegistration).append("|")
        strBuilder.append(entityProperty.isRegWumfChecked)
        return strBuilder.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): RegistrationInfo {
        if (databaseValue.isNullOrEmpty()) {
            return RegistrationInfo()
        }
        val result = RegistrationInfo()
        val arr = databaseValue.split("|")
        result.photo = arr[0]
        result.name = arr[1]
        result.telegramId = arr[2].toInt()
        result.hasRegistration = arr[3].toBoolean()
        result.isRegWumfChecked = arr[4].toBoolean()
        return result
    }

}