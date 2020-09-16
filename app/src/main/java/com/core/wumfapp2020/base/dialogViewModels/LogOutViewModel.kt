package com.core.wumfapp2020.base.dialogViewModels

import com.core.wumfapp2020.viewmodel.AnyFragmentBaseViewModel
import com.squareup.inject.assisted.AssistedInject

class LogOutViewModel @AssistedInject constructor(): AnyFragmentBaseViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(): LogOutViewModel
    }

}