package com.app.api.api

import kotlinx.coroutines.CopyableThrowable

class RetrofitException(private val requestMessage: String): Exception(requestMessage),
    CopyableThrowable<RetrofitException> {
    override fun createCopy(): RetrofitException {
        return  RetrofitException(requestMessage)
    }
}