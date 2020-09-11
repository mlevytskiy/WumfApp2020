package com.library.telegramkotlinapi

import org.drinkless.td.libcore.telegram.TdApi
import java.lang.Exception

interface TelegramApi {

    suspend fun authWithPhone(phone: String): AuthWithPhoneResult

    suspend fun checkVerificationCode(code: String): TrueOrError

    suspend fun getUserInfo(): TelegramUser?

    suspend fun getContacts(): TelegramUsers

    enum class AuthWithPhoneResult(var errorCode: Int = 0, var errorMsg: String = "") {
        SUCCESS,
        ERROR,
        ERROR_TOO_MANY_REQUESTS
    }

    class TrueOrError(val value: Boolean, val errorCode: Int = -1, val errorMessage: String = "")

    class TelegramUsers(val users: TdApi.Users?, val errorCode: Int = -1, val errorMessage: String = "", val exc: Exception? = null)

}