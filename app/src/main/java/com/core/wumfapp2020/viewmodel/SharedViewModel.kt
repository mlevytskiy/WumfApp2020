package com.core.wumfapp2020.viewmodel

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedViewModel @Inject constructor() {
    var status: ResultStatus? = null
    var data: Any? = null
}

enum class ResultStatus {
    SUCCESS, CANCEL, ERROR
}