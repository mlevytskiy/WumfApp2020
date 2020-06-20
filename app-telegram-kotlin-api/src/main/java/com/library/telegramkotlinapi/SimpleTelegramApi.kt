package com.library.telegramkotlinapi

import com.library.telegramkotlinapi.handler.AuthorizationResponseHandler
import com.library.telegramkotlinapi.handler.CheckCodeResponseHandler
import com.library.telegramkotlinapi.handler.GetContactsResponseHandler
import com.library.telegramkotlinapi.handler.GetCurrentUserResponseHandler
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SimpleTelegramApi {

    private var client: Client? = null

    fun client(client: Client): SimpleTelegramApi {
        this.client = client
        return this
    }

    suspend fun authWithPhone(phone: String): AuthWithPhoneResult = suspendCoroutine { cont ->
//        val phoneNumber = phone.replace(" ", "")
//        client?.send(
//            TdApi.SetAuthenticationPhoneNumber(phoneNumber, false, false),
//            AuthorizationResponseHandler(
//                { cont.resume(AuthWithPhoneResult.SUCCESS) },
//                { cont.resume(AuthWithPhoneResult.ERROR) },
//                { cont.resume(AuthWithPhoneResult.ERROR_TOO_MANY_REQUESTS) })
//        )
        cont.resume(AuthWithPhoneResult.SUCCESS)
    }

    suspend fun checkVerificationCode(code: String): Boolean = suspendCoroutine { cont ->
//        client?.send(TdApi.CheckAuthenticationCode(code, null, null),
//            CheckCodeResponseHandler({ cont.resume(true) }, { cont.resume(false) }))
        cont.resume(true)
    }

    suspend fun getUserInfo(): TelegramUser? = suspendCoroutine { cont ->
//        client?.send(TdApi.GetMe(), GetCurrentUserResponseHandler( { telegramUser -> cont.resume(telegramUser) }, { cont.resume(null) } ))
        val telegramUser = TelegramUser(33333, "Vova", null)
        cont.resume(telegramUser)
    }

    suspend fun getContacts(): TdApi.Users? = suspendCoroutine { cont ->
//        client?.send(TdApi.GetContacts(), GetContactsResponseHandler({ users -> cont.resume(users) }, { cont.resume(null) }))
        val users = TdApi.Users(3, intArrayOf(11111, 22222, 444444))
        cont.resume(users)
    }

    enum class AuthWithPhoneResult {
        SUCCESS,
        ERROR,
        ERROR_TOO_MANY_REQUESTS
    }

}