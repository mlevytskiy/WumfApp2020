package com.library.telegramkotlinapi

import com.library.telegramkotlinapi.TelegramApi.*
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FakeTelegramApi(private val client: Client): TelegramApi {

    override suspend fun authWithPhone(phone: String): AuthWithPhoneResult = suspendCoroutine { cont ->
        cont.resume(AuthWithPhoneResult.SUCCESS)
    }

    override suspend fun checkVerificationCode(code: String): TrueOrError = suspendCoroutine { cont ->
        cont.resume(TrueOrError(true))
    }

    override suspend fun getUserInfo(): TelegramUser? = suspendCoroutine { cont ->
        val telegramUser = TelegramUser(33333, "Vova", null)
        cont.resume(telegramUser)
    }

    override suspend fun getContacts(): TelegramUsers = suspendCoroutine { cont ->
        val users = TdApi.Users(3, intArrayOf(11111, 22222, 444444))
        cont.resume(TelegramUsers(users))
    }

}