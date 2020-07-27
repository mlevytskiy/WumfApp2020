package com.library.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import java.lang.ref.WeakReference

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val uiRes: Int, private val navRes: Int) : AppCompatActivity() {

    protected abstract val viewModel: VM

    protected lateinit var binding: B

    val navController by unsyncLazy {
        val controller = Navigation.findNavController(this, navRes)
        controller
    }

    var onActivityResultHandler: WeakReference<OnActivityResultHandler>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, uiRes)
        setViewModelInBinding(binding, viewModel)
    }

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        onActivityResultHandler?.let {
            onActivityResultHandler?.get()?.onActivityResult(requestCode, resultCode, data)
            onActivityResultHandler = null
        } ?: run {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun <T> observeEvent(liveData: LiveData<T>, onUnhandledEvent: (T) -> Unit) {
        liveData.observe(this, Observer {
            onUnhandledEvent(it)
        })
    }

}