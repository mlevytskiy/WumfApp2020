package com.library.core

import androidx.lifecycle.ViewModel
import androidx.databinding.*
import androidx.navigation.NavDirections
import com.library.core.event.BaseEvent
import com.library.core.event.FragmentNavigationDirection
import com.library.core.event.PopBackTo
import com.library.core.event.ShowToastEvent
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference

abstract class BaseViewModel<B : ViewDataBinding>: ViewModel(), Observable, HoldUI<B> {

    @Transient
    private var callbacks: PropertyChangeRegistry? = null
    @Transient
    private val supervisor = SupervisorJob()

    @Transient
    private val handler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    @Transient
    protected var scope = CoroutineScope(Dispatchers.IO + supervisor + handler)

    private var weakReferenceUI: WeakReference<B>? = null

    override fun setUI(binding: B?) {
        binding?.let {
            weakReferenceUI = WeakReference(it)
        }
    }

    override fun getUI(): WeakReference<B>? {
        return weakReferenceUI
    }

    override fun needHoldUI(): Boolean {
        return false
    }

    override fun onCleared() {
        super.onCleared()
        cancelBgJobs()
    }

    open fun shouldHoldUI(): Boolean {
        return false
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.remove(callback)
    }

    fun showToast(message: String) {
        postEvent(ShowToastEvent(message))
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

    private fun cancelBgJobs() {
        supervisor.cancelChildren()
    }

    fun navigate(nav: NavDirections) {
        postEvent(FragmentNavigationDirection(nav))
    }

    fun popTo(id: Int, inclusive: Boolean = false) {
        postEvent(PopBackTo(id, inclusive))
    }

    fun postEvent(event: BaseEvent) {
        EventBus.getDefault().post(event)
    }

    fun postStickyEvent(event: BaseEvent) {
        EventBus.getDefault().postSticky(event)
        startBgJob {
            delay(100)
            val catchedEvent = EventBus.getDefault().getStickyEvent(event.javaClass)
            if (catchedEvent != null) {
                EventBus.getDefault().removeStickyEvent(catchedEvent)
            }
        }
    }

    enum class ErrorHandlerMode {
        HANDLE, IGNORE, THROW
    }
}