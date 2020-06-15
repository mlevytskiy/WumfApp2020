package com.core.wumfapp2020.memory

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class UserInfo {

    @Id(assignable = true)
    var id: Long = ID
    var statusBarHeight: Int = 0
    var language: Byte = 0
    var phoneNumberFromSystem: String? = null
    var registrationToken: String? = null

    constructor()

    constructor(statusBarHeight: Int = 0,
                language: Byte = 0,
                phoneNumberFromSystem: String? = null,
                registrationToken: String? = null) {
        this.statusBarHeight = statusBarHeight
        this.language = language
        this.phoneNumberFromSystem = phoneNumberFromSystem
        this.registrationToken = registrationToken
    }

    @Convert(converter = RegistrationInfoConverter::class, dbType = String::class)
    var telegramUser: RegistrationInfo? = null //tmp registrationInfo

    companion object {
        @JvmField val ID = 1L
    }
}