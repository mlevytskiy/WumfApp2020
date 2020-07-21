package com.core.wumfapp2020.base.dialogViewModels

import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import wumf.com.appsprovider2.AppContainer

open class AppViewModel @AssistedInject constructor(@Assisted val app: AppContainer, @Assisted private val showPeopleWhoLikes: (()->Unit)?,
                                               @Assisted private val showInGooglePlay: (()->Unit), @Assisted val peopleWhoLikeCount: Int,
                                               private val stringRes: StringRes, private val colorRes: ColorRes,
                                               private val internetConnection: InternetConnectionChecker): BaseViewModel() {

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

    fun onClickShowInGooglePlay() {
        showInGooglePlay()
    }

    fun onClickShowPeopleWhoLikes() {
        showPeopleWhoLikes?.let {
            it()
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(app: AppContainer, showPeopleWhoLikes: (()->Unit)?=null, showInGooglePlay: (()->Unit),
                   peopleWhoLikeCount: Int): AppViewModel
    }

}