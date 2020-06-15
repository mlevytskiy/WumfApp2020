package com.library.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.library.core.di.unsyncLazy
import java.lang.ref.WeakReference

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val uiRes: Int, private val navRes: Int) : AppCompatActivity() {

    protected abstract val viewModel: VM

    private lateinit var binding: B

    protected val navController by unsyncLazy { Navigation.findNavController(this, navRes) }

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

}