package com.core.wumfapp2020.base.dialogViewModels

import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import wumf.com.appsprovider2.AppContainer

class AppInMyCollectionViewModel @AssistedInject constructor(@Assisted app: AppContainer,
                                                             @Assisted showInGooglePlay: (()->Unit),
                                                             @Assisted private val removeFromMyCollection: (()->Unit),
                                                             stringRes: StringRes, colorRes: ColorRes,
                                                             internetConnection: InternetConnectionChecker):
    AppViewModel(app, null, showInGooglePlay, 0, stringRes, colorRes, internetConnection) {

    fun onClickRemoveFromMyCollection() {
        removeFromMyCollection()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(app: AppContainer, showInGooglePlay: (()->Unit), removeFromMyCollection: (()->Unit)): AppInMyCollectionViewModel
    }

}