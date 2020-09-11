package com.library.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.library.Event
import kotlinx.coroutines.*
import java.io.IOException


const val POP_BACK = -1

abstract class BaseViewModel: ViewModel(), Observable {

    private val fragmentNavDirectionMutable = SingleLiveEvent<NavDirections>()
    val fragmentNavDirection: LiveData<NavDirections> = fragmentNavDirectionMutable

    data class PopBackTo(val id: Int, val inclusive: Boolean)
    private val popBackToMutable = SingleLiveEvent<PopBackTo>()
    val popBackTo: LiveData<PopBackTo> = popBackToMutable

    @Transient
    private val showToastMutable = SingleLiveEvent<String>()

    @Transient
    val showToast: LiveData<String> = showToastMutable
    val progress = ObservableBoolean(false)

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

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

    fun startBgJob(scope: CoroutineScope = viewModelScope,
                   block: suspend CoroutineScope.() -> Unit): Job {
        progress.set(true)
        return scope.launch(context = Dispatchers.IO, block = {
            block.invoke(this)
            progress.set(false)
        })
    }

    suspend fun <T> runMain(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.Main, block)

    fun toast(msg: String) {
        showToastMutable.postEvent(msg)
    }

    open fun navigate(nav: NavDirections) {
        fragmentNavDirectionMutable.postValue(nav)
    }

    //we can send several events
    fun <T> MutableLiveData<T>?.postEvent(value: T) {
        if (isMainThread()) {
            this?.value = value
        } else {
            viewModelScope.launch(Dispatchers.Main) {
                this@postEvent?.value = value
            }
        }
    }

    open fun popTo(id: Int, inclusive: Boolean = false) {
        popBackToMutable.postValue(PopBackTo(id, inclusive))
    }

    fun popBack() {
        popBackToMutable.postValue(PopBackTo(POP_BACK, false))
    }

    companion object {
        val syncMyAppsMutable = SingleLiveEvent<Event<Unit>>()
        val syncMyApps: LiveData<Event<Unit>> = syncMyAppsMutable
    }

    infix fun CoroutineScope.onError(function: (Throwable) -> Unit): CoroutineScope =
        this + CoroutineExceptionHandler { _, e -> function(e) }

    suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 30000,    // 30 second
        factor: Double = 2.0,
        block: suspend () -> T): T
    {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: IOException) {
                // you can log an error here and/or make a more finer-grained
                // analysis of the cause to see if retry is needed
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }

}