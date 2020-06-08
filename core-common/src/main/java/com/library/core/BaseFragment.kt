package com.library.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.library.core.di.unsyncLazy

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>(private val uiRes: Int) : Fragment() {

    protected abstract val viewModel: VM

    private lateinit var binding: B

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding = DataBindingUtil.inflate(inflater, uiRes, container, false)
        setViewModelInBinding(binding, viewModel)
        return binding.root
    }

}