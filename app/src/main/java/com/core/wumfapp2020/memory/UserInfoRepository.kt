package com.core.wumfapp2020.memory

import io.objectbox.BoxStore
import io.objectbox.kotlin.query

class UserInfoRepository(boxStore: BoxStore): BaseRepository<UserInfo>(boxStore, UserInfo::class.java) {

    override fun initCache() {
        cached = currentT() ?:run {
            UserInfo(statusBarHeight = -1, language=0, phoneNumberFromSystem=null)
        }
    }

    override fun currentT(): UserInfo? {
        val userInfo = box.query {
            equal(UserInfo_.id, UserInfo.ID)
        }.findFirst()
        return userInfo
    }

    fun getStatusBarHeight(): Int {
        cached?.let {
            return it.statusBarHeight
        } ?:run {
            return currentT()?.statusBarHeight ?: 0
        }
    }

    fun setStatusBarHeight(height: Int) {
        cached?.statusBarHeight = height
        save()
    }

    fun getLanguage(): Byte {
        cached?.let {
            return it.language
        } ?:run {
            return currentT()?.language ?: -1
        }
    }

    fun setLanguage(language: Byte) {
        cached?.language = language
        save()
    }

    fun setPhoneNumberFromSystem(phoneNumber: String) {
        cached?.phoneNumberFromSystem = phoneNumber
        save()
    }

    fun isPhoneNumberHintShowed(): Boolean {
        cached?.let {
            return it.phoneNumberFromSystem != null
        } ?:run {
            return currentT()?.phoneNumberFromSystem != null
        }
    }

    fun getPhoneNumberFromSystem(): String? {
        cached?.let {
            return it.phoneNumberFromSystem
        } ?:run {
            return currentT()?.phoneNumberFromSystem
        }
    }

    fun setToken(token: String) {
        cached?.registrationToken = token
        save()
    }

    fun getToken(): String? {
        cached?.let {
            return it.registrationToken
        } ?:run {
            return currentT()?.registrationToken
        }
    }

    fun setTelegramUser(value: RegistrationInfo) {
        cached?.telegramUser = value
        save()
    }

    fun getTelegramUser(): RegistrationInfo? {
        cached?.let {
            return it.telegramUser
        } ?: run {
            return currentT()?.telegramUser
        }
    }

    fun setCountryMCC(value: Int) {
        cached?.countryMCC = value
        save()
    }

    fun getCountryMCC(): Int {
        cached?.let {
            return it.countryMCC
        } ?: run {
            return  currentT()?.countryMCC ?: 0
        }
    }

}