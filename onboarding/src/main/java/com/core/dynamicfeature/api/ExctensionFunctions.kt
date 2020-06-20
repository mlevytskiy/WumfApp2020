package com.app.api.api

import retrofit2.Call
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.concurrent.locks.ReadWriteLock

inline fun <R> ReadWriteLock.synchronizeRead(block: () -> R): R {
    this.readLock().lock()
    try {
        return block.invoke()
    } finally {
        this.readLock().unlock()
    }
}

inline fun <R> ReadWriteLock.synchronizeWrite(block: () -> R): R {
    this.writeLock().lock()
    try {
        return block.invoke()
    } finally {
        this.writeLock().unlock()
    }
}

fun String.getNullIfEmpty(): String? {
    if (this == "") return null
    else return this
}

fun <T> executeRetrofit(call: Call<T>,
                        socketTimeout: (SocketTimeoutException)->Unit = {executeRetrofit(call, {}, generalError)},
                        generalError: (Exception)->Unit = {}): T? {
    try {
        return executeRetrofit(call)
    } catch (ex: SocketTimeoutException) {
        socketTimeout(ex)
    } catch (e: Exception) {
        generalError(e)
    }
    return null
}

//todo: move me to interceptor
private fun <T> executeRetrofit(call: Call<T>): T? {
    val response = call.execute()
    if (response.isSuccessful) {
        return response.body()
    } else {
        throw RetrofitException("Status = ${response.code()} ${response.message()} body= ${response.errorBody()?.string()?:"is empty"}")
    }
}