package com.core.wumfapp2020.memory

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

    fun addToMyApps(pkgName: String): Boolean {
        var isAdded = false
        if (cached?.apps?.contains(pkgName) == false) {
            isAdded = cached?.apps?.add(pkgName) ?: false
        }
        if (isAdded) {
            save()
        }
        return isAdded
    }

    fun removeFromMyApps(pkgName: String): Boolean {
        var isRemoved = cached?.apps?.remove(pkgName) ?: false
        if (isRemoved) {
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