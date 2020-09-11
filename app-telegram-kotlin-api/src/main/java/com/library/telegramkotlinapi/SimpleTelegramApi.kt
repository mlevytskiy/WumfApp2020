package com.library.telegramkotlinapi

import com.library.telegramkotlinapi.TelegramApi.*
import com.library.telegramkotlinapi.handler.AuthorizationResponseHandler
import com.library.telegramkotlinapi.handler.CheckCodeResponseHandler
import com.library.telegramkotlinapi.handler.GetContactsResponseHandler
import com.library.telegramkotlinapi.handler.GetCurrentUserResponseHandler
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SimpleTelegramApi(private val client: Client): TelegramApi {

    override suspend fun authWithPhone(phone: String): AuthWithPhoneResult = suspendCoroutine { cont ->
        val phoneNumber = phone.replace(" ", "")
        client.send(
            TdApi.SetAuthenticationPhoneNumber(phoneNumber, TdApi.PhoneNumberAuthenticationSettings(true, true, false)),
            AuthorizationResponseHandler(
                successHandle = { cont.resume(AuthWithPhoneResult.SUCCESS) },
                errorHandle = { code, msg ->
                    run {
                        AuthWithPhoneResult.ERROR.errorMsg = msg
                        AuthWithPhoneResult.ERROR.errorCode = code
                        cont.resume(AuthWithPhoneResult.ERROR)
                    }
                },
                tooManyRequestsErrorHandler = { code, msg ->
                    run {
                        AuthWithPhoneResult.ERROR.errorMsg = msg
                        AuthWithPhoneResult.ERROR.errorCode = code
                        cont.resume(AuthWithPhoneResult.ERROR_TOO_MANY_REQUESTS)
                    }
                })
        )
    }

    override suspend fun checkVerificationCode(code: String): TrueOrError = suspendCoroutine { cont ->
        client.send(TdApi.CheckAuthenticationCode(code), CheckCodeResponseHandler(
            successHandle = { cont.resume(TrueOrError(value = true)) },
            errorHandle = { errorCode, message -> cont.resume(TrueOrError(value = false, errorCode = errorCode, errorMessage = message)) }))
    }

    override suspend fun getUserInfo(): TelegramUser? = suspendCoroutine { cont ->
        client.send(TdApi.GetMe(), GetCurrentUserResponseHandler(
            successHandle = { telegramUser -> cont.resume(telegramUser) },
            errorHandle = { cont.resume(null) }))
    }

    override suspend fun getContacts(): TelegramUsers = suspendCoroutine { cont ->
        try {
            client.send(
                TdApi.GetContacts(), GetContactsResponseHandler(
                    successHandle = { users -> cont.resume(TelegramUsers(users)) },
                    errorHandle = {code,msg-> run {
                        cont.resume(TelegramUsers(users = null, errorCode = code, errorMessage = msg))
                    } })
            )
        } catch (e: Exception) {
            cont.resume(TelegramUsers(users = null, exc = e))
        }
    }

}