package com.library.telegramkotlinapi.handler

import android.util.Log
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class AuthorizationResponseHandler(private val successHandle: ()->Unit, private val errorHandle: (Int)->Unit,
                                   private val tooManyRequestsErrorHandler: ()->Unit): Client.ResultHandler {

    private val IGNORED_ERROR_CODE = 406
    private val TOO_MANY_REQUESTS = 429

    override fun onResult(obj: TdApi.Object?) {
        when (obj?.constructor) {
            TdApi.Error.CONSTRUCTOR -> {
                val errorObj = obj as TdApi.Error
                when(errorObj.code) {
                    IGNORED_ERROR_CODE -> { }
                    TOO_MANY_REQUESTS -> {
                        tooManyRequestsErrorHandler()
                    }
                    else -> {
                        errorHandle(obj.code)
                    }

                }
            }
            TdApi.Ok.CONSTRUCTOR -> {
                successHandle()
            }
            else -> { Log.i("test", "Receive wrong response from TDLib: $obj") }
        }
    }

}