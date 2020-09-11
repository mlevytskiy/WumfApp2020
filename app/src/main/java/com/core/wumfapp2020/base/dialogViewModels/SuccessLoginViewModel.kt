package com.core.wumfapp2020.base.dialogViewModels

import androidx.databinding.ObservableField
import com.library.core.BaseViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.delay
import org.drinkless.td.libcore.telegram.TdApi

class SuccessLoginViewModel @AssistedInject constructor(@Assisted val image: TdApi.File?,
                                                        @Assisted val name: String, @Assisted val contactsAmount: Int,
                                                        @Assisted val dismissDialog: ()->Unit): BaseViewModel() {

    val photoFilePath: ObservableField<String> = ObservableField("")

    init {
        progress.set(true)
        startBgJob {
            for (i in 1..30) {
                delay(100)
                if (photoFilePath.get().isNullOrEmpty() && (image?.local?.isDownloadingCompleted == true)) {
                    photoFilePath.set(image.local.path)
                }
            }
            dismissDialog()
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(image: TdApi.File?, name: String, contactsAmount: Int, dismissDialog: ()->Unit): SuccessLoginViewModel
    }

}