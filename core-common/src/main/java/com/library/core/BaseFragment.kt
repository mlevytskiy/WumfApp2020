package com.library.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.library.Event

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>(private val uiRes: Int) : Fragment() {

    protected abstract val viewModel: VM
    protected lateinit var binding: B
    protected val navController by unsyncLazy { findNavController() }

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding = DataBindingUtil.inflate(inflater, uiRes, container, false)
        binding.lifecycleOwner = this
        setViewModelInBinding(binding, viewModel)

        Log.i("testr", "onCreateView ${this.javaClass}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState(viewModel.showToast) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
        observeState(viewModel.fragmentNavDirection) { direction ->
            navController.currentDestination?.let {
              it.getAction(direction.actionId)?.let {
                  navController.navigate(direction, getNavOptions(it.navOptions))
              }
            }
        }
        observeState(viewModel.popBackTo) {
            if (it.id == POP_BACK) {
                navController.popBackStack()
            }
        }
        onVisible()
    }

    open fun onVisible() { }

    open fun getNavOptions(navOptionsByDirection: NavOptions?): NavOptions? {
        return navOptionsByDirection
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
    }

    fun <T> observeState(liveData: LiveData<T>, onUnhandledState: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer {
            onUnhandledState(it)
        })
    }

    fun <T> observeEvent(liveData: LiveData<Event<T>>, onUnhandledEvent: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer {event->
            if (event?.handled == false) {
                event.getContent()?.let(onUnhandledEvent)
            }
        })
    }

}