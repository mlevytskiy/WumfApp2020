package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.BaseRepository
import io.objectbox.BoxStore
import io.objectbox.kotlin.query

class MyAppsCollectionRepository(boxStore: BoxStore): BaseRepository<MyAppsCollection>(boxStore, MyAppsCollection::class.java) {

    fun getMyApps():List<String> {
        cached?.let {
            return it.apps
        } ?:run {
            return currentT()?.apps ?: emptyList()
        }
    }

    fun shouldWeStartDeferredTask(): Boolean {
        return (cached!!.deferredTaskAddApps.isNotEmpty() || cached!!.deferredTaskRemoveApps.isNotEmpty())
    }

    fun getDeferredTaskAddApps(): List<String> {
        cached?.let {
            return it.deferredTaskAddApps
        } ?:run {
            return currentT()?.deferredTaskAddApps ?: emptyList()
        }
    }

    fun getDeferredTaskRemoveApps(): List<String> {
        cached?.let {
            return it.deferredTaskRemoveApps
        } ?:run {
            return currentT()?.deferredTaskRemoveApps ?: emptyList()
        }
    }

    fun clearDeferredTaskAddApps() {
        cached?.deferredTaskAddApps?.clear()
        save()
    }

    fun clearDeferredTaskRemoveApps() {
        cached?.deferredTaskRemoveApps?.clear()
        save()
    }

    fun replaceAppsInMemory(apps: List<String>) {
        cached?.apps?.let {
            if (it.isEmpty()) {
                it.clear()
                it.addAll(apps)
                save()
            }
        }
    }

    fun addToMyApps(pkgName: String): Boolean {
        var isAdded = false
        if (cached?.apps?.contains(pkgName) == false) {
            isAdded = cached?.apps?.add(pkgName) ?: false

            if (cached?.deferredTaskRemoveApps?.contains(pkgName) == true) {
                cached?.deferredTaskRemoveApps?.remove(pkgName)
            }
            if (cached?.deferredTaskAddApps?.contains(pkgName) == false) {
                cached?.deferredTaskAddApps?.add(pkgName)
            }
        }
        if (isAdded) {
            save()
        }
        return isAdded
    }

    fun removeFromMyApps(pkgName: String): Boolean {
        var isRemoved = cached?.apps?.remove(pkgName) ?: false
        if (isRemoved) {
            if (cached?.deferredTaskAddApps?.contains(pkgName) == true) {
                cached?.deferredTaskAddApps?.remove(pkgName)
            } else if (cached?.deferredTaskRemoveApps?.contains(pkgName) == false) {
                cached?.deferredTaskRemoveApps?.add(pkgName)
            }
            save()
        }
        return isRemoved
    }

    override fun currentT(): MyAppsCollection? {
        val data = box.query {
            equal(MyAppsCollection_.id, MyAppsCollection.ID)
        }.findFirst()
        return data
    }

    override fun initCache() {
        cached = currentT() ?:run {
            MyAppsCollection()
        }
    }

    override fun isEmpty(): Boolean {
        return super.isEmpty() || (cached?.apps?.isEmpty() ?: false)
    }
    
}