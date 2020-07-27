package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.MyAppsConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class MyAppsCollection {

    @Id(assignable = true)
    var id: Long = ID

    @Convert(converter = MyAppsConverter::class, dbType = String::class)
    var apps: MutableList<String> = ArrayList()

    @Convert(converter = MyAppsConverter::class, dbType = String::class)
    var deferredTaskAddApps: MutableList<String> = ArrayList()

    @Convert(converter = MyAppsConverter::class, dbType = String::class)
    var deferredTaskRemoveApps: MutableList<String> = ArrayList()

    constructor()

    companion object {
        @JvmField val ID = 2L
    }
}