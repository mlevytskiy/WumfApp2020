package com.library.telegramkotlinapi.handler

import com.library.telegramkotlinapi.TelegramUser
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class GetCurrentUserResponseHandler(private var successHandle: (TelegramUser)->Unit, private var errorHandle: (Int)->Unit): Client.ResultHandler {

    override fun onResult(obj: TdApi.Object?) {
        when (obj?.constructor) {
            TdApi.Error.CONSTRUCTOR -> {
                val errorObj = obj as TdApi.Error
                errorHandle(errorObj.code)
            }
            TdApi.User.CONSTRUCTOR -> {
                val currUser = obj as TdApi.User
//                currUser.profilePhoto.
                successHandle(TelegramUser(currUser.id, currUser.firstName))
            }
        }
    }

}