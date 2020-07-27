package com.core.wumfapp2020.memory

import androidx.annotation.Keep
import com.core.wumfapp2020.memory.impl.HomeAppsConverter
import com.core.wumfapp2020.memory.impl.HomeAppsSource
import com.core.wumfapp2020.memory.impl.HomeAppsSourceConverter
import com.core.wumfapp2020.memory.impl.TYPE_IN_THE_WORLD
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

class App(@Keep val packageName: String, @Keep val whoLikes: List<Int>)

@Entity
class HomeState {

    @Id(assignable = true)
    var id: Long = ID

    @Convert(converter = HomeAppsSourceConverter::class, dbType = String::class)
    var appsSource: HomeAppsSource = HomeAppsSource(TYPE_IN_THE_WORLD, 0, "")

    @Convert(converter = HomeAppsConverter::class, dbType = String::class)
    var apps: MutableList<App> = ArrayList()

    constructor()

    companion object {
        @JvmField val ID = 3L
    }
}