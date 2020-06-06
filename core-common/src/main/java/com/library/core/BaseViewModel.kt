package com.library.core

import androidx.lifecycle.ViewModel
import androidx.databinding.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class BaseViewModel: ViewModel(), Observable {

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

    @Transient
    private val handler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                callbacks = PropertyChangeRegistry()
            }
        }
        callbacks?.add(callback)
    }

    abstract fun handleException(e: Throwable)

    fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.plus(handler).launch(block = {
                block.invoke(this)
        })
    }

    suspend fun <T> runMain(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.Main, block)

}