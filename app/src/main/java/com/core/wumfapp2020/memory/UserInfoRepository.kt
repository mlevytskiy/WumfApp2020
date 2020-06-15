package com.core.wumfapp2020.memory

import android.content.Context
import android.util.Log
import io.objectbox.Box
import io.objectbox.kotlin.query
import kotlinx.coroutines.*
import java.lang.Exception

class UserInfoRepository(context: Context) {

    private val userInfoBox: Box<UserInfo>
    @Transient
    private val supervisor = SupervisorJob()
    @Transient
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

    @Volatile
    private var cachedUserInfo: UserInfo? = null

    init {
        val boxStore = MyObjectBox.builder().androidContext(context).buildDefault()
        userInfoBox = boxStore.boxFor(UserInfo::class.java)
    }

    fun initCache() {
        cachedUserInfo = currentUser() ?:run {
            UserInfo(statusBarHeight = -1, language=0, phoneNumberFromSystem=null)
        }
    }

    fun isEmpty() = userInfoBox.isEmpty

    fun getStatusBarHeight(): Int {
        cachedUserInfo?.let {
            return it.statusBarHeight
        } ?:run {
            return currentUser()?.statusBarHeight ?: 0
        }
    }

    fun setStatusBarHeight(height: Int) {
        cachedUserInfo?.statusBarHeight = height
        save()
    }

    fun getLanguage(): Byte {
        cachedUserInfo?.let {
            return it.language
        } ?:run {
            return currentUser()?.language ?: -1
        }
    }

    fun setLanguage(language: Byte) {
        cachedUserInfo?.language = language
        save()
    }

    fun setPhoneNumberFromSystem(phoneNumber: String) {
        cachedUserInfo?.phoneNumberFromSystem = phoneNumber
        save()
    }

    fun isPhoneNumberHintShowed(): Boolean {
        cachedUserInfo?.let {
            return it.phoneNumberFromSystem != null
        } ?:run {
            return currentUser()?.phoneNumberFromSystem != null
        }
    }

    fun getPhoneNumberFromSystem(): String? {
        cachedUserInfo?.let {
            return it.phoneNumberFromSystem
        } ?:run {
            return currentUser()?.phoneNumberFromSystem
        }
    }

    fun setToken(token: String) {
        cachedUserInfo?.registrationToken = token
        save()
    }

    fun getToken(): String? {
        cachedUserInfo?.let {
            return it.registrationToken
        } ?:run {
            return currentUser()?.registrationToken
        }
    }

    fun setTelegramUser(value: RegistrationInfo) {
        cachedUserInfo?.telegramUser = value
        save()
    }

    fun getTelegramUser(): RegistrationInfo? {
        cachedUserInfo?.let {
            return it.telegramUser
        } ?: run {
            return currentUser()?.telegramUser
        }
    }

    private fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                //ignore
            }
        })
    }

    private fun save() {
            startBgJob {
            cachedUserInfo?.let {
                userInfoBox.put(it)
            }
        }
    }

    private fun currentUser(): UserInfo? {
        val userInfo = userInfoBox.query {
            equal(UserInfo_.id, UserInfo.ID)
        }.findFirst()
        Log.i("testr", "userInfo=" + userInfo)
        return userInfo
    }


}