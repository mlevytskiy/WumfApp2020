package com.library.core

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline val <T> T.exhaustive get() = this

fun <T> unsyncLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

inline fun CoroutineScope.launchCatching(
  noinline block: suspend CoroutineScope.() -> Unit,
  crossinline onFailure: (Throwable) -> Unit
) {
  launch(CoroutineExceptionHandler { _, throwable -> onFailure(throwable) }, block = block)
}

inline fun <reified T : ViewModel> FragmentActivity.lazyViewModel(
  crossinline viewModelProducer: () -> T
) = viewModels<T> {
  object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModelProducer() as T
  }
}

inline fun <reified T : ViewModel> FragmentActivity.lazySavedStateViewModel(
  crossinline viewModelProducer: (handle: SavedStateHandle) -> T
) = viewModels<T> {
  object : AbstractSavedStateViewModelFactory(this, intent.extras) {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
      viewModelProducer(handle) as T
  }
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
  crossinline viewModelProducer: () -> T
) = viewModels<T> {
  object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModelProducer() as T
  }
}

inline fun <reified T : ViewModel> Fragment.lazySavedStateViewModel(
  crossinline viewModelProducer: (handle: SavedStateHandle) -> T
) = viewModels<T> {
  object : AbstractSavedStateViewModelFactory(this, arguments ?: Bundle.EMPTY) {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
      viewModelProducer(handle) as T
  }
}

inline fun <reified T : ViewModel> Fragment.lazyActivityViewModel(
  crossinline viewModelProducer: () -> T
) = activityViewModels<T> {
  object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModelProducer() as T
  }
}

inline fun <reified T : ViewModel> Fragment.lazySavedStateActivityViewModel(
  crossinline viewModelProducer: (handle: SavedStateHandle) -> T
) = activityViewModels<T> {
  object : AbstractSavedStateViewModelFactory(this, requireArguments()) {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
      viewModelProducer(handle) as T
  }
}

inline fun isMainThread(): Boolean = Looper.getMainLooper().thread == Thread.currentThread()

inline fun <reified T> SavedStateHandle.delegate(key: String? = null): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
  override fun getValue(thisRef: Any, property: KProperty<*>): T? {
    val stateKey = key ?: property.name
    return this@delegate[stateKey]
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
    val stateKey = key ?: property.name
    this@delegate[stateKey] = value
  }
}

fun String.log() {
  Log.i("testr", this)
}

inline fun View?.showKeyboard() = this?.run {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  if (this.hasFocus()) {
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
  } else {
    this.rootView.findFocus()?.let { imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT) }
  }
}

inline fun View?.focusAndShowKeyboard() = this?.run {
  requestFocusFromTouch()
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

inline fun View?.hideKeyboard() = this?.run {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, 0)
}

inline fun Activity?.showKeyboard() = this?.run {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  if (!imm.isAcceptingText) {
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
  }
}

inline fun Activity?.hideKeyboard() = this?.currentFocus?.hideKeyboard()

inline fun Fragment?.showKeyboard() = this?.run {
  val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  if (!imm.isAcceptingText) {
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
  }
}

inline fun Fragment?.hideKeyboard() = this?.activity?.currentFocus?.hideKeyboard()
