package com.core.wumfapp2020

import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.io.Serializable

class StringResult: Serializable {
    val result = SingleLiveEvent<String?>()
}

@UiThread
fun <LO : LifecycleOwner> StringResult.observe(owner: LO, observer: Observer<String?>): StringResult {
    result.observe(owner, observer)
    return this
}
 //Simple solution. It stops work after deserialization of course. But I use it because it is simple.
class ResultListener: Serializable {
    val status = SingleLiveEvent<ResultStatus>()
}

enum class ResultStatus {
    SUCCESS, CANCEL, ERROR
}

@UiThread
fun <LO : LifecycleOwner> ResultListener.observe(owner: LO, observer: Observer<ResultStatus>): ResultListener {
    status.observe(owner, observer)
    return this
}

class ChangeListener {
    val changed = SingleLiveEvent<Unit>()
}