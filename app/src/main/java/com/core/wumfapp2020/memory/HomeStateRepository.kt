package com.core.wumfapp2020.memory

import com.core.wumfapp2020.memory.impl.BaseRepository
import io.objectbox.BoxStore
import io.objectbox.kotlin.query

class HomeStateRepository(boxStore: BoxStore): BaseRepository<HomeState>(boxStore, HomeState::class.java) {

    override fun initCache() {
        cached = currentT() ?:run {
            HomeState()
        }
    }

    override fun currentT(): HomeState? {
        val homeState = box.query {
            equal(HomeState_.id, HomeState.ID)
        }.findFirst()
        return homeState
    }

    fun update(homeState: HomeState) {
        cached = homeState
        save()
    }

}