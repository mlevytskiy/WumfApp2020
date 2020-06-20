package com.app.api.api

import okhttp3.Headers.Companion.toHeaders
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

private const val AUTH_HEADER_KEY = "Authorization"
private const val BEARER = "Bearer "

class HeaderInterceptor(private val headers: HashMap<String, String>) : Interceptor {

    private val lock: ReadWriteLock = ReentrantReadWriteLock()

    override fun intercept(chain: Interceptor.Chain): Response {
        lock.synchronizeRead {
            val allHeaders = chain.request().headers.newBuilder().addAll(headers.toHeaders()).build()
            return chain.proceed(chain.request().newBuilder().headers(allHeaders).build())
        }
    }

    fun updateToken(bearerToken: String) {
        lock.synchronizeWrite {
            headers[AUTH_HEADER_KEY] = BEARER + bearerToken
        }
    }

    fun removeToken() {
        lock.synchronizeWrite {
            headers.remove(AUTH_HEADER_KEY)
        }
    }

    fun hasToken() {
        lock.synchronizeRead {
            headers[AUTH_HEADER_KEY]?.isNotBlank() ?: false
        }
    }
}