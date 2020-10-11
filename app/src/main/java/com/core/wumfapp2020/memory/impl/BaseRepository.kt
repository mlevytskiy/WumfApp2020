package com.core.wumfapp2020.memory.impl

import com.core.wumfapp2020.memory.FriendsCollection
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.*
import java.lang.Exception

abstract class BaseRepository<T>(boxStore: BoxStore, classT: Class<T>) {

    protected val box: Box<T>
    private val supervisor = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

    @Volatile
    protected var cached: T? = null

    init {
        box = boxStore.boxFor(classT)
    }

    fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                //ignore
            }
        })
    }

    open fun isEmpty() = box.isEmpty

    fun save() {
        startBgJob {
            cached?.let {
                box.put(it)
            }
        }
    }

    fun clear() {
        box.removeAll()
        cached = null
    }

    protected abstract fun currentT(): T?

    fun current(): T? {
        cached?.let {
            return cached
        }
        return currentT()
    }

    abstract fun initCache()

}