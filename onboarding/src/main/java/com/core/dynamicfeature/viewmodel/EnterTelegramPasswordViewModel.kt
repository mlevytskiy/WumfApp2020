package com.core.dynamicfeature.viewmodel

import androidx.databinding.ObservableField
import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import com.core.wumfapp2020.viewmodel.ResultStatus
import com.core.wumfapp2020.viewmodel.SharedViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import krafts.alex.tg.TgClient

class EnterTelegramPasswordViewModel @AssistedInject constructor(private val sharedViewModel: SharedViewModel, @Assisted private val hint: String): AnyFragmentBaseViewModel()  {

    val password = ObservableField<String>()

    init {
        sharedViewModel.status = ResultStatus.CANCEL
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(hint: String): EnterTelegramPasswordViewModel
    }

    fun onClickContinue() {
        password.get()?.let {
            asyncCall {
                sharedViewModel.status = ResultStatus.SUCCESS
                sharedViewModel.data = password.get()
                popBack()
            }
        }
    }
}