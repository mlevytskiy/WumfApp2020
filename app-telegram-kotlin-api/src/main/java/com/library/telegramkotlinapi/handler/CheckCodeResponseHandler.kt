package com.library.telegramkotlinapi.handler

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class CheckCodeResponseHandler(private var successHandle: ()->Unit, private var errorHandle: (Int)->Unit): Client.ResultHandler {

    override fun onResult(obj: TdApi.Object?) {
        when (obj?.constructor) {
            TdApi.Error.CONSTRUCTOR -> {
                val errorObj = obj as TdApi.Error
                errorHandle(errorObj.code)
            }
            TdApi.Ok.CONSTRUCTOR -> {
                successHandle()
            }
        }
    }

}