package com.library.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.library.core.di.unsyncLazy

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val uiRes: Int, private val navRes: Int) : AppCompatActivity() {

    protected abstract val viewModel: VM

    private lateinit var binding: B

    protected val navController by unsyncLazy { Navigation.findNavController(this, navRes) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, uiRes)
        setViewModelInBinding(binding, viewModel)
    }

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

}