package com.library.core

import androidx.lifecycle.ViewModel
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.library.core.di.isMainThread
import kotlinx.coroutines.*

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

    @Transient
    private var callbacks: PropertyChangeRegistry? = null

    @Transient
    private val handler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    @Transient
    val scope = viewModelScope.plus(handler)

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
        return scope.launch(block = {
                block.invoke(this)
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
            scope.launch(Dispatchers.Main) {
                this@postEvent?.value = value
            }
        }
    }

    open fun popTo(id: Int, inclusive: Boolean = false) {
        popBackToMutable.postValue(PopBackTo(id, inclusive))
    }

}