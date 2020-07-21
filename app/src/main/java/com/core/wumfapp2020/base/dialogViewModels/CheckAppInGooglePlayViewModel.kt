package com.core.wumfapp2020.base.dialogViewModels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.core.wumfapp2020.InternetConnectionChecker
import com.core.wumfapp2020.R
import com.core.wumfapp2020.base.ColorRes
import com.core.wumfapp2020.base.StringRes
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.delay
import wumf.com.appsprovider2.AppContainer
import wumf.com.appsprovider2.GooglePlayAppsProvider

private const val SHOW_SUCCESS_MESSAGE_SECONDS = 2

class CheckAppInGooglePlayViewModel @AssistedInject constructor(@Assisted val app: AppContainer, @Assisted val addPkgInMemory: (()->Unit),
                                                                @Assisted val moveToAddToMyCollectionScreen: (()->Unit),
                                                                @Assisted val dismissDialog: (()->Unit),
                                                                private val stringRes: StringRes, private val colorRes: ColorRes,
                                                                private val internetConnection: InternetConnectionChecker): BaseViewModel() {

    val textColor = ObservableField<Int>(colorRes.getColor(R.color.black))
    val title = ObservableField<String>()
    val message = ObservableField<String>()
    val showRecheckButton = ObservableBoolean(true)

    fun check() {
        progress.set(true)
        startBgJob {
            if (internetConnection.hasInternetConnection()) {
                val app = GooglePlayAppsProvider.fillAppFromGooglePlay(app.packageName)
                val exist = (app.name != null)
                if (exist) {
                    addPkgInMemory()
                    moveToAddToMyCollectionScreen()
                    title.set(stringRes.getStr(R.string.title_app_added_to_your_collection))
                    message.set(stringRes.getStrFormat(R.string.message_app_added_to_your_collection, app.name?:""))
                    textColor.set(colorRes.getColor(R.color.green))
                    showRecheckButton.set(false)
                    delay(1000)
                    dismissDialog()
                } else {
                    title.set(stringRes.getStr(R.string.title_we_cant_adding_app_to_your_collection))
                    message.set(stringRes.getStrFormat(R.string.message_we_cant_adding_app_to_your_collection, app.name?:""))
                    textColor.set(colorRes.getColor(R.color.black))
                }
            } else {
                title.set("")
                message.set(stringRes.getStr(R.string.no_internet_connection))
                textColor.set(colorRes.getColor(R.color.red))
            }
            progress.set(false)
        }
    }

    fun recheck() {
        check()
    }

    private fun addPkgToMyCollection() {

    }

    private fun proceedToMyCollectionScreen() {

    }

    override fun handleException(e: Throwable) {
        TODO("Not yet implemented")
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(app: AppContainer, addPkgInMemory: (()->Unit),
                   moveToAddToMyCollectionScreen: (()->Unit),
                   dismissDialog: (()->Unit)): CheckAppInGooglePlayViewModel
    }

    public override fun onCleared() {
        Log.i("testr", "onCleared()")
        super.onCleared()
    }
}