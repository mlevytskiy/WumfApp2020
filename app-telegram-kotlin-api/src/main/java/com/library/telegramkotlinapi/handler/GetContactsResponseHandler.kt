package com.library.telegramkotlinapi.handler

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Users

class GetContactsResponseHandler(private var successHandle: (Users)->Unit, private var errorHandle: (Int)->Unit): Client.ResultHandler {

    override fun onResult(obj: TdApi.Object?) {
        when (obj?.constructor) {
            TdApi.Error.CONSTRUCTOR -> {
                val errorObj = obj as TdApi.Error
                errorHandle(errorObj.code)
            }
            TdApi.User.CONSTRUCTOR -> {
                val users = obj as Users
                successHandle(users)
            }
        }
    }

}