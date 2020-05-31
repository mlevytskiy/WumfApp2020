package com.library.core

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.library.core.event.HideBottomNavEvent
import com.library.core.event.ShowBottomNavEvent
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel<B>>: Fragment(),
    OnActivityResultHandler {

    @JvmField @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    var viewModel: VM? = null

    var binding: B? = null

    protected var navController: NavController? = null

    fun navigate(nav: NavDirections) {
        navController?.navigate(nav)
//        log("nav to: $nav")
//        postEvent(FragmentNavigationDirection(nav, this::class))
    }

    fun popTo(id: Int, inclusive: Boolean = false) {
//        log("pop to: $id")
//        postEvent(PopBackTo(id, inclusive, this::class))
    }

    override fun onResume() {
        super.onResume()
        registerEventBus()
        if (isShowBottomTabs()) {
            viewModel?.postEvent(ShowBottomNavEvent(true))
        } else {
            viewModel?.postEvent(HideBottomNavEvent(true))
        }
    }

    override fun onPause() {
        unregisterEventBus()
        super.onPause()
    }

    protected open fun getToolbar(): Toolbar? {
        return null
    }

    private fun isShowBottomTabs(): Boolean {
        return getToolbar() == null
    }

    private fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            try {
                EventBus.getDefault().register(this)
            } catch (exception : Throwable) {
                //ignore
            }
//            log("event bus registered for: " + this::class.simpleName)
        }
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
//        log("event bus unregistered for: " + this::class.simpleName)
    }

    abstract fun onInitVM()

    protected abstract fun getViewModelClass(): KClass<VM>

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory?.let {
            viewModel = ViewModelProvider(requireActivity(), it)[getViewModelClass().java]
        }
    }

    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding = viewModel?.getUI()?.get() ?: DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        if (viewModel?.needHoldUI() ?: false) {
            viewModel?.setUI(binding)
        }

        setViewModelInBinding(binding!!, viewModel!!)
        getToolbar()?.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
        }
        navController = findNavController()
        getToolbar()?.let {
            NavigationUI.setupActionBarWithNavController(
                (activity as AppCompatActivity),
                navController!!,
                getAppBarConfiguration()
            )
                it.setNavigationOnClickListener { navController!!.popBackStack() }
        }
        onInitVM()

        val parent = binding?.root?.getParent()
        if (parent != null) {
            (parent as ViewGroup).removeView(binding?.root)
        }
        return binding?.root
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
    }

    open fun getAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration(navController!!.graph)
    }
}